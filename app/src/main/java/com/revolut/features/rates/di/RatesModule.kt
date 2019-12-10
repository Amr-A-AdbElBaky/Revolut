package com.revolut.features.rates.di

import androidx.lifecycle.ViewModel
import com.revolut.core.di.ViewModelKey
import com.revolut.features.rates.data.repository.RatesRepositoryImpl
import com.revolut.features.rates.data.source.RatesRemoteDS
import com.revolut.features.rates.data.source.api.RatesApiServices
import com.revolut.features.rates.domain.repository.RatsRepository
import com.revolut.features.rates.presentation.viewmodel.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit


@Module
class RatesDataModule{

    @RatesScope
    @Provides
    fun provideRatesApiServices(retrofit: Retrofit): RatesApiServices
            = retrofit.create(RatesApiServices::class.java)

    @RatesScope
    @Provides
    fun provideRatesRepoImpl(ratesRemoteDS: RatesRemoteDS)
            : RatsRepository = RatesRepositoryImpl(ratesRemoteDS)
}

@Module
abstract class RatesPresentationModule {
    @Binds
    @RatesScope
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    internal abstract fun ratesViewModel(viewModel: RatesViewModel): ViewModel
}

