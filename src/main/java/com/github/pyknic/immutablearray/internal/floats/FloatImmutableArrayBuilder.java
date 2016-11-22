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
package com.github.pyknic.immutablearray.internal.floats;

import com.github.pyknic.immutablearray.internal.ints.IntMultiBufferImmutableArrayImpl;
import com.github.pyknic.immutablearray.internal.ints.IntSingleBufferImmutableArrayImpl;
import com.github.pyknic.immutablearray.internal.ints.IntImmutableArrayImpl;
import com.github.pyknic.immutablearray.FloatImmutableArray;
import com.github.pyknic.immutablearray.internal.EmptyImmutableArray;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.BUFFER_SIZE;
import com.github.pyknic.immutablearray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class FloatImmutableArrayBuilder 
implements FloatImmutableArray.Builder {
    
    private final LinkedList<IntBuffer> buffers; // Store in raw format.
    private int outer, inner;

    public FloatImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }
    
    @Override
    public FloatImmutableArray.Builder append(float value) {
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
        current.put(inner, Float.floatToIntBits(value));
        
        // If the inner index is about to overflow, reset it and increment outer
        // index until next time.
        if (BUFFER_SIZE == ++inner) {
            inner = 0;
            outer++;
        }
        
        return this;
    }

    @Override
    public FloatImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
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