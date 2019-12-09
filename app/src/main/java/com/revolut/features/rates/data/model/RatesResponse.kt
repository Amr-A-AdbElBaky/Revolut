package com.revolut.features.rates.data.model

import java.util.*
import kotlin.collections.HashMap


data class RatesResponse(
   val base:String,
   val date:String,
   val rates :HashMap<String, Double>
)





