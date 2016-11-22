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
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.immutablearray.internal.util.IndexUtil.outerIndex;
import java.nio.ShortBuffer;

/**
 *
 * @author Emil Forslund
 * @since  1.0.1
 */
public final class BooleanVeryLargeImmutableArray implements BooleanImmutableArray {
    
    private final ShortBuffer[] buffers;
    private final long length;

    public BooleanVeryLargeImmutableArray(ShortBuffer[] buffers, long length) {
        this.buffers = buffers;
        this.length = length;
    }

    @Override
    public boolean getAsBoolean(long index) {
        final long idx = index / BITMASK_SIZE;
        final short bitmask = buffers[outerIndex(idx)].get(innerIndex(idx));
        return BitUtil.isSet(bitmask, index % BITMASK_SIZE);
    }

    @Override
    public long length() {
        return length;
    }
}