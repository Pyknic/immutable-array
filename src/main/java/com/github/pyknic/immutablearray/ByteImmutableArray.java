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
package com.github.pyknic.immutablearray;

import com.github.pyknic.immutablearray.internal.bytes.ByteImmutableArrayBuilder;

/**
 * An immutable byte array that can hold a very large number of elements. 
 * Implementations of this interface may or may not be backed by a direct buffer 
 * depending on the size and how the data contained is structured.
 * <p>
 * To create a new instance of this interface, use the builder pattern initiated
 * with the {@link #builder()} method. The actual implementation that is used
 * will depend on the data that is passed to the builder. After the instance has
 * been built, it will be immutable.
 * 
 * @author Emil Forslund
 * @since  1.0.0
 */
public interface ByteImmutableArray {
    
    /**
     * Returns the value located at the specified index in the array. If the
     * index is outside the bounds of the array, the behavior is unspecified.
     * 
     * @param index  the index
     * @return       the value at that index
     */
    byte getAsByte(long index);
    
    /**
     * Returns the length of the array (the number of bytes that can safely be
     * retrieved using the {@link #getAsByte(long)} method).
     * 
     * @return  the length
     */
    long length();
    
    /**
     * Creates a new builder. The builder guarantees that appended values will
     * be stored in the same order as they were inserted, but the actual size of
     * the array and how it is backed will be determined when the 
     * {@link Builder#build()} method is finally invoked.
     * <p>
     * The builder should not be used after the {@link Builder#build()}-method
     * has been called.
     * 
     * @return  a new builder
     */
    static Builder builder() {
        return new ByteImmutableArrayBuilder();
    }
    
    /**
     * A builder for a {@link ByteImmutableArray}. The builder can be appended
     * with values using the {@link #append(byte)}-method until it is finalized
     * using the {@link #build()}-method. The backing implementation will be
     * decided once the array is built.
     * <p>
     * The implementation of {@code Builder} is intended to handle a very large
     * amount of bytes as well as a very small amount.
     */
    interface Builder {
        
        /**
         * Append an value to the builder.
         * 
         * @param value  the value to append
         * @return       a reference to this builder
         */
        ByteImmutableArray.Builder append(byte value);
    
        /**
         * Builds the array. After this method has been called, the builder
         * should be discarded. The returned array is immutable.
         * 
         * @return  the built array
         */
        ByteImmutableArray build();
        
    }
}