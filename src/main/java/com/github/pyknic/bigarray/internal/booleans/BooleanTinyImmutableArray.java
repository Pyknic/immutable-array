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
import com.github.pyknic.bigarray.internal.util.BitUtil;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class BooleanTinyImmutableArray implements BooleanImmutableArray {

    private final short bitmask;
    private final long length;

    public BooleanTinyImmutableArray(short bitmask, long length) {
        this.bitmask = bitmask;
        this.length  = length;
    }

    @Override
    public boolean getAsBoolean(long index) {
        return BitUtil.isSet(bitmask, index);
    }

    @Override
    public long length() {
        return length;
    }
}