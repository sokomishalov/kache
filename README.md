Kache
========
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)


## Overview
Kotlin/JVM useful coroutine-based cache abstraction with several implementations.

## Distribution
Library with modules are available only from `jitpack` so far:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## Kache abstraction
Library provides common [interface](./kache-core/src/main/kotlin/ru/sokomishalov/kache/core/Kache.kt) for cache operations:
```kotlin
interface Kache {

    // -----------------------------------------------------------------------------------//
    //  Methods that must be implemented.                                                 //
    // -----------------------------------------------------------------------------------//

    val serializer: Serializer
    suspend fun getRaw(key: String): ByteArray?
    suspend fun putRaw(key: String, value: ByteArray)
    suspend fun findKeysByGlob(glob: String): List<String>
    suspend fun delete(key: String)

    // -------------------------------------------------------------------------------------//
    //  Methods that should be overridden for a better efficiency of implementation.        //
    //  This is one of main reasons why these methods are not extension functions.          //
    // -------------------------------------------------------------------------------------//

    suspend fun <T> getOne(key: String, clazz: Class<T>): T? { /*...*/ }
    suspend fun <T> getList(key: String, elementClass: Class<T>): List<T> { /*...*/ }
    suspend fun <T> getMap(key: String, valueClass: Class<T>): Map<String, T> { /*...*/ }
    suspend fun <T> getFromMap(key: String, mapKey: String, clazz: Class<T>): T? { /*...*/ }
    suspend fun <T> put(key: String, value: T) { /*...*/ }
    suspend fun <T> addToList(key: String, clazz: Class<T>, vararg values: T): List<T> { /*...*/ }
    suspend fun <T> addToMap(key: String, clazz: Class<T>, additionalMap: Map<String, T>): Map<String, T> { /*...*/ }
    suspend fun findAllKeys(): List<String> { /*...*/ }
    suspend fun <T : Any> find(glob: String, elementClass: Class<T>): List<T> { /*...*/ }
    suspend fun delete(keys: Iterable<String>) { /*...*/ }
    suspend fun <T> deleteFromList(key: String, clazz: Class<T>, vararg values: T): List<T> { /*...*/ }
    suspend fun <T> deleteFromMap(key: String, clazz: Class<T>, removalMap: Map<String, T>): Map<String, T> { /*...*/ }
    suspend fun deleteAll() { /*...*/ }
    suspend fun exists(key: String): Boolean { /*...*/ }
    suspend fun expire(key: String, ttlMs: Long) { /*...*/ }
}
```

## Serializers
Serializers are required by cache implementations to serialize various types of data into different 
storage types. There are several serializer implementations so far:
- [kache-serialization-jackson (preferred)](./serializers/kache-serialization-jackson/src/main/kotlin/ru/sokomishalov/kache/serialization/JacksonSerializer.kt)
- [kache-serialization-gson](./serializers/kache-serialization-gson/src/main/kotlin/ru/sokomishalov/kache/serialization/GsonSerializer.kt)

Import as a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-{serialization-module}</artifactId>
    <version>${kache.version}</version>
</dependency>
```

## Implementations
There are several jvm kache implementations so far
- [kache-concurrent-map](./providers/kache-concurrent-map/src/main/kotlin/ru/sokomishalov/kache/provider/ConcurrentMapKache.kt)
- [kache-spring](./providers/kache-spring/src/main/kotlin/ru/sokomishalov/kache/provider/SpringKache.kt)
- [kache-redis-lettuce](./providers/redis/kache-redis-lettuce/src/main/kotlin/ru/sokomishalov/kache/provider/RedisLettuceKache.kt)
- [kache-mongo-reactive-streams](./providers/mongo/kache-mongo-reactive-streams/src/main/kotlin/ru/sokomishalov/kache/provider/MongoReactiveStreamsKache.kt)

Import as a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-{provider-module}</artifactId>
    <version>${kache.version}</version>
</dependency>
```