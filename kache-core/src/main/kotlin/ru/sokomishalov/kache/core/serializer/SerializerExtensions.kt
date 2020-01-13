@file:Suppress("unused")

package ru.sokomishalov.kache.core.serializer

import ru.sokomishalov.kache.core.serializer.jackson.JacksonSerializer

/**
 * @author sokomishalov
 */

val DEFAULT_SERIALIZER: Serializer = JacksonSerializer()

inline fun <reified T> Serializer.deserialize(byteArray: ByteArray): T = deserialize(byteArray, T::class.java)
inline fun <reified T> Serializer.deserializeList(byteArray: ByteArray): Iterable<T> = deserializeList(byteArray, T::class.java)
inline fun <reified K, reified V> Serializer.deserializeMap(byteArray: ByteArray): Map<K, V> = deserializeMap(byteArray, K::class.java, V::class.java)