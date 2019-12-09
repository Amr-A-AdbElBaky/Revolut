package com.revolut.features.rates.data.repository

import com.revolut.features.rates.data.model.mapper.mapToEntity
import com.revolut.features.rates.data.source.RatesRemoteDS
import com.revolut.features.rates.domain.entity.RatesEntity
import com.revolut.features.rates.domain.repository.RatsRepository

import io.reactivex.Single
import javax.inject.Inject


class RatesRepositoryImpl @Inject constructor(private val ratesRemoteDS: RatesRemoteDS) : RatsRepository {

    override fun getCurrencies(currency: String): Single<RatesEntity> {
        return ratesRemoteDS.getCurrencies(currency ).map { it.mapToEntity() }
    }
}