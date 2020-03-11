/**
 * Copyright (c) 2019-present Mikhael Sokolov
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

package ru.sokomishalov.kache.core.serialization.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import ru.sokomishalov.kache.core.Serializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class KryoSerializer(
        private val kryo: Kryo = DEFAULT_KRYO
) : Serializer {

    override fun <T> serialize(obj: T): ByteArray {
        return write(obj)
    }

    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T {
        return read(byteArray) as T
    }

    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> {
        return read(byteArray) as List<T>
    }

    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> {
        return read(byteArray) as Map<K, V>
    }

    private fun <T> write(obj: T): ByteArray {
        return ByteArrayOutputStream().use { baos ->
            Output(baos).use { kout ->
                kryo.writeClassAndObject(kout, obj)
                kout.flush()
                baos.toByteArray()
            }
        }
    }

    private fun read(byteArray: ByteArray): Any? {
        return ByteArrayInputStream(byteArray).use { bais ->
            Input(bais).use { kin ->
                kryo.readClassAndObject(kin)
            }
        }
    }

    companion object {
        val DEFAULT_KRYO = Kryo().apply {
            register(List::class.java)
            register(ArrayList::class.java)
            register(Map::class.java)
            register(HashMap::class.java)
            register(LinkedHashMap::class.java)
        }
    }
}