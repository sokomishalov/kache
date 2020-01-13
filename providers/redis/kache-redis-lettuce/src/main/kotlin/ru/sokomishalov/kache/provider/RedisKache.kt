package ru.sokomishalov.kache.provider

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.ScanArgs
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.reactive.awaitFirstOrNull
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.util.unit

/**
 * @author sokomishalov
 */

class RedisKache(
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

    // override some methods for better performance
}