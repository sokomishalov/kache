package ru.sokomishalov.kache.core.serializer.gson

import ru.sokomishalov.kache.core.serializer.Serializer

/**
 * @author sokomishalov
 */
class GsonSerializer : Serializer {
    override fun <T> serialize(obj: T): ByteArray = TODO("serialize() not implemented")
    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T = TODO("deserialize() not implemented")
    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> = TODO("deserializeList() not implemented")
    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> = TODO("deserializeMap() not implemented")
}