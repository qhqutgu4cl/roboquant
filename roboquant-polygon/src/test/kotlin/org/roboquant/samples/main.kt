/*
 * Copyright 2020-2023 Neural Layer
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

package org.roboquant.samples

import kotlinx.coroutines.runBlocking
import org.roboquant.common.Timeframe
import org.roboquant.common.minutes
import org.roboquant.feeds.PriceAction
import org.roboquant.feeds.filter
import org.roboquant.polygon.PolygonLiveFeed


private fun testLiveFeed() = runBlocking {
    val feed = PolygonLiveFeed {
        this.delayed = true
    }
    feed.subscribe("IBM", "AAPL")
    val actions = feed.filter<PriceAction>(timeframe = Timeframe.next(5.minutes)) {
        println(it)
        true
    }
    assert(actions.isNotEmpty())
    feed.disconnect()
}

fun main() {
    System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")
    testLiveFeed()
}
