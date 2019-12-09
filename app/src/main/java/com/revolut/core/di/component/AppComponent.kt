package com.revolut.core.di.component

import com.revolut.core.application.RevolutApplication
import com.revolut.core.di.module.ActivityBuilder
import com.revolut.core.di.module.AppModule
import com.revolut.core.di.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ViewModelModule::class,ActivityBuilder::class])
interface AppComponent : AndroidInjector<RevolutApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<RevolutApplication>()

}
