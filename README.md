# Immutable Array
Read-only primitive Java arrays backed by Direct Buffers and indexed using 64-bit indexes

The library uses a Builder Pattern for the array classes. When the builder is finalized, an appropriate implementation of the interface is choosen to fit the data. Here are some optimizations that is done upon build:
* Special implementation for empty arrays
* Small arrays are backed by an OnHeap array (regular `long[]`, `int[]`, etc)
* Medium sized arrays are backed by a single `DirectBuffer`
* Very large arrays (more than can be indexed with a 32-bit int) are backed by a number of direct buffers
* If all values fit a smaller primitive, they will be warped (`long` to `int`, `int` to `short` etc)

# Features
* 64-bit indexing
* Thread-safe (after build() has been called)
* Immutable using a Builder pattern
* Variable backing structure depending on the data
* Allocated buffers are cleared as soon as they are no longer used (no need to wait for GC)

## Example
```java
LongImmutableArray array = LongImmutableArray.builder()
    .append(5)
    .append(100_232)
    .append(-32)
    .build();
```

If all values follow the same pattern, they can be compressed upon build.
```java
LongImmutableArray array = LongImmutableArray.builder()
    .append(1)
    .append(2)
    .append(3)
    .build(); // Will be backed internally by a `byte[]` of length 3
```

## Usage
Add the following to your `pom.xml`-file. The library has no external dependencies.
```xml
<dependency>
    <groupId>com.github.pyknic</groupId>
    <artifactId>immutable-array</artifactId>
    <version>1.0.0</version>
</dependency>
```

## License
This project is available under the [Apache 2 license](http://www.apache.org/licenses/LICENSE-2.0). 