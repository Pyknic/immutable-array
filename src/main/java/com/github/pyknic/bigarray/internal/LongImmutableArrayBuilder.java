/**
 *
 * Copyright (c) 2006-2016, Emil Forslund. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.pyknic.bigarray.internal;

import com.github.pyknic.bigarray.ByteImmutableArray;
import com.github.pyknic.bigarray.IntImmutableArray;
import com.github.pyknic.bigarray.LongImmutableArray;
import com.github.pyknic.bigarray.ShortImmutableArray;
import com.github.pyknic.bigarray.internal.util.BitUtil;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.BUFFER_SIZE;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.outerIndex;
import com.github.pyknic.bigarray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.LinkedList;
import java.util.function.LongConsumer;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class LongImmutableArrayBuilder
implements LongImmutableArray.Builder {
    
    private final LinkedList<LongBuffer> buffers;
    private long bitmask;
    private int outer, inner;
    
    public LongImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }

    @Override
    public LongImmutableArray.Builder append(long value) {
        final LongBuffer current;
        
        // If the specified outer index is not yet allocated, do that first.
        if (outer == buffers.size()) {
            buffers.add(current = ByteBuffer.allocateDirect(
                BUFFER_SIZE * Long.BYTES
            ).asLongBuffer());
        } else {
            current = buffers.getLast();
        }
        
        // Store the value at the specified index.
        current.put(inner, value);
        bitmask |= value;
        
        // If the inner index is about to overflow, reset it and increment outer
        // index until next time.
        if (BUFFER_SIZE == ++inner) {
            inner = 0;
            outer++;
        }
        
        return this;
    }

    @Override
    public LongImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        // Could every long in this array be converted into an byte?
        if (BitUtil.isLongToBytePossible(bitmask)) {
            final ByteImmutableArray.Builder builder =
                ByteImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.longToByte(value)));
            buffers.forEach(MemoryUtil::clear);
            return (LongImmutableArray) builder.build();
            
        // Could every long in this array be converted into an short?
        } else if (BitUtil.isLongToShortPossible(bitmask)) {
            final ShortImmutableArray.Builder builder =
                ShortImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.longToShort(value)));
            buffers.forEach(MemoryUtil::clear);
            return (LongImmutableArray) builder.build();
            
        // Could every long in this array be converted into an int?
        } else if (BitUtil.isLongToIntPossible(bitmask)) {
            final IntImmutableArray.Builder builder =
                IntImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.longToInt(value)));
            buffers.forEach(MemoryUtil::clear);
            return (LongImmutableArray) builder.build();
        }
        
        if (outer == 0) {
            final LongBuffer current = buffers.getFirst();
            if (inner < Short.MAX_VALUE) {
                try {
                    final long[] array = new long[inner];
                    for (int i = 0; i < inner; i++) {
                        array[i] = current.get(i);
                    }
                    return new LongImmutableArrayImpl(array);
                } finally {
                    MemoryUtil.clear(current);
                }
            } else {
                return new LongSingleBufferImmutableArrayImpl(current, inner);
            }
        } else {
            return new LongMultiBufferImmutableArrayImpl(
                bufferArray(), 
                length()
            );
        }
    }
    
    private long length() {
        return outer * BUFFER_SIZE + inner;
    }
    
    private LongBuffer[] bufferArray() {
        return buffers.toArray(new LongBuffer[outer + 1]);
    }
    
    private void forEachThenClear(LongConsumer action) {
        final long length = length();
        final LongBuffer[] bufferArray = bufferArray();
        
        for (long l = 0; l < length; l++) {
            final int o = outerIndex(l);
            final int i = innerIndex(l);
            
            action.accept(bufferArray[o].get(i));
            
            // If we just consumed the last value in this buffer, clear it.
            if (i + 1 == BUFFER_SIZE) {
                MemoryUtil.clear(bufferArray[o]);
            }
        }
    }
}