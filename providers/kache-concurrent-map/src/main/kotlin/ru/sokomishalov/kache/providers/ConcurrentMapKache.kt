package ru.sokomishalov.kache.providers

import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.serializer.DEFAULT_SERIALIZER
import ru.sokomishalov.kache.core.serializer.Serializer
import ru.sokomishalov.kache.core.util.convertGlobToRegex
import ru.sokomishalov.kache.core.util.unit
import java.time.temporal.TemporalAmount
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author sokomishalov
 */
class ConcurrentMapKache(
        override val cacheName: String = "cache:",
        override val serializer: Serializer = DEFAULT_SERIALIZER,
        private val map: ConcurrentMap<String, ByteArray> = ConcurrentHashMap()
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = map[key.addPrefix()]

    override suspend fun putRaw(key: String, value: ByteArray) = map.put(key.addPrefix(), value).unit()

    override suspend fun delete(key: String) = unit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = throw UnsupportedOperationException()

    override suspend fun findKeys(glob: String): List<String> = map.keys.map { it.removePrefix() }.filter { glob.convertGlobToRegex().matches(it) }

    override suspend fun exists(key: String): Boolean = map.keys.contains(key.addPrefix())

    override suspend fun deleteAll() = map.clear()

    // override some methods for better performance
}