package  com.revolut.core.extension

import com.revolut.base.domain.executors.PostExecutionThread
import com.revolut.base.domain.executors.ThreadExecutor
import com.revolut.core.presentation.ObservableResource
import com.revolut.core.presentation.executors.JobExecutor
import com.revolut.core.presentation.executors.UIThread
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


fun <T> Flowable<T>.applyAsyncSchedulers(executionScheduler: Scheduler = Schedulers.io(), postExecutionScheduler: PostExecutionThread = UIThread()): Flowable<T> =
    this.compose {
        it.subscribeOn(executionScheduler).observeOn(postExecutionScheduler.scheduler)
    }


fun <T, R> Flowable<T>.publishToObservableResource(res: ObservableResource<R>,
                                                   doOnBeforeSuccess: ((data: R) -> Unit)? = null,doOnNext: ((R) -> Unit)? = null, doOnComplete: (() -> Unit)? = null, mapper: (T) -> R = noMapper(), executor: ThreadExecutor = JobExecutor(), postExecutor: PostExecutionThread = UIThread()): Disposable {
    res.loading.value = true
    return this.map(mapper).applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
        doOnBeforeSuccess?.invoke(it)
        onSuccess(res, it)
        doOnNext?.invoke(it)
    }, {
        onError(it, res)
    }, {
        doOnComplete?.invoke()
    })
}



fun Disposable?.addTo(composite: CompositeDisposable){
    if (this != null)
        composite.add(this)
}

private fun <R> onSuccess(res: ObservableResource<R>, it: R) {
    res.loading.value = false
    res.value = it
}

private fun <R> onError(err: Throwable, res: ObservableResource<R>, onError: ((data: Throwable) -> Unit)? = null ,doAfterError: ((data: Throwable) -> Unit)? = null) {
    res.loading.value = false

    if (onError != null) {
        onError.invoke(err)
    } else {
        res.error.value = err
        doAfterError?.invoke(err)
    }
}

private fun <T, R> noMapper(): (T) -> R {
    return mapper@{
        try {
            with(it as R) {
                return@mapper this
            }
        } catch (e: Exception) {
            throw ClassCastException("Please provide mapper to publishToObservableResource method or make sure that ObservableResource of the same type as the Source e.g Signle,Observable,Maybe,Flowable")
        }
    }
}
