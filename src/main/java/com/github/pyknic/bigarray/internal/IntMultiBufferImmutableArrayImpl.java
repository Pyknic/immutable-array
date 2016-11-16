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

import com.github.pyknic.bigarray.IntImmutableArray;
import com.github.pyknic.bigarray.LongImmutableArray;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.innerIndex;
import static com.github.pyknic.bigarray.internal.util.IndexUtil.outerIndex;
import java.nio.IntBuffer;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
final class IntMultiBufferImmutableArrayImpl 
implements IntImmutableArray, LongImmutableArray {
    
    private final IntBuffer[] buffers;
    private final long length;

    IntMultiBufferImmutableArrayImpl(IntBuffer[] buffers, long length) {
        this.buffers = buffers;
        this.length  = length;
    }

    @Override
    public long getAsLong(long index) {
        return getAsInt(index);
    }

    @Override
    public int getAsInt(long index) {
        return buffers[outerIndex(index)].get(innerIndex(index));
    }

    @Override
    public long length() {
        return length;
    }
}