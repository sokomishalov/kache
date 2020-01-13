package ru.sokomishalov.kache.core

/**
 * @author sokomishalov
 */

import ru.sokomishalov.kache.core.serializer.DEFAULT_SERIALIZER
import ru.sokomishalov.kache.core.serializer.Serializer
import java.time.Duration
import java.time.temporal.TemporalAmount

/**
 * @author sokomishalov
 */
interface Kache {

    // ---------------------------------------------------------------------------------------------------------------------------------

    val serializer: Serializer get() = DEFAULT_SERIALIZER
    val cacheName: String get() = "cache"

    // ---------------------------------------------------------------------------------------------------------------------------------

    fun String.addPrefix(): String = "${cacheName}${this}"
    fun String.removePrefix(): String = removePrefix(cacheName)

    // ---------------------------------------------------------------------------------------------------------------------------------

    suspend fun getRaw(key: String): ByteArray?
    suspend fun putRaw(key: String, value: ByteArray)
    suspend fun expire(key: String, ttl: TemporalAmount)
    suspend fun delete(key: String)
    suspend fun findKeys(glob: String): List<String>

    // ---------------------------------------------------------------------------------------------------------------------------------

    suspend fun <T> getOne(key: String, clazz: Class<T>): T? {
        return getRaw(key)?.let { serializer.deserialize(it, clazz) }
    }

    suspend fun <T> getList(key: String, elementClass: Class<T>): List<T> {
        return getRaw(key)?.let { serializer.deserializeList(it, elementClass) } ?: emptyList()
    }

    suspend fun <T> getMap(key: String, valueClass: Class<T>): Map<String, T> {
        return getRaw(key)?.let { serializer.deserializeMap(it, String::class.java, valueClass) } ?: emptyMap()
    }

    suspend fun <T> getFromMap(key: String, mapKey: String, clazz: Class<T>): T? {
        return getMap(key, clazz)[mapKey]
    }

    suspend fun <T> put(key: String, value: T, ttl: TemporalAmount = Duration.ofSeconds(-1)) {
        putRaw(key, serializer.serialize(value))
        expireIfNeeded(key, ttl)
    }

    suspend fun <T> addToList(key: String, clazz: Class<T>, vararg values: T): List<T> {
        val list = getList(key, clazz).toMutableList()
        list += values.toList()
        put(key, list)
        return list
    }

    suspend fun <T> addToMap(key: String, clazz: Class<T>, additionalMap: Map<String, T>): Map<String, T> {
        val map = getMap(key, clazz).toMutableMap()
        map += additionalMap
        put(key, map)
        return map
    }

    suspend fun findAllKeys(): List<String> {
        return findKeys("*")
    }

    suspend fun <T : Any> find(pattern: String, elementClass: Class<T>): List<T> {
        return findKeys(pattern).mapNotNull { getRaw(it)?.let { v -> serializer.deserialize(v, elementClass) } }
    }

    suspend fun delete(keys: Iterable<String>) {
        keys.forEach { delete(it) }
    }

    suspend fun <T> deleteFromList(key: String, clazz: Class<T>, vararg values: T): List<T> {
        val list = getList(key, clazz).toMutableList()
        list -= values.toList()
        put(key, list)
        return list
    }

    suspend fun <T> deleteFromMap(key: String, clazz: Class<T>, removalMap: Map<String, T>): Map<String, T> {
        val map = getMap(key, clazz).toMutableMap()
        map -= removalMap.keys
        put(key, map)
        return map
    }

    suspend fun deleteAll() {
        findAllKeys().forEach { delete(it) }
    }

    suspend fun exists(key: String): Boolean {
        return getRaw(key.addPrefix()) != null
    }
}