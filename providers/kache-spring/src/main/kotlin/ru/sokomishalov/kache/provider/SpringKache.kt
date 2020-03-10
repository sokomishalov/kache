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

import org.springframework.cache.Cache
import org.springframework.cache.concurrent.ConcurrentMapCache
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.model.GlobString

/**
 * @author sokomishalov
 */
class SpringKache(
        override val serializer: Serializer,
        private val cache: Cache = ConcurrentMapCache("cache")
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = cache.get(key)?.get() as ByteArray?

    override suspend fun putRaw(key: String, value: ByteArray) = cache.put(key, value)

    override suspend fun delete(key: String) = cache.evict(key)

    override suspend fun findKeys(glob: GlobString): List<String> = emptyList()

    override suspend fun deleteAll() = cache.clear()
}