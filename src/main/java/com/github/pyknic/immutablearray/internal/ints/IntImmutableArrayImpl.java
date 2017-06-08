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
package com.github.pyknic.immutablearray.internal.ints;

import com.github.pyknic.immutablearray.FloatImmutableArray;
import com.github.pyknic.immutablearray.IntImmutableArray;
import com.github.pyknic.immutablearray.LongImmutableArray;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class IntImmutableArrayImpl 
implements IntImmutableArray, LongImmutableArray, FloatImmutableArray {

    private final int[] values;

    public IntImmutableArrayImpl(int[] values) {
        this.values = values;
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
        return values[(int) index];
    }

    @Override
    public long length() {
        return values.length;
    }
}