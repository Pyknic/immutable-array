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
package com.github.pyknic.bigarray.internal.ints;

import com.github.pyknic.bigarray.FloatImmutableArray;
import com.github.pyknic.bigarray.IntImmutableArray;
import com.github.pyknic.bigarray.LongImmutableArray;
import java.nio.IntBuffer;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class IntSingleBufferImmutableArrayImpl 
implements IntImmutableArray, LongImmutableArray, FloatImmutableArray {

    private final IntBuffer buffer;
    private final int length;
    
    public IntSingleBufferImmutableArrayImpl(IntBuffer buffer, int length) {
        this.buffer = requireNonNull(buffer);
        this.length = length;
    }

    @Override
    public float getAsFloat(long index) {
        return Float.intBitsToFloat(getAsInt(index));
    }

    @Override
    public long getAsLong(long index) {
        return getAsInt(index);
    }
    
    @Override
    public int getAsInt(long index) {
        return buffer.get((int) index);
    }

    @Override
    public long length() {
        return length;
    }
}