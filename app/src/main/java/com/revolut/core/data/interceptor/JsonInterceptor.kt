package com.revolut.core.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class JsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var initialRequest = chain.request()
        initialRequest = initialRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(initialRequest)
    }
}