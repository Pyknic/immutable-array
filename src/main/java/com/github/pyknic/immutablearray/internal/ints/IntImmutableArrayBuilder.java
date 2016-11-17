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
package com.github.pyknic.immutablearray.internal.ints;

import com.github.pyknic.immutablearray.ByteImmutableArray;
import com.github.pyknic.immutablearray.IntImmutableArray;
import com.github.pyknic.immutablearray.ShortImmutableArray;
import com.github.pyknic.immutablearray.internal.EmptyImmutableArray;
import com.github.pyknic.immutablearray.internal.util.BitUtil;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.BUFFER_SIZE;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.outerIndex;
import com.github.pyknic.immutablearray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.function.IntConsumer;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class IntImmutableArrayBuilder
implements IntImmutableArray.Builder {
    
    private final LinkedList<IntBuffer> buffers;
    private int bitmask;
    private int outer, inner;
    
    public IntImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }

    @Override
    public IntImmutableArray.Builder append(int value) {
        final IntBuffer current;
        
        // If the specified outer index is not yet allocated, do that first.
        if (outer == buffers.size()) {
            buffers.add(current = ByteBuffer.allocateDirect(
                BUFFER_SIZE * Integer.BYTES
            ).asIntBuffer());
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
    public IntImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        // Could every long in this array be converted into an byte?
        if (BitUtil.isLongToBytePossible(bitmask)) {
            final ByteImmutableArray.Builder builder =
                ByteImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.intToByte(value)));
            buffers.forEach(MemoryUtil::clear);
            return (IntImmutableArray) builder.build();
            
        // Could every long in this array be converted into an short?
        } else if (BitUtil.isLongToShortPossible(bitmask)) {
            final ShortImmutableArray.Builder builder =
                ShortImmutableArray.builder();
            
            forEachThenClear(value -> builder.append(BitUtil.intToShort(value)));
            buffers.forEach(MemoryUtil::clear);
            return (IntImmutableArray) builder.build();
        }
        
        if (outer == 0) {
            if (inner < Short.MAX_VALUE) {
                final IntBuffer current = buffers.getFirst();
                try {
                    final int[] array = new int[inner];
                    for (int i = 0; i < inner; i++) {
                        array[i] = current.get(i);
                    }
                    return new IntImmutableArrayImpl(array);
                } finally {
                    MemoryUtil.clear(current);
                }
            } else {
                rescaleLastBuffer();
                return new IntSingleBufferImmutableArrayImpl(buffers.getFirst(), inner);
            }
        } else {
            rescaleLastBuffer();
            return new IntMultiBufferImmutableArrayImpl(
                bufferArray(), 
                length()
            );
        }
    }
    
    private long length() {
        return outer * BUFFER_SIZE + inner;
    }
    
    private IntBuffer[] bufferArray() {
        return buffers.toArray(new IntBuffer[outer + 1]);
    }
    
    private void forEachThenClear(IntConsumer action) {
        final long length = length();
        final IntBuffer[] bufferArray = bufferArray();
        
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
        final IntBuffer last = buffers.removeLast();
        if (inner > 0) {
            if (inner < Short.MAX_VALUE) {
                final int[] temp = new int[inner];
                last.get(temp);
                MemoryUtil.clear(last);
                buffers.add(IntBuffer.wrap(temp));
            } else {
                final IntBuffer temp = IntBuffer.allocate(inner);
                for (int i = 0; i < inner; i++) {
                    temp.put(i, last.get(i));
                }
                MemoryUtil.clear(last);
                buffers.add(temp);
            }
        }
    }
}