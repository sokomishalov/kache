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
package ru.sokomishalov.kache.core

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author sokomishalov
 */

/**
 * @author sokomishalov
 */
interface Kache {

    // -----------------------------------------------------------------------------------//
    //  Methods that must be implemented.                                                 //
    // -----------------------------------------------------------------------------------//

    val serializer: Serializer
    suspend fun getRaw(key: String): ByteArray?
    suspend fun putRaw(key: String, value: ByteArray)
    suspend fun findKeysByGlob(glob: String): List<String>
    suspend fun delete(key: String)

    // -------------------------------------------------------------------------------------//
    //  Methods that should be overridden for a better efficiency of implementation.        //
    //  This is one of main reasons why these methods are not extension functions.          //
    // -------------------------------------------------------------------------------------//

    suspend fun <T> getOne(key: String, clazz: Class<T>): T? {
        return getRaw(key)?.let { serializer.deserialize(it, clazz) }
    }

    suspend fun <T> getList(key: String, elementClass: Class<T>): List<T> {
        return getRaw(key)?.let { serializer.deserializeList(it, elementClass) } ?: emptyList()
    }

    suspend fun <T> getMap(key: String, valueClass: Class<T>): Map<String, T> {
        return getRaw(key)?.let { serializer.deserializeMap(it, String::class.java, valueClass) } ?: emptyMap()
    }

    suspend fun <T> getFromMap(key: String, mapKey: String, clazz: Class<T>): T? {
        return getMap(key, clazz)[mapKey]
    }

    suspend fun <T> put(key: String, value: T) {
        putRaw(key, serializer.serialize(value))
    }

    suspend fun <T> addToList(key: String, clazz: Class<T>, vararg values: T): List<T> {
        val list = getList(key, clazz).toMutableList()
        list += values.toList()
        put(key, list)
        return list
    }

    suspend fun <T> addToMap(key: String, clazz: Class<T>, additionalMap: Map<String, T>): Map<String, T> {
        val map = getMap(key, clazz).toMutableMap()
        map += additionalMap
        put(key, map)
        return map
    }

    suspend fun findAllKeys(): List<String> {
        return findKeysByGlob("*")
    }

    suspend fun <T : Any> find(glob: String, elementClass: Class<T>): List<T> {
        return findKeysByGlob(glob).mapNotNull { getRaw(it)?.let { v -> serializer.deserialize(v, elementClass) } }
    }

    suspend fun delete(keys: Iterable<String>) {
        keys.forEach { delete(it) }
    }

    suspend fun <T> deleteFromList(key: String, clazz: Class<T>, vararg values: T): List<T> {
        val list = getList(key, clazz).toMutableList()
        list -= values.toList()
        put(key, list)
        return list
    }

    suspend fun <T> deleteFromMap(key: String, clazz: Class<T>, removalMap: Map<String, T>): Map<String, T> {
        val map = getMap(key, clazz).toMutableMap()
        map -= removalMap.keys
        put(key, map)
        return map
    }

    suspend fun deleteAll() {
        findAllKeys().forEach { delete(it) }
    }

    suspend fun exists(key: String): Boolean {
        return getRaw(key) != null
    }

    suspend fun expire(key: String, ttlMs: Long) {
        if (ttlMs > 0) {
            GlobalScope.launch {
                delay(ttlMs)
                delete(key)
            }
        }
    }
}