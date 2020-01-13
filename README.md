Kache
========
Kotlin/JVM cache abstraction.

[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Usage:
First of all, we have to import the project:
```xml
<dependencies>
    <!-- ...   -->
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
    <!-- ...   -->
</dependencies>
```  

Providers: 
- [in-memory concurrent map](./providers/kache-concurrent-map/src/main/kotlin/ru/sokomishalov/kache/provider/ConcurrentMapKache.kt)
- [spring cache abstraction](./providers/kache-spring-cache/src/main/kotlin/ru/sokomishalov/kache/provider/SpringKache.kt)
- [redis](./providers/redis/kache-redis-lettuce/src/main/kotlin/ru/sokomishalov/kache/provider/RedisLettuceKache.kt)
- [mongo](./providers/mongo/kache-mongo-reactive-streams/src/main/kotlin/ru/sokomishalov/kache/provider/MongoReactiveStreamsKache.kt)

Serializers: 

