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
package com.github.pyknic.immutablearray.internal.shorts;

import com.github.pyknic.immutablearray.IntImmutableArray;
import com.github.pyknic.immutablearray.LongImmutableArray;
import com.github.pyknic.immutablearray.ShortImmutableArray;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
final class ShortImmutableArrayImpl 
implements ShortImmutableArray, IntImmutableArray, LongImmutableArray {

    private final short[] values;

    ShortImmutableArrayImpl(short[] values) {
        this.values = values;
    }

    @Override
    public long getAsLong(long index) {
        return getAsShort(index);
    }

    @Override
    public int getAsInt(long index) {
        return getAsShort(index);
    }
    
    @Override
    public short getAsShort(long index) {
        return values[(int) index];
    }

    @Override
    public long length() {
        return values.length;
    }
}