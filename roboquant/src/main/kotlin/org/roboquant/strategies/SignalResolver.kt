/*
 * Copyright 2020-2024 Neural Layer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package org.roboquant.strategies

import org.roboquant.common.iszero

typealias SignalResolver = List<Signal>.() -> List<Signal>

fun List<Signal>.sumSignals(): List<Signal> {
    val assets = groupBy { it.asset }.mapValues { entry -> Signal(entry.key, entry.value.sumOf { it.rating }) }
    return assets.values.filter { !it.rating.iszero }
}

fun List<Signal>.averageSignals(): List<Signal> {
    val assets =
        groupBy { it.asset }.mapValues { entry -> Signal(entry.key, entry.value.sumOf { it.rating } / entry.value.size) }
    return assets.values.filter { !it.rating.iszero }
}

fun List<Signal>.firstSignals(): List<Signal> {
    return groupBy { it.asset }.mapValues { it.value.first() }.values.toList()
}

fun List<Signal>.lastSignals(): List<Signal> {
    return groupBy { it.asset }.mapValues { it.value.last() }.values.toList()
}
