@file:Suppress("UNCHECKED_CAST")

package ru.sokomishalov.kache.core.serialization.jdkserializable

import ru.sokomishalov.kache.core.Serializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class JdkSerializableSerializer : Serializer {

    override fun <T> serialize(obj: T): ByteArray {
        return ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).use { out ->
                out.writeObject(obj)
                bos.toByteArray()
            }
        }
    }

    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T {
        return deserialize(byteArray) as T
    }

    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> {
        return deserialize(byteArray) as List<T>
    }

    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> {
        return deserialize(byteArray) as Map<K, V>
    }

    private fun deserialize(byteArray: ByteArray): Any {
        return ByteArrayInputStream(byteArray).use { bis ->
            ObjectInputStream(bis).use { ois ->
                ois.readObject()
            }
        }
    }
}