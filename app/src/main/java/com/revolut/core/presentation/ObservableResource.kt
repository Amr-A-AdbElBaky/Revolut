package com.revolut.core.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class ObservableResource<T> : SingleLiveEvent<T>() {
    val error: SingleLiveEvent<Throwable> = SingleLiveEvent()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    fun observe(
        owner: LifecycleOwner,
        successObserver: Observer<in T>,
        loadingObserver: Observer<Boolean>? = null

    ) {
        super.observe(owner, successObserver)
        loadingObserver?.let { loading.observe(owner, it) }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        super.removeObservers(owner)
        loading.removeObservers(owner)
        error.removeObservers(owner)
    }

    fun observe(
        owner: LifecycleOwner,
        doOnSuccess: (T) -> Unit,
        doOnLoading: (() -> Unit)? = null,
        doOnError: (Throwable) -> Unit = { },
        doOnTerminate: (() -> Unit)? = null
    ) {
        super.observe(owner, Observer {
            it?.let {
                doOnSuccess.invoke(it)
            } /*?: doOnComplete?.invoke()*/
        })

        loading.observe(owner, Observer { isLoading ->
            if (isLoading)
                doOnLoading?.invoke()
            else
                doOnTerminate?.invoke()
        })
        error.observe(owner , Observer {
            doOnError.invoke(it)
        })
    }

}