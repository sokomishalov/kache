@file:Suppress("unused")

package ru.sokomishalov.kache.serialization

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.sokomishalov.kache.core.Serializer
import kotlin.text.Charsets.UTF_8


/**
 * @author sokomishalov
 */
class GsonSerializer(
        private val gson: Gson = Gson()
) : Serializer {

    override fun <T> serialize(obj: T): ByteArray {
        return gson.toJson(obj).toByteArray(UTF_8)
    }

    override fun <T> deserialize(byteArray: ByteArray, toClass: Class<T>): T {
        return gson.fromJson(byteArray.toString(UTF_8), toClass)
    }

    override fun <T> deserializeList(byteArray: ByteArray, elementClass: Class<T>): List<T> {
        return gson.fromJson<List<T>>(byteArray.toString(UTF_8), TypeToken.getParameterized(List::class.java, elementClass).type)
    }

    override fun <K, V> deserializeMap(byteArray: ByteArray, keyClass: Class<K>, valueClass: Class<V>): Map<K, V> {
        return gson.fromJson<Map<K, V>>(byteArray.toString(UTF_8), TypeToken.getParameterized(Map::class.java, keyClass, valueClass).type)
    }

}