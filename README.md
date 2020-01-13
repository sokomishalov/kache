Kache
========
Kotlin/JVM coroutine-based cache abstraction.

[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Usage:
First of all, we have to import the project:
```xml
<dependencies>
    <!-- ...  -->
    <dependency>
        <groupId>com.github.sokomishalov</groupId>
        <artifactId>kache-{provider}</artifactId>
        <version>0.0.1</version>
    </dependency>
    <dependency>
        <groupId>com.github.sokomishalov</groupId>
        <artifactId>kache-{serializer}</artifactId>
        <version>0.0.1</version>
    </dependency>
    <!-- ...  -->
</dependencies>
```  

Serializers are required by most of cache implementations to 
serialize various types of data at different storage types. There are two serializer implementations so far:  
- [Jackson serialization (preferred)](./serializers/kache-serialization-jackson/src/main/kotlin/ru/sokomishalov/kache/serialization/JacksonSerializer.kt)
- [Gson serialization](./serializers/kache-serialization-gson/src/main/kotlin/ru/sokomishalov/kache/serialization/GsonSerializer.kt)

Providers: 
- [In-memory concurrent map implementation](./providers/kache-concurrent-map/src/main/kotlin/ru/sokomishalov/kache/provider/ConcurrentMapKache.kt)
- [Spring cache abstraction implementation](./providers/kache-spring-cache/src/main/kotlin/ru/sokomishalov/kache/provider/SpringKache.kt)
- [Redis implementation](./providers/redis/kache-redis-lettuce/src/main/kotlin/ru/sokomishalov/kache/provider/RedisLettuceKache.kt)
- [Mongo implementation](./providers/mongo/kache-mongo-reactive-streams/src/main/kotlin/ru/sokomishalov/kache/provider/MongoReactiveStreamsKache.kt)

