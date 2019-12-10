package com.revolut.features.rates.domain.repository

import com.revolut.features.rates.domain.entity.RatesEntity
import io.reactivex.Flowable


interface RatsRepository {

    fun getCurrencies(currency: String): Flowable<RatesEntity>
}