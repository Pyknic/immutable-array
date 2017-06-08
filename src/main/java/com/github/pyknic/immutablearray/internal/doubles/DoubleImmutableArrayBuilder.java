/**
 *
 * Copyright (c) 2016-2017, Emil Forslund. All Rights Reserved.
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
package com.github.pyknic.immutablearray.internal.doubles;

import com.github.pyknic.immutablearray.DoubleImmutableArray;
import com.github.pyknic.immutablearray.internal.EmptyImmutableArray;
import com.github.pyknic.immutablearray.internal.longs.LongImmutableArrayImpl;
import com.github.pyknic.immutablearray.internal.longs.LongMultiBufferImmutableArrayImpl;
import com.github.pyknic.immutablearray.internal.longs.LongSingleBufferImmutableArrayImpl;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.BUFFER_SIZE;
import com.github.pyknic.immutablearray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.LinkedList;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class DoubleImmutableArrayBuilder 
implements DoubleImmutableArray.Builder {
    
    private final LinkedList<LongBuffer> buffers; // Store in raw format.
    private int outer, inner;

    public DoubleImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }
    
    @Override
    public DoubleImmutableArray.Builder append(double value) {
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
        current.put(inner, Double.doubleToLongBits(value));
        
        // If the inner index is about to overflow, reset it and increment outer
        // index until next time.
        if (BUFFER_SIZE == ++inner) {
            inner = 0;
            outer++;
        }
        
        return this;
    }

    @Override
    public DoubleImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        if (outer == 0) {
            if (inner < Short.MAX_VALUE) {
                final LongBuffer current = buffers.getFirst();
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
                rescaleLastBuffer();
                return new LongSingleBufferImmutableArrayImpl(buffers.getFirst(), inner);
            }
        } else {
            rescaleLastBuffer();
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
    
    private void rescaleLastBuffer() {
        final LongBuffer last = buffers.removeLast();
        if (inner > 0) {
            if (inner < Short.MAX_VALUE) {
                final long[] temp = new long[inner];
                last.get(temp);
                MemoryUtil.clear(last);
                buffers.add(LongBuffer.wrap(temp));
            } else {
                final LongBuffer temp = LongBuffer.allocate(inner);
                for (int i = 0; i < inner; i++) {
                    temp.put(i, last.get(i));
                }
                MemoryUtil.clear(last);
                buffers.add(temp);
            }
        }
    }
}