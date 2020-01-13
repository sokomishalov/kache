package ru.sokomishalov.kache.provider

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.serialization.JacksonSerializer
import ru.sokomishalov.kache.tck.KacheTck

/**
 * @author sokomishalov
 */
class RedisKacheTest : KacheTck() {

    companion object {
        @get:ClassRule
        val redis: RedisTestContainer = createDefaultRedisContainer()

        @AfterClass
        @JvmStatic
        fun stop() = redis.stop()
    }

    override val kache: Kache by lazy {
        redis.start()
        RedisKache(serializer = JacksonSerializer(), client = redis.createRedisClient())
    }
}