package ru.sokomishalov.kache.core.serializer

/**
 * @author sokomishalov
 */

val DEFAULT_SERIALIZER: Serializer = object : Serializer {
    override fun <T> serialize(obj: T): ByteArray = TODO("serialize() not implemented")
    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T = TODO("deserialize() not implemented")
    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> = TODO("deserializeIterable() not implemented")
    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> = TODO("deserializeMap() not implemented")
}

inline fun <reified T> Serializer.deserialize(byteArray: ByteArray): T = deserialize(byteArray, T::class.java)
inline fun <reified T> Serializer.deserializeList(byteArray: ByteArray): Iterable<T> = deserializeList(byteArray, T::class.java)
inline fun <reified K, reified V> Serializer.deserializeMap(byteArray: ByteArray): Map<K, V> = deserializeMap(byteArray, K::class.java, V::class.java)