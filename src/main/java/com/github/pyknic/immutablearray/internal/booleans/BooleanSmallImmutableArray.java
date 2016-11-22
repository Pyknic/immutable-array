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
package com.github.pyknic.immutablearray.internal.booleans;

import com.github.pyknic.immutablearray.BooleanImmutableArray;
import com.github.pyknic.immutablearray.internal.util.BitUtil;
import static com.github.pyknic.immutablearray.internal.util.BitUtil.BITMASK_SIZE;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class BooleanSmallImmutableArray implements BooleanImmutableArray {
    
    private final short[] bitmasks;
    private final long length;

    public BooleanSmallImmutableArray(short[] bitmask, long length) {
        this.bitmasks = bitmask;
        this.length   = length;
    }

    @Override
    public boolean getAsBoolean(long index) {
        return BitUtil.isSet(bitmasks[(int) (index / BITMASK_SIZE)], index % BITMASK_SIZE);
    }

    @Override
    public long length() {
        return length;
    }
}