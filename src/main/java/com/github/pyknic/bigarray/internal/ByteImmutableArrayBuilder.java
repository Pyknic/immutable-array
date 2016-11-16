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
import static com.github.pyknic.bigarray.internal.util.IndexUtil.BUFFER_SIZE;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.outerIndex;
import com.github.pyknic.bigarray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class ByteImmutableArrayBuilder
implements ByteImmutableArray.Builder {
    
    private final LinkedList<ByteBuffer> buffers;
    private int outer, inner;
    
    public ByteImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
    }

    @Override
    public ByteImmutableArray.Builder append(byte value) {
        final ByteBuffer current;
        
        // If the specified outer index is not yet allocated, do that first.
        if (outer == buffers.size()) {
            buffers.add(current = ByteBuffer.allocateDirect(BUFFER_SIZE));
        } else {
            current = buffers.getLast();
        }
        
        // Store the value at the specified index.
        current.put(inner, value);
        
        // If the inner index is about to overflow, reset it and increment outer
        // index until next time.
        if (BUFFER_SIZE == ++inner) {
            inner = 0;
            outer++;
        }
        
        return this;
    }

    @Override
    public ByteImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        if (outer == 0) {
            final ByteBuffer current = buffers.getFirst();
            if (inner < Short.MAX_VALUE) {
                try {
                    final byte[] array = new byte[inner];
                    for (int i = 0; i < inner; i++) {
                        array[i] = current.get(i);
                    }
                    return new ByteImmutableArrayImpl(array);
                } finally {
                    MemoryUtil.clear(current);
                }
            } else {
                return new ByteSingleBufferImmutableArrayImpl(current, inner);
            }
        } else {
            return new ByteMultiBufferImmutableArrayImpl(
                bufferArray(), 
                length()
            );
        }
    }
    
    private long length() {
        return outer * BUFFER_SIZE + inner;
    }
    
    private ByteBuffer[] bufferArray() {
        return buffers.toArray(new ByteBuffer[outer + 1]);
    }
    
    private void forEachThenClear(ByteConsumer action) {
        final long length = length();
        final ByteBuffer[] bufferArray = bufferArray();
        
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
    
    @FunctionalInterface
    private interface ByteConsumer {
        void accept(byte value);
    }
}