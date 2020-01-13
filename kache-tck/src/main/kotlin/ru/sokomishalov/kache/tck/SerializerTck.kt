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
@file:Suppress("FunctionName")

package ru.sokomishalov.kache.tck

import org.junit.Test
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.deserialize
import ru.sokomishalov.kache.core.deserializeList
import ru.sokomishalov.kache.core.deserializeMap
import ru.sokomishalov.kache.tck.internal.DummyModel
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

/**
 * @author sokomishalov
 */
abstract class SerializerTck {

    protected abstract val serializer: Serializer

    @Test
    open fun `Serialize raw value`() {
        val value = randomUUID().toString()
        val serialized = serializer.serialize(value)
        val deserialized = serializer.deserialize<String>(serialized)
        assertEquals(value, deserialized)
    }

    @Test
    open fun `Serialize pojo`() {
        val value = DummyModel()
        val serialized = serializer.serialize(value)
        val deserialized = serializer.deserialize<DummyModel>(serialized)
        assertEquals(value, deserialized)
    }

    @Test
    open fun `Serialize list`() {
        val value = (0L..10L).map { DummyModel(it) }
        val serialized = serializer.serialize(value)
        val deserialized = serializer.deserializeList<DummyModel>(serialized)
        assertEquals(value.size, deserialized.size)
        value.sorted().forEachIndexed { index, dummyModel -> assertEquals(dummyModel, deserialized.sorted()[index]) }
    }

    @Test
    open fun `Serialize map`() {
        val value = (0L..10L).map { it to DummyModel(it) }.toMap()
        val serialized = serializer.serialize(value)
        val deserialized = serializer.deserializeMap<Long, DummyModel>(serialized)
        assertEquals(value.size, deserialized.size)
        value.forEach { (k, v) -> assertEquals(v, deserialized[k]) }
    }
}