/**
 *
 * Copyright (c) 2016-2016, Emil Forslund. All Rights Reserved.
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
package com.github.pyknic.immutablearray.internal.shorts;

import com.github.pyknic.immutablearray.ByteImmutableArray;
import com.github.pyknic.immutablearray.ShortImmutableArray;
import com.github.pyknic.immutablearray.internal.EmptyImmutableArray;
import com.github.pyknic.immutablearray.internal.util.BitUtil;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.BUFFER_SIZE;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.outerIndex;
import com.github.pyknic.immutablearray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class ShortImmutableArrayBuilder
implements ShortImmutableArray.Builder {
    
    private final LinkedList<ShortBuffer> buffers;
    private short bitmask;
    private int outer, inner;
    
    public ShortImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }

    @Override
    public ShortImmutableArray.Builder append(short value) {
        final ShortBuffer current;
        
        // If the specified outer index is not yet allocated, do that first.
        if (outer == buffers.size()) {
            buffers.add(current = ByteBuffer.allocateDirect(
                BUFFER_SIZE * Short.BYTES
            ).asShortBuffer());
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
    public ShortImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        // Could every long in this array be converted into an byte?
        if (BitUtil.isLongToBytePossible(bitmask)) {
            final ByteImmutableArray.Builder builder =
                ByteImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.shortToByte(value)));
            buffers.forEach(MemoryUtil::clear);
            return (ShortImmutableArray) builder.build();
        }
        
        if (outer == 0) {
            if (inner < Short.MAX_VALUE) {
                final ShortBuffer current = buffers.getFirst();
                try {
                    final short[] array = new short[inner];
                    for (int i = 0; i < inner; i++) {
                        array[i] = current.get(i);
                    }
                    return new ShortImmutableArrayImpl(array);
                } finally {
                    MemoryUtil.clear(current);
                }
            } else {
                rescaleLastBuffer();
                return new ShortSingleBufferImmutableArrayImpl(buffers.getFirst(), inner);
            }
        } else {
            rescaleLastBuffer();
            return new ShortMultiBufferImmutableArrayImpl(
                bufferArray(), 
                length()
            );
        }
    }
    
    private long length() {
        return outer * BUFFER_SIZE + inner;
    }
    
    private ShortBuffer[] bufferArray() {
        return buffers.toArray(new ShortBuffer[outer + 1]);
    }
    
    private void forEachThenClear(ShortConsumer action) {
        final long length = length();
        final ShortBuffer[] bufferArray = bufferArray();
        
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
    
    private void rescaleLastBuffer() {
        final ShortBuffer last = buffers.removeLast();
        if (inner > 0) {
            if (inner < Short.MAX_VALUE) {
                final short[] temp = new short[inner];
                last.get(temp);
                MemoryUtil.clear(last);
                buffers.add(ShortBuffer.wrap(temp));
            } else {
                final ShortBuffer temp = ShortBuffer.allocate(inner);
                for (int i = 0; i < inner; i++) {
                    temp.put(i, last.get(i));
                }
                MemoryUtil.clear(last);
                buffers.add(temp);
            }
        }
    }
    
    @FunctionalInterface
    private interface ShortConsumer {
        void accept(short value);
    }
}