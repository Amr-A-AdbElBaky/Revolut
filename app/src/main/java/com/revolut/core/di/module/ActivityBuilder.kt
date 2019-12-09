package com.revolut.core.di.module

import com.revolut.features.rates.di.RatesDataModule
import com.revolut.features.rates.di.RatesPresentationModule
import com.revolut.features.rates.di.RatesScope
import com.revolut.features.rates.presentation.view.activity.RateActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {


    @RatesScope
    @ContributesAndroidInjector(modules = [RatesDataModule::class , RatesPresentationModule::class])
    internal abstract fun provideRatesActivity(): RateActivity
}