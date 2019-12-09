package com.revolut.core.application

import androidx.appcompat.app.AppCompatDelegate
import com.revolut.core.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class RevolutApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }


    override fun applicationInjector(): AndroidInjector<RevolutApplication> =
        DaggerAppComponent.builder().create(this)

    companion object {
        lateinit var instance: RevolutApplication
            private set
    }

}
