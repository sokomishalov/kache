package ru.sokomishalov.kache.provider

import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.serialization.JacksonSerializer
import ru.sokomishalov.kache.tck.KacheTck

/**
 * @author sokomishalov
 */
class ConcurrentMapKacheTest : KacheTck() {
    override val kache: Kache by lazy { ConcurrentMapKache(serializer = JacksonSerializer()) }
}