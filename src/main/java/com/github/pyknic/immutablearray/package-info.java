/**
 * The main interfaces of the immutable-array library are located in this 
 * package.
 * <h3>Supported Types</h3>
 * The following array types are supported.
 * <ul>
 *      <li>{@link com.github.pyknic.immutablearray.BooleanImmutableArray BooleanImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.ByteImmutableArray ByteImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.DoubleImmutableArray DoubleImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.FloatImmutableArray FloatImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.IntImmutableArray IntImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.LongImmutableArray LongImmutableArray}
 *      <li>{@link com.github.pyknic.immutablearray.ShortImmutableArray ShortImmutableArray}
 * </ul>
 * <h3>Usage</h3>
 * {@code
 *     LongImmutableArray array = LongImmutableArray.builder()
 *         .append(5)
 *         .append(100_232)
 *         .append(-32)
 *         .build();
 * }
 * <p>
 * If all values follow the same pattern, they can be compressed upon build.
 * {@code
 *     LongImmutableArray array = LongImmutableArray.builder()
 *         .append(1)
 *         .append(2)
 *         .append(3)
 *         .build(); // Will be backed internally by a byte[] of length 3
 * }
 * <p>
 * This package is part of the API. Modifications to classes here should only
 * (if ever) be done in major releases.
 */
package com.github.pyknic.immutablearray;