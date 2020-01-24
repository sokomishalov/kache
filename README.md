Kache
========
~~Here should be some modern logo~~

[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![](https://jitpack.io/v/sokomishalov/kache.svg)](https://jitpack.io/#sokomishalov/kache)


## Overview
Kotlin/JVM useful coroutine-based key-value cache abstraction with several implementations.

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
    //  Methods below have inefficient default implementation and should be overridden      // 
    //  for a better performance. This is one of main reasons why these methods are         //
    //  not extension functions.                                                            //
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
Serializers are required by cache implementations to serialize various types of data into different storage types. 
There are several serializer implementations so far:
- [JDK Serializable](./kache-core/src/main/kotlin/ru/sokomishalov/kache/core/serialization/jdkserializable/JdkSerializableSerializer.kt)
- [Jackson (preferred)](./kache-core/src/main/kotlin/ru/sokomishalov/kache/core/serialization/jackson/JacksonSerializer.kt)
- [Gson](././kache-core/src/main/kotlin/ru/sokomishalov/kache/core/serialization/gson/GsonSerializer.kt)
- [XStream](././kache-core/src/main/kotlin/ru/sokomishalov/kache/core/serialization/xstream/XStreamSerializer.kt)

## Implementations
There are several jvm kache implementations so far
- [kache-concurrent-map](#concurrent-map)
- [kache-spring](#integration-with-org.springframework.cache.Cache)
- [kache-redis-lettuce](#redis-with-reactive-Lettuce)
- [kache-mongo-reactive-streams](#mongo-with-reactive-streams-driver)

## Concurrent map
Import a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-concurrent-map</artifactId>
    <version>${kache.version}</version>
</dependency>
```
Then use [this implementation](./providers/kache-concurrent-map/src/main/kotlin/ru/sokomishalov/kache/provider/ConcurrentMapKache.kt):
```kotlin
val kache = ConcurrentMapKache(serializer = JacksonSerializer())
```


## Integration with org.springframework.cache.Cache
Import a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-spring</artifactId>
    <version>${kache.version}</version>
</dependency>
```
Then use [this implementation](./providers/kache-spring/src/main/kotlin/ru/sokomishalov/kache/provider/SpringKache.kt)
```kotlin
val kache = SpringKache(serializer = JacksonSerializer())
```


## Redis with reactive Lettuce
Import a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-redis-lettuce</artifactId>
    <version>${kache.version}</version>
</dependency>
```
Then use [this implementation](./providers/redis/kache-redis-lettuce/src/main/kotlin/ru/sokomishalov/kache/provider/RedisLettuceKache.kt)
```kotlin
val kache = RedisLettuceKache(serializer = JacksonSerializer(), client = RedisClient.create())
```


## Mongo with reactive streams driver
Import a dep:
```xml
<dependency>
    <groupId>com.github.sokomishalov</groupId>
    <artifactId>kache-mongo-reactive-streams</artifactId>
    <version>${kache.version}</version>
</dependency>
```
Then use [this implementation](./providers/mongo/kache-mongo-reactive-streams/src/main/kotlin/ru/sokomishalov/kache/provider/MongoReactiveStreamsKache.kt)
```kotlin
val kache = MongoReactiveStreamsKache(serializer = JacksonSerializer(), client = MongoClients.create())
```