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

package org.roboquant.brokers.sim

import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.common.bips
import org.roboquant.feeds.PriceBar
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PricingEngineTest {

    private val priceBar = PriceBar(Asset("TEST"), 100.0, 101.0, 99.0, 100.5, 1000)

    @Test
    fun noCostTest() {
        val pe = NoCostPricingEngine()
        val pricing = pe.getPricing(priceBar, Instant.now())
        val price = pricing.marketPrice(Size(100))
        assertEquals(priceBar.getPrice(), price)
    }

    @Test
    fun spreadPricing() {
        // Pricing engine with 200 BIPS (2%) spread
        val pe = SpreadPricingEngine(200.bips, "OPEN")
        val pricing = pe.getPricing(priceBar, Instant.now())
        val price = pricing.marketPrice(Size(100))
        assertEquals(priceBar.getPrice("OPEN") * 1.01, price)

        val price2 = pricing.marketPrice(Size(-100))
        assertEquals(priceBar.getPrice("OPEN") * 0.99, price2)
    }
}
