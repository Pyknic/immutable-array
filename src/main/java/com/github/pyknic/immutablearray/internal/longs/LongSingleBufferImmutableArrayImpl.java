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
package com.github.pyknic.immutablearray.internal.longs;

import com.github.pyknic.immutablearray.DoubleImmutableArray;
import com.github.pyknic.immutablearray.LongImmutableArray;
import java.nio.LongBuffer;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class LongSingleBufferImmutableArrayImpl 
implements LongImmutableArray, DoubleImmutableArray {

    private final LongBuffer buffer;
    private final int length;
    
    public LongSingleBufferImmutableArrayImpl(LongBuffer buffer, int length) {
        this.buffer = requireNonNull(buffer);
        this.length = length;
    }

    @Override
    public double getAsDouble(long index) {
        return Double.longBitsToDouble(getAsLong(index));
    }
    
    @Override
    public long getAsLong(long index) {
        return buffer.get((int) index);
    }

    @Override
    public long length() {
        return length;
    }
}