package com.revolut.features.rates.presentation.viewmodel


import com.revolut.base.domain.executors.ThreadExecutor
import com.revolut.base.presentation.viewmodel.BaseViewModel
import com.revolut.core.extension.addTo
import com.revolut.core.extension.publishToObservableResource
import com.revolut.core.presentation.ObservableResource
import com.revolut.core.presentation.executors.UIThread
import com.revolut.features.rates.domain.entity.Rate
import com.revolut.features.rates.domain.entity.RatesEntity
import com.revolut.features.rates.domain.interactor.GetCurrenciesUseCase
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val uiThread: UIThread,
    private val threadExecutor: ThreadExecutor
) : BaseViewModel() {

    val ratesObservableResource: ObservableResource<RatesEntity> = ObservableResource()
    var currentCurrency: Rate? = null


    fun getCurrencies(rate :Rate = Rate()) {
        currentCurrency = rate
        getCurrenciesUseCase.build(rate.name).publishToObservableResource(
            ratesObservableResource,
            doOnBeforeSuccess = ::setCurrentCurrency,
            executor = threadExecutor, postExecutor = uiThread
        )
            .addTo(compositeDisposable)
    }

    private fun setCurrentCurrency(ratesEntity: RatesEntity) {
        currentCurrency?.let {
            val rateValue: Double = if (it.value == 0.0)
                1.0
            else
                it.value

            currentCurrency = Rate(ratesEntity.base, rateValue)
        } ?: run {
            currentCurrency = Rate(ratesEntity.base, 1.0)
        }


    }
}



