/*
 * Copyright 2020-2023 Neural Layer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.roboquant.brokers.sim

import org.roboquant.brokers.sim.execution.InternalAccount
import org.roboquant.common.sumOf

/**
 * AccountModel that models a plain cash account. No additional leverage or margin is available for trading.
 * This is the default AccountModel if none is specified during instantiation of a SimBroker
 *
 * You should not short positions when using the CashAccount since that is almost never allowed in the real world
 * and also not supported. If you do anyway, the short exposures are for the full 100% deducted from the buying power.
 * So the used calculation is:
 *
 *      buying power = cash - short exposure - minimum
 *
 * Note: currently open orders are not taken into consideration when calculating the total buying power
 *
 * @property minimum the minimum amount of cash balance required to maintain in the account, defaults to 0.0. It is
 * denoted in the base currency of the account (like also the buyingPower is)
 */
class CashAccount(private val minimum: Double = 0.0) : AccountModel {

    /**
     * @see [AccountModel.updateAccount]
     */
    override fun updateAccount(account: InternalAccount) {
        val shortExposure = account.portfolio.values.filter { it.short }.sumOf { it.exposure }
        val cash = account.cash - shortExposure

        val buyingPower = cash.convert(account.baseCurrency, account.lastUpdate) - minimum
        account.buyingPower = buyingPower
    }

}