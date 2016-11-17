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
package com.github.pyknic.bigarray.internal.longs;

import com.github.pyknic.bigarray.DoubleImmutableArray;
import com.github.pyknic.bigarray.LongImmutableArray;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class LongImmutableArrayImpl 
implements LongImmutableArray, DoubleImmutableArray {

    private final long[] values;

    public LongImmutableArrayImpl(long[] values) {
        this.values = values;
    }
    
    @Override
    public double getAsDouble(long index) {
        return Double.longBitsToDouble(getAsLong(index));
    }

    @Override
    public long getAsLong(long index) {
        return values[(int) index];
    }

    @Override
    public long length() {
        return values.length;
    }
}