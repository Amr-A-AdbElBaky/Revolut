package com.revolut.features.rates.domain.entity

data class RatesEntity( val base:String,
                        val rates :MutableList<Rate>)

data class Rate(val name:String  = "EUR", var value :Double = 0.0, var isFocused :Boolean = false)

