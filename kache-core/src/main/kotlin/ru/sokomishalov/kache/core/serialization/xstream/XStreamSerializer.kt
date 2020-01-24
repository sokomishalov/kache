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