package ru.sokomishalov.kache.provider

import org.springframework.cache.Cache
import org.springframework.cache.concurrent.ConcurrentMapCache
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.util.unit

/**
 * @author sokomishalov
 */
class SpringKache(
        override val serializer: Serializer,
        private val cache: Cache = ConcurrentMapCache("cache")
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = cache.get(key)?.get() as ByteArray?

    override suspend fun putRaw(key: String, value: ByteArray) = cache.put(key, value).unit()

    override suspend fun delete(key: String) = cache.evict(key).unit()

    override suspend fun findKeysByGlob(glob: String): List<String> = emptyList()

    override suspend fun deleteAll() = cache.clear()

    // override some methods for better performance
}