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
package com.github.pyknic.bigarray.internal.booleans;

import com.github.pyknic.bigarray.BooleanImmutableArray;
import com.github.pyknic.bigarray.internal.EmptyImmutableArray;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.BUFFER_SIZE;
import com.github.pyknic.bigarray.internal.util.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class BooleanImmutableArrayBuilder
implements BooleanImmutableArray.Builder {
    
    private final LinkedList<ShortBuffer> buffers;
    private int outer, inner, bit;
    
    public BooleanImmutableArrayBuilder() {
        buffers = new LinkedList<>();
        outer   = 0;
        inner   = 0;
        bit     = 0;
    }

    @Override
    public BooleanImmutableArray.Builder append(boolean value) {
        final ShortBuffer current;
        
        // If the specified outer index is not yet allocated, do that first.
        if (outer == buffers.size()) {
            buffers.add(current = ByteBuffer.allocateDirect(
                BUFFER_SIZE * Short.BYTES
            ).asShortBuffer());
        } else {
            current = buffers.getLast();
        }
        
        // Update the current bitmask
        short bitmask = current.get(inner);
        bitmask |= (1 << bit);
        current.put(inner, bitmask);
        
        // If the bitmask index is about to overflow, reset it and increment
        // inner index until next time.
        if (++bit == 16) {
            ++inner;
            bit = 0;
        }
        
        // If the inner index is about to overflow, reset it and increment outer
        // index until next time.
        if (BUFFER_SIZE == inner) {
            inner = 0;
            outer++;
        }
        
        return this;
    }

    @Override
    public BooleanImmutableArray build() {
        if (buffers.isEmpty()) {
            return new EmptyImmutableArray();
        }
        
        if (outer == 0) {
            final ShortBuffer current = buffers.getFirst();
            final long length = inner * 16 + bit;
            
            // If we are still on the first bitmask
            if (inner == 0 || (inner == 1 && bit == 0)) {
                try {
                    return new BooleanTinyImmutableArray(
                        current.get(0), inner * 16 + bit
                    );
                } finally {
                    MemoryUtil.clear(current);
                }
            } else if (inner < Short.MIN_VALUE) {
                try {
                    final short[] array = new short[inner];
                    for (int i = 0; i < inner; i++) {
                        array[i] = current.get(i);
                    }
                    return new BooleanSmallImmutableArray(array, length);
                } finally {
                    MemoryUtil.clear(current);
                }
            } else {
                rescaleLastBuffer();
                return new BooleanLargeImmutableArray(current, length);
            }
        } else {
            rescaleLastBuffer();
            return new BooleanVeryLargeImmutableArray(
                bufferArray(), 
                (outer * BUFFER_SIZE + inner) * 16 + bit
            );
        }
    }
    
    private ShortBuffer[] bufferArray() {
        return buffers.toArray(new ShortBuffer[outer + 1]);
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
}