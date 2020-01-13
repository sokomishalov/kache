/**
 * Copyright 2019-2019 the original author or authors.
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

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test
import ru.sokomishalov.kache.core.*
import ru.sokomishalov.kache.tck.internal.DummyModel
import java.util.UUID.randomUUID
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

abstract class KacheTck {

    protected abstract val kache: Kache

    @After
    open fun tearDown() = runBlocking {
        kache.deleteAll()
    }

    @Test
    open fun `Put strings`() = runBlocking {
        val data = listOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
        )
        data.forEach { (key, value) ->
            kache.put(key, value)
        }

        data.shuffled().forEach { (key, value) ->
            assertEquals(value, kache.getOne<String>(key))
        }
    }

    @Test
    open fun `Put and delete strings`() = runBlocking {
        kache.put(CACHE_KEY, CACHE_VALUE)
        assertEquals(CACHE_VALUE, kache.getOne<String>(CACHE_KEY))

        kache.delete(CACHE_KEY)
        assertNull(kache.getOne(CACHE_KEY))
    }

    @Test
    open fun `Put and replace strings`() = runBlocking {
        kache.put(CACHE_KEY, CACHE_VALUE)
        assertEquals(CACHE_VALUE, kache.getOne<String>(CACHE_KEY))

        val newValue = "newValue"
        kache.put(CACHE_KEY, newValue)
        assertEquals(newValue, kache.getOne<String>(CACHE_KEY))
    }

    @Test
    open fun `Put objects`() = runBlocking {
        val data = mutableListOf(
                "key1" to DummyModel(MIN_VALUE, "firstDummy"),
                "key2" to DummyModel(0, "secondDummy"),
                "key3" to DummyModel(MAX_VALUE, "thirdDummy")
        )
        data.forEach { (key, value) ->
            kache.put(key, value)
        }

        data.shuffled().forEach { (key, value) ->
            assertEquals(value, kache.getOne<DummyModel>(key))
        }
    }

    @Test
    open fun `Put and delete objects`() {
        return runBlocking {
            val dummy = DummyModel(1, "DummyModel")
            kache.put(CACHE_KEY, dummy)
            assertEquals(dummy, kache.getOne<DummyModel>(CACHE_KEY))

            kache.delete(CACHE_KEY)
            assertNull(kache.getOne<DummyModel>(CACHE_KEY))
        }
    }

    @Test
    open fun `Put and replace object`() = runBlocking {
        val firstDummy = DummyModel(MIN_VALUE, "firstDummy")
        kache.put(CACHE_KEY, firstDummy)
        assertEquals(firstDummy, kache.getOne<DummyModel>(CACHE_KEY))

        val secondDummy = DummyModel(MAX_VALUE, "secondDummy")
        kache.put(CACHE_KEY, secondDummy)
        assertEquals(secondDummy, kache.getOne<DummyModel>(CACHE_KEY))
    }

    @Test
    open fun `Get non existing value`() = runBlocking {
        assertNull(kache.getOne<String>(randomUUID().toString()))
        assertEquals("kek", kache.getOne(randomUUID().toString()) { "kek" })
    }

    @Test
    open fun `Put string list`() = runBlocking {
        val data = listOf("value1", "value2", "value3")
        kache.put(CACHE_KEY, data)

        val result = kache.getList<String>(CACHE_KEY)
        assertEquals(data, result.sorted())
    }

    @Test
    open fun `Put object list`() = runBlocking {
        val data = mutableListOf(
                DummyModel(1, "aFirstDummy"),
                DummyModel(2, "bSecondDummy"),
                DummyModel(3, "cThirdDummy")
        )
        kache.put(CACHE_KEY, data)

        val result = kache.getList<DummyModel>(CACHE_KEY)
        assertEquals(data, result.sorted())
    }

    @Test
    open fun `Put empty list`() = runBlocking {
        kache.put(CACHE_KEY, emptyList<Any>())
        assertEquals(emptyList<Any>(), kache.getList<Any>(CACHE_KEY))
    }

    @Test
    open fun `Get non existing list`() = runBlocking {
        assertEquals(emptyList<Any>(), kache.getList<Any>(CACHE_KEY))
        assertEquals(listOf("kek"), kache.getList<Any>(CACHE_KEY) { listOf("kek") })
    }

    @Test
    open fun `Put map`() = runBlocking {
        val data = mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
        )
        kache.put(CACHE_KEY, data)

        assertEquals(data, kache.getMap<String>(CACHE_KEY))
        data.forEach { (key, value) ->
            assertEquals(value, kache.getFromMap<String>(CACHE_KEY, key))
        }
    }

    @Test
    open fun `Put empty map`() = runBlocking {
        kache.put(CACHE_KEY, emptyMap<Any, Any>())
        assertEquals(emptyMap<Any, Any>(), kache.getMap<Any>(CACHE_KEY))
    }

    @Test
    open fun `Get non existing map`() = runBlocking {
        assertEquals(emptyMap<Any, Any>(), kache.getMap<Any>(randomUUID().toString()))
    }

    @Test
    open fun `Get from not existing map`() = runBlocking {
        assertNull(kache.getFromMap(randomUUID().toString(), randomUUID().toString()))
        assertEquals("kek", kache.getFromMap(randomUUID().toString(), randomUUID().toString()) { "kek" })
    }

    @Test
    open fun `Add and delete items from list`() = runBlocking {
        val key = randomUUID().toString()
        val items = (0L..10L).map { DummyModel(it) }

        kache.addToList(key, *items.subList(0, 6).toTypedArray())
        assertEquals(6, kache.getList<DummyModel>(key).size)

        kache.addToList(key, *items.subList(3, 9).toTypedArray())
        assertEquals(12, kache.getList<DummyModel>(key).size)

        kache.deleteFromList(key, *items.subList(5, 8).toTypedArray())
        assertEquals(8, kache.getList<DummyModel>(key).size)
    }

    @Test
    open fun `Put and delete items from map`() = runBlocking {
        val key = randomUUID().toString()
        val items = (0L..10L).map { it.toString() to DummyModel(it) }.toMap()

        kache.addToMap(key, items.filterKeys { it.toLong() in (0..6) })
        assertEquals(7, kache.getMap<DummyModel>(key).size)

        kache.addToMap(key, items.filterKeys { it.toLong() in (3L..9L) })
        assertEquals(10, kache.getMap<DummyModel>(key).size)

        kache.deleteFromMap(key, items.filterKeys { it.toLong() in (5L..8L) })
        assertEquals(6, kache.getMap<DummyModel>(key).size)
    }

    @Test
    open fun `Delete multiple keys`() = runBlocking {
        val keys = (1L..10L).map { it.toString() }
        keys.forEach { kache.put(it, it) }

        val keysToDelete = (1L..4L).map { it.toString() }
        kache.delete(keysToDelete)

        assertNull(kache.getOne("3"))
        assertNotNull(kache.getOne("6"))
    }

    @Test
    open fun `Test find by glob pattern`() = runBlocking {
        val data = mapOf(
                "kekpek1" to "kekpek1",
                "kekpek2" to "kekpek2",
                "cheburek1" to "cheburek1",
                "cheburek2" to "cheburek2"
        )
        data.forEach { (k, v) -> kache.put(k, v) }

        val all = kache.find<String>("*")
        assertEquals(4, all.size)

        val found = kache.find<String>("*kek*")
        assertEquals(2, found.size)

        val notFound = kache.find<String>("*hehmda")
        assertEquals(0, notFound.size)

        val notFoundButElse = kache.find("*hehmda") { listOf("lol") }
        assertEquals(1, notFoundButElse.size)
    }

    companion object {
        private const val CACHE_KEY = "key"
        private const val CACHE_VALUE = "value"
    }
}