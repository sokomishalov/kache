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
package ru.sokomishalov.kache.provider.internal

import io.lettuce.core.codec.ByteArrayCodec
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import java.nio.ByteBuffer
import kotlin.text.Charsets.UTF_8

/**
 * @author sokomishalov
 */
@PublishedApi
internal class StringByteArrayCodec(
        private val keyCodec: RedisCodec<String, String> = StringCodec(UTF_8),
        private val valueCodec: RedisCodec<ByteArray, ByteArray> = ByteArrayCodec()
) : RedisCodec<String, ByteArray> {
    override fun encodeKey(key: String?): ByteBuffer = keyCodec.encodeKey(key)
    override fun decodeKey(bytes: ByteBuffer?): String = keyCodec.decodeKey(bytes)
    override fun encodeValue(value: ByteArray?): ByteBuffer = valueCodec.encodeValue(value)
    override fun decodeValue(bytes: ByteBuffer?): ByteArray = valueCodec.decodeValue(bytes)
}