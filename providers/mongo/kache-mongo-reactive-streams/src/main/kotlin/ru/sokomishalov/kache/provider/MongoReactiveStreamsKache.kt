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
package ru.sokomishalov.kache.provider

/**
 * @author sokomishalov
 */

import com.mongodb.ConnectionString
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.Updates.set
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.Document
import org.bson.conversions.Bson
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.model.GlobString
import ru.sokomishalov.kache.core.model.globToRegex
import kotlin.text.Charsets.UTF_8

/**
 * @author sokomishalov
 */
class MongoReactiveStreamsKache(
        override val serializer: Serializer,
        private val host: String? = "localhost",
        private val port: Int? = 27017,
        private val databaseName: String = "database",
        private val collectionName: String = "cache",
        private val client: MongoClient = MongoClients.create(ConnectionString("mongodb://${host}:${port}/${databaseName}"))
) : Kache {

    companion object {
        private const val ID_FIELD = "_id"
        private const val RAW_VALUE_FIELD = "rawValue"
    }

    override suspend fun getRaw(key: String): ByteArray? {
        return (getCollection()
                .find(key.buildKeyBson())
                .awaitFirstOrNull()
                ?.get(RAW_VALUE_FIELD) as String?)
                ?.toByteArray(UTF_8)
    }

    override suspend fun putRaw(key: String, value: ByteArray) {
        getCollection()
                .findOneAndUpdate(key.buildKeyBson(), value.buildValueBson(), FindOneAndUpdateOptions().upsert(true))
                .awaitFirstOrNull()
    }

    override suspend fun delete(key: String) {
        getCollection()
                .deleteOne(key.buildKeyBson())
                .awaitFirstOrNull()
    }

    override suspend fun findKeys(glob: GlobString): List<String> {
        return getCollection()
                .find(eq(ID_FIELD, glob.globToRegex().toPattern()))
                .asFlow()
                .toList()
                .mapNotNull { it?.get(ID_FIELD) as String? }
    }

    override suspend fun deleteAll() {
        getCollection().drop().awaitFirstOrNull()
    }

    private fun String.buildKeyBson(): Bson = eq(ID_FIELD, this)
    private fun ByteArray.buildValueBson(): Bson = set(RAW_VALUE_FIELD, this.toString(UTF_8))

    private fun getCollection(): MongoCollection<Document> = client.getDatabase(databaseName).getCollection(collectionName)
}