package com.revolut.features.rates.data.model.mapper

import com.revolut.features.rates.data.model.RatesResponse
import com.revolut.features.rates.domain.entity.Rate
import com.revolut.features.rates.domain.entity.RatesEntity


fun RatesResponse.mapToEntity() = RatesEntity(base, rates.getRatesList())

    private fun HashMap<String, Double>.getRatesList () :MutableList<Rate>{
        val ratesList :MutableList<Rate> = mutableListOf<Rate>()
        this.map {
            ratesList.add(Rate(it.key , it.value))
        }
        return ratesList
    }

