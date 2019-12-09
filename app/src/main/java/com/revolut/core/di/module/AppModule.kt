package com.revolut.core.di.module


import android.content.Context
import com.google.gson.Gson
import com.revolut.BuildConfig.BASE_URL
import com.revolut.base.domain.executors.PostExecutionThread
import com.revolut.base.domain.executors.ThreadExecutor
import com.revolut.core.application.RevolutApplication
import com.revolut.core.data.interceptor.JsonInterceptor
import com.revolut.core.presentation.executors.JobExecutor
import com.revolut.core.presentation.executors.UIThread
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: RevolutApplication): Context = application

    @Provides
    @Singleton
    internal fun provideUIThread(): PostExecutionThread = UIThread()

    @Provides
    @Singleton
    internal fun provideBackgroundExecutor(): ThreadExecutor = JobExecutor()

    @Provides
    @Singleton
    fun provideGson() = Gson()


    @Provides
    @Singleton
    fun getClient( gson :Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(JsonInterceptor())
            .build()


        return Retrofit.Builder()

            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            /* .addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


}