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
package com.github.pyknic.bigarray.internal.util;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class BitUtil {
    
    public static boolean isLongToBytePossible(long value) {
        return 0L == (value & 0x7fffffffffffff80L);
    }
    
    public static boolean isLongToShortPossible(long value) {
        return 0L == (value & 0x7fffffffffff8000L);
    }
    
    public static boolean isLongToIntPossible(long value) {
        return 0L == (value & 0x7fffffff80000000L);
    }
    
    public static boolean isIntToBytePossible(int value) {
        return 0 == (value & 0x7fffff80);
    }
    
    public static boolean isIntToShortPossible(int value) {
        return 0 == (value & 0x7fff8000);
    }
    
    public static boolean isShortToBytePossible(short value) {
        return 0 == (value & 0x7f80);
    }
    
    public static byte longToByte(long value) {
        return isLongNegative(value)
            ? (byte) (0x7f & value | 0x80)
            : (byte) (0x7f & value);
    }
    
    public static short longToShort(long value) {
        return isLongNegative(value)
            ? (short) (0x7fff & value | 0x8000)
            : (short) (0x7fff & value);
    }
    
    public static short longToInt(long value) {
        return isLongNegative(value)
            ? (short) (0x7fffffff & value | 0x80000000)
            : (short) (0x7fffffff & value);
    }
    
    public static byte intToByte(int value) {
        return isIntNegative(value)
            ? (byte) (0x7f & value | 0x80)
            : (byte) (0x7f & value);
    }
    
    public static short intToShort(int value) {
        return isIntNegative(value)
            ? (short) (0x7fff & value | 0x8000)
            : (short) (0x7fff & value);
    }
    
    public static byte shortToByte(short value) {
        return isShortNegative(value)
            ? (byte) (0x7f & value | 0x80)
            : (byte) (0x7f & value);
    }
    
    public static boolean isLongNegative(long value) {
        return 0x8000000000000000L == (value & 0x8000000000000000L);
    }
    
    public static boolean isIntNegative(int value) {
        return 0x80000000 == (value & 0x80000000);
    }
    
    public static boolean isShortNegative(short value) {
        return 0x8000 == (value & 0x8000);
    }
    
    private BitUtil() {}
    
}