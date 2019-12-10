package com.revolut.features.rates.data.source


import com.revolut.features.rates.data.model.RatesResponse
import com.revolut.features.rates.data.source.api.RatesApiServices
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RatesRemoteDS @Inject constructor(private val ratesApiServices :RatesApiServices) {
    private var currentCurrency: String = ""
    var isErrorHappened :  PublishSubject<Boolean> = PublishSubject.create()


    fun getCurrencies(currency: String): Flowable<RatesResponse> {
        currentCurrency = currency
        return Flowable.timer(1, TimeUnit.SECONDS)
            .repeatUntil { currency != currentCurrency }
            .switchMap {
                ratesApiServices.getCurrencies(currency)}
            .retryUntil {
                isErrorHappened.onNext(true)
                currentCurrency == ""
            }



    }

    fun isError():Flowable<Boolean>{
        return isErrorHappened.toFlowable(BackpressureStrategy.LATEST)
    }



}