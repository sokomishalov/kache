/**
 * Copyright 2019-2020 the original author or authors.
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
package ru.sokomishalov.kache.core

import ru.sokomishalov.kache.core.model.GlobString


/**
 * @author sokomishalov
 */
suspend inline fun <reified T> Kache.getOne(key: String, orElse: () -> T? = { null }): T? = getOne(key, T::class.java) ?: orElse()

suspend inline fun <reified T> Kache.getList(key: String, ifEmpty: () -> List<T> = { emptyList() }): List<T> = getList(key, T::class.java).ifEmpty { ifEmpty() }

suspend inline fun <reified T> Kache.getMap(key: String, ifEmpty: () -> Map<String, T> = { emptyMap() }): Map<String, T> = getMap(key, T::class.java).ifEmpty { ifEmpty() }

suspend inline fun <reified T> Kache.getFromMap(key: String, mapKey: String, orElse: () -> T? = { null }): T? = getFromMap(key, mapKey, T::class.java) ?: orElse()

suspend inline fun <reified T : Any> Kache.find(glob: GlobString, ifEmpty: () -> List<T> = { emptyList() }): List<T> = find(glob, T::class.java).ifEmpty { ifEmpty() }

suspend inline fun <reified T> Kache.addToList(key: String, vararg values: T): List<T> = addToList(key, T::class.java, *values)

suspend inline fun <reified T> Kache.addToMap(key: String, additionalMap: Map<String, T>): Map<String, T> = addToMap(key, T::class.java, additionalMap)

suspend inline fun <reified T> Kache.deleteFromList(key: String, vararg values: T): List<T> = deleteFromList(key, T::class.java, *values)

suspend inline fun <reified T> Kache.deleteFromMap(key: String, removalMap: Map<String, T>): Map<String, T> = deleteFromMap(key, T::class.java, removalMap)