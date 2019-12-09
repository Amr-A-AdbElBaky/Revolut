package com.revolut.features.rates.data.source


import com.revolut.features.rates.data.model.RatesResponse
import com.revolut.features.rates.data.source.api.RatesApiServices
import io.reactivex.Single
import javax.inject.Inject


class RatesRemoteDS @Inject constructor(private val ratesApiServices :RatesApiServices) {

    fun getCurrencies(currency: String): Single<RatesResponse> {
        return ratesApiServices.getCurrencies(currency)
    }





}