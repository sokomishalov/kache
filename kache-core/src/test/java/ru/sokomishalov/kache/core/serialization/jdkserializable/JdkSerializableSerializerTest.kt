package ru.sokomishalov.kache.core.serialization.jdkserializable

import ru.sokomishalov.kache.core.Serializer
import ru.sokomishalov.kache.core.serialization.SerializerTck

class JdkSerializableSerializerTest : SerializerTck() {
    override val serializer: Serializer = JdkSerializableSerializer()
}