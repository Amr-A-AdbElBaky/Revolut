package com.revolut.features.rates.data.repository

import com.revolut.features.rates.data.model.mapper.mapToEntity
import com.revolut.features.rates.data.source.RatesRemoteDS
import com.revolut.features.rates.domain.entity.RatesEntity
import com.revolut.features.rates.domain.repository.RatsRepository
import io.reactivex.Flowable
import io.reactivex.Observable

import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RatesRepositoryImpl @Inject constructor(private val ratesRemoteDS: RatesRemoteDS) :
    RatsRepository {
    private var currentCurrency: String = ""
    override fun getCurrencies(currency: String): Flowable<RatesEntity> {
        currentCurrency = currency
        return Flowable.timer(10, TimeUnit.SECONDS).repeatUntil { currency != currentCurrency }
            .switchMap {
                ratesRemoteDS.getCurrencies(currency).map { it.mapToEntity() }
            }


    }
}



