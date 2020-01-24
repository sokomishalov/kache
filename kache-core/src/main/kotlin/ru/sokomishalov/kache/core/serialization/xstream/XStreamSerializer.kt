@file:Suppress("UNCHECKED_CAST")

package ru.sokomishalov.kache.core.serialization.xstream

import com.thoughtworks.xstream.XStream
import ru.sokomishalov.kache.core.Serializer
import kotlin.text.Charsets.UTF_8


class XStreamSerializer(
        private val xStream: XStream = XStream()
) : Serializer {
    override fun <T> serialize(obj: T): ByteArray {
        return xStream.toXML(obj).toByteArray(UTF_8)
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
        return xStream.fromXML(byteArray.toString(UTF_8))
    }
}