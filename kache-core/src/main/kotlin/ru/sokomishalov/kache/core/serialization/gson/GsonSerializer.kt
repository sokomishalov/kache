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
@file:Suppress("unused")

package ru.sokomishalov.kache.core.serialization.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.sokomishalov.kache.core.Serializer
import kotlin.text.Charsets.UTF_8


/**
 * @author sokomishalov
 */
class GsonSerializer(
        private val gson: Gson = Gson()
) : Serializer {

    override fun <T> serialize(obj: T): ByteArray {
        return gson.toJson(obj).toByteArray(UTF_8)
    }

    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T {
        return gson.fromJson(byteArray.toString(UTF_8), toClass)
    }

    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> {
        return gson.fromJson<List<T>>(byteArray.toString(UTF_8), TypeToken.getParameterized(List::class.java, elementClass).type)
    }

    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> {
        return gson.fromJson<Map<K, V>>(byteArray.toString(UTF_8), TypeToken.getParameterized(Map::class.java, keyClass, valueClass).type)
    }

}