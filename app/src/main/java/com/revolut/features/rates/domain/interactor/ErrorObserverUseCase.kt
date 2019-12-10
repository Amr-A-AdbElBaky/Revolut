package com.revolut.features.rates.domain.interactor

import com.revolut.base.domain.interactor.FlowableUseCase
import com.revolut.features.rates.domain.repository.RatsRepository
import io.reactivex.Flowable
import javax.inject.Inject


class ErrorObserverUseCase @Inject constructor(private val repository: RatsRepository) : FlowableUseCase<Unit, Boolean>() {
    override fun build(params: Unit ): Flowable<Boolean> {
        return repository.isErrorHappened()
    }
}