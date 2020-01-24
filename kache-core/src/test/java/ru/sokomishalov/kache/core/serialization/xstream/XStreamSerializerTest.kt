package ru.sokomishalov.kache.core.serialization.xstream

import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.serialization.SerializerTck

class XStreamSerializerTest : SerializerTck() {
    override val serializer: Serializer = XStreamSerializer()
}