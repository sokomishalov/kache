@file:Suppress("unused")

package ru.sokomishalov.kache.core

/**
 * @author sokomishalov
 */

inline fun <reified T> Serializer.deserialize(byteArray: ByteArray): T = deserialize(byteArray, T::class.java)

inline fun <reified T> Serializer.deserializeList(byteArray: ByteArray): List<T> = deserializeList(byteArray, T::class.java)

inline fun <reified K, reified V> Serializer.deserializeMap(byteArray: ByteArray): Map<K, V> = deserializeMap(byteArray, K::class.java, V::class.java)