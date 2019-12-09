package com.revolut.features.rates.domain.interactor

import com.revolut.base.domain.interactor.SingleUseCase
import com.revolut.features.rates.domain.entity.RatesEntity
import com.revolut.features.rates.domain.repository.RatsRepository
import io.reactivex.Single
import javax.inject.Inject


class GetCurrenciesUseCase @Inject constructor(private val repository: RatsRepository) : SingleUseCase<String, RatesEntity>() {
    override fun build(params: String): Single<RatesEntity> {
        return repository.getCurrencies(params)
    }
}