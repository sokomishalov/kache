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

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.ScanArgs
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.reactive.awaitFirstOrNull
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.util.unit
import ru.sokomishalov.kache.provider.internal.StringByteArrayCodec

/**
 * @author sokomishalov
 */

class RedisLettuceKache(
        override val serializer: Serializer,
        private val host: String = "localhost",
        private val port: Int = 27017,
        private val client: RedisClient = RedisClient.create(RedisURI.create(host, port)),
        private val connection: StatefulRedisConnection<String, ByteArray> = client.connect(StringByteArrayCodec())
) : Kache {

    override suspend fun getRaw(key: String): ByteArray? = connection.reactive().get(key).awaitFirstOrNull()

    override suspend fun putRaw(key: String, value: ByteArray) = connection.reactive().set(key, value).awaitFirstOrNull().unit()

    override suspend fun delete(key: String) = connection.reactive().del(key).awaitFirstOrNull().unit()

    override suspend fun expire(key: String, ttlMs: Long) = connection.reactive().pexpire(key, ttlMs).awaitFirstOrNull().unit()

    override suspend fun findKeysByGlob(glob: String): List<String> {
        return connection.reactive().scan(ScanArgs().match(glob)).awaitFirstOrNull()?.keys ?: emptyList()
    }

    override suspend fun exists(key: String): Boolean = connection.reactive().exists(key).awaitFirstOrNull() == 1L
}