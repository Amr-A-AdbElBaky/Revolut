package com.revolut.features.rates.domain.repository

import com.revolut.features.rates.domain.entity.RatesEntity
import io.reactivex.Single


interface RatsRepository {

    fun getCurrencies(currency: String): Single<RatesEntity>
}