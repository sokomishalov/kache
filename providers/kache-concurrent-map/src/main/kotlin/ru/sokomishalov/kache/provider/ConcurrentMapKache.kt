package ru.sokomishalov.kache.provider

import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.util.globToRegex
import ru.sokomishalov.kache.core.util.unit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author sokomishalov
 */
class ConcurrentMapKache(
        override val serializer: Serializer,
        private val map: ConcurrentMap<String, ByteArray> = ConcurrentHashMap()
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = map[key]

    override suspend fun putRaw(key: String, value: ByteArray) = map.put(key, value).unit()

    override suspend fun delete(key: String) = map.remove(key).unit()

    override suspend fun findKeysByGlob(glob: String): List<String> = map.keys.map { it }.filter { glob.globToRegex().matches(it) }

    override suspend fun exists(key: String): Boolean = map.keys.contains(key)

    override suspend fun deleteAll() = map.clear()

    // override some other methods for better performance
}