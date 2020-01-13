package ru.sokomishalov.kache.provider

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.serialization.JacksonSerializer
import ru.sokomishalov.kache.tck.KacheTck

/**
 * @author sokomishalov
 */

class MongoKacheTest : KacheTck() {

    companion object {
        @get:ClassRule
        val mongo: MongoTestContainer = createDefaultMongoContainer()

        @AfterClass
        @JvmStatic
        fun stop() = mongo.stop()
    }

    override val kache: Kache by lazy {
        mongo.start()
        MongoKache(serializer = JacksonSerializer(), client = mongo.createReactiveMongoClient())
    }
}