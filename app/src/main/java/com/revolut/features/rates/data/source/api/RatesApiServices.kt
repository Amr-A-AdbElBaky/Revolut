package com.revolut.features.rates.data.source.api

import com.revolut.features.rates.data.model.RatesResponse
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApiServices {


    @GET("latest")
    fun getCurrencies(@Query("base") currency: String):Flowable<RatesResponse>
}