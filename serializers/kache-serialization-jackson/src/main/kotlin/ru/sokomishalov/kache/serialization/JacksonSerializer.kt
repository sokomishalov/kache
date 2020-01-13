@file:Suppress("unused")

package ru.sokomishalov.kache.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.sokomishalov.kache.core.Serializer

/**
 * @author sokomishalov
 */
class JacksonSerializer(
        private val objectMapper: ObjectMapper = jacksonObjectMapper()
) : Serializer {

    override fun <T> serialize(obj: T): ByteArray {
        return objectMapper.writeValueAsBytes(obj)
    }

    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T {
        return objectMapper.readValue(byteArray, toClass)
    }

    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> {
        return objectMapper.readValue(byteArray, objectMapper.typeFactory.constructCollectionType(List::class.java, elementClass))
    }

    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> {
        return objectMapper.readValue(byteArray, objectMapper.typeFactory.constructMapType(Map::class.java, keyClass, valueClass))
    }

}