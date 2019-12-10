package com.revolut.features.rates.data.repository

import com.revolut.features.rates.data.model.mapper.mapToEntity
import com.revolut.features.rates.data.source.RatesRemoteDS
import com.revolut.features.rates.domain.entity.RatesEntity
import com.revolut.features.rates.domain.repository.RatsRepository
import io.reactivex.Flowable
import javax.inject.Inject


class RatesRepositoryImpl @Inject constructor(private val ratesRemoteDS: RatesRemoteDS) :
    RatsRepository {
    override fun isErrorHappened(): Flowable<Boolean> {
        return ratesRemoteDS.isError()
    }

    override fun getCurrencies(currency: String): Flowable<RatesEntity> {
        return ratesRemoteDS.getCurrencies(currency).map { it.mapToEntity() }
    }
}



