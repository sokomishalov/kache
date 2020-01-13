package ru.sokomishalov.kache.core

/**
 * @author sokomishalov
 */
interface Serializer {
    fun <T> serialize(obj: T): ByteArray
    fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T
    fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T>
    fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V>
}