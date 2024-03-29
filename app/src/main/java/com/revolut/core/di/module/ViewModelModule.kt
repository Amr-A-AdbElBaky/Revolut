package com.revolut.core.di.module

import androidx.lifecycle.ViewModelProvider
import com.revolut.core.di.module.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
