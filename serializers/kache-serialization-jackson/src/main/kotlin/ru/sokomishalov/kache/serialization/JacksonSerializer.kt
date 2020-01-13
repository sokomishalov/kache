/**
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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