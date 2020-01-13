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
package ru.sokomishalov.kache.provider

import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.util.globToRegex
import ru.sokomishalov.kache.core.util.unit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author sokomishalov
 */
class ConcurrentMapKache(
        override val serializer: Serializer,
        private val map: ConcurrentMap<String, ByteArray> = ConcurrentHashMap()
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = map[key]

    override suspend fun putRaw(key: String, value: ByteArray) = map.put(key, value).unit()

    override suspend fun delete(key: String) = map.remove(key).unit()

    override suspend fun findKeysByGlob(glob: String): List<String> = map.keys.map { it }.filter { glob.globToRegex().matches(it) }

    override suspend fun exists(key: String): Boolean = map.keys.contains(key)

    override suspend fun deleteAll() = map.clear()

    // override some other methods for better performance
}