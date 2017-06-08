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
package com.github.pyknic.immutablearray.internal.util;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class BitUtil {
    
    public final static int BITMASK_SIZE = 16;
    
    public static boolean isSet(short bitmask, long index) {
        switch ((short) (index & 0xff)) {
            case 0  : return 0b1                == (bitmask & 0b1);
            case 1  : return 0b10               == (bitmask & 0b10);
            case 2  : return 0b100              == (bitmask & 0b100);
            case 3  : return 0b1000             == (bitmask & 0b1000);
            case 4  : return 0b10000            == (bitmask & 0b10000);
            case 5  : return 0b100000           == (bitmask & 0b100000);
            case 6  : return 0b1000000          == (bitmask & 0b1000000);
            case 7  : return 0b10000000         == (bitmask & 0b10000000);
            case 8  : return 0b100000000        == (bitmask & 0b100000000);
            case 9  : return 0b1000000000       == (bitmask & 0b1000000000);
            case 10 : return 0b10000000000      == (bitmask & 0b10000000000);
            case 11 : return 0b100000000000     == (bitmask & 0b100000000000);
            case 12 : return 0b1000000000000    == (bitmask & 0b1000000000000);
            case 13 : return 0b10000000000000   == (bitmask & 0b10000000000000);
            case 14 : return 0b100000000000000  == (bitmask & 0b100000000000000);
            case 15 : return 0b1000000000000000 == (bitmask & 0b1000000000000000);
            default : throw new ArrayIndexOutOfBoundsException();
        }
    }
    
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