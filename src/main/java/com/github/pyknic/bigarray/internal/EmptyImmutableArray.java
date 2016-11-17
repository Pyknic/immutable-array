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

import com.github.pyknic.bigarray.BooleanImmutableArray;
import com.github.pyknic.bigarray.ByteImmutableArray;
import com.github.pyknic.bigarray.DoubleImmutableArray;
import com.github.pyknic.bigarray.FloatImmutableArray;
import com.github.pyknic.bigarray.IntImmutableArray;
import com.github.pyknic.bigarray.LongImmutableArray;
import com.github.pyknic.bigarray.ShortImmutableArray;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class EmptyImmutableArray 
implements LongImmutableArray, 
           IntImmutableArray, 
           ShortImmutableArray, 
           ByteImmutableArray,
           FloatImmutableArray,
           DoubleImmutableArray,
           BooleanImmutableArray {
    
    public EmptyImmutableArray() {}
    
    @Override
    public long getAsLong(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public int getAsInt(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public short getAsShort(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public byte getAsByte(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }
    
    @Override
    public float getAsFloat(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public double getAsDouble(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public boolean getAsBoolean(long index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public long length() {
        return 0L;
    }
}