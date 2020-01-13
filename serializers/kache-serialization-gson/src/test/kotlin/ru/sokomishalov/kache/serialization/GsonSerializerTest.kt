package ru.sokomishalov.kache.serialization

import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.tck.SerializerTck

/**
 * @author sokomishalov
 */
class GsonSerializerTest : SerializerTck() {
    override val serializer: Serializer by lazy { GsonSerializer() }
}