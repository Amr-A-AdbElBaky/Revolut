package  com.revolut.core.extension

import com.revolut.base.domain.executors.PostExecutionThread
import com.revolut.base.domain.executors.ThreadExecutor
import com.revolut.core.presentation.ObservableResource
import com.revolut.core.presentation.SingleLiveEvent
import com.revolut.core.presentation.executors.JobExecutor
import com.revolut.core.presentation.executors.UIThread
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.schedulers.Schedulers


fun <T> Flowable<T>.applyAsyncSchedulers(executionScheduler: Scheduler = Schedulers.io(), postExecutionScheduler: PostExecutionThread = UIThread()): Flowable<T> =
    this.compose {
        it.subscribeOn(executionScheduler).observeOn(postExecutionScheduler.scheduler)
    }


fun <T> Single<T>.applyAsyncSchedulers(executor: ThreadExecutor,
                                       postExecutor: PostExecutionThread
): Single<T> =
    this.compose {
        it.subscribeOn(Schedulers.from(executor)).observeOn(postExecutor.scheduler)
    }

fun <T> Single<T>.applyAsyncSchedulers(executionScheduler: Scheduler = Schedulers.from(JobExecutor()), postExecutionScheduler: PostExecutionThread = UIThread()): Single<T> =
    this.compose {
        it.subscribeOn(executionScheduler).observeOn(postExecutionScheduler.scheduler)
    }

fun <T, R> Flowable<T>.publishToObservableResource(res: ObservableResource<R>, doOnNext: ((R) -> Unit)? = null, doOnComplete: (() -> Unit)? = null, mapper: (T) -> R = noMapper(), executor: ThreadExecutor = JobExecutor(), postExecutor: PostExecutionThread = UIThread()): Disposable {
    res.loading.value = true
    return this.map(mapper).applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
        onSuccess(res, it)
        doOnNext?.invoke(it)
    }, {
        onError(it, res)
    }, {
        doOnComplete?.invoke()
    })
}

fun <T, R> Single<T>.publishToObservableResource(res: ObservableResource<R>,
                                                 doOnBeforeSuccess: ((data: R) -> Unit)? = null,
                                                 doOnError: ((data: Throwable) -> Unit)? = null,
                                                 doAfterError: ((data: Throwable) -> Unit)? = null,
                                                 loading: Boolean = true,
                                                 mapper: (T) -> R = noMapper(),
                                                 executor: ThreadExecutor = JobExecutor(),
                                                 postExecutor: PostExecutionThread = UIThread()): Disposable {
    if (loading) res.loading.value = true

    return this.map(mapper).applyAsyncSchedulers(Schedulers.from(executor), postExecutor)
        .subscribe({
            doOnBeforeSuccess?.invoke(it)
            onSuccess(res, it)

        }, {
            onError(it, res, doOnError ,doAfterError)
        })
}

fun <T, R> Single<List<T>>.appendToObservableResource(res: ObservableResource<List<R>>,
                                                      mapper: (T) -> R = noMapper(),
                                                      executor: ThreadExecutor = JobExecutor(),
                                                      postExecutor: PostExecutionThread = UIThread(),
                                                      doOnSuccess: ((data: List<R>) -> Unit)? = null,
                                                      doOnError: ((data: Throwable) -> Unit)? = null): Disposable {
    if (res.loading.value != true){
        res.loading.value = true
        return this.flattenAsFlowable { it }.map(mapper).toList()
            .applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
                appendOnSuccess(res, it)
                doOnSuccess?.invoke(it)
            }, {
                onError(it, res)
            })
    }else
       return DisposableHelper.DISPOSED
}



fun <T, R> Single<T>.publishToSingleLive(resSuccess: SingleLiveEvent<R>,
                                         resError: SingleLiveEvent<Throwable>? = null,
                                         onSuccess: ((data: R) -> Unit)? = null,
                                         mapper: (T) -> R = noMapper(),
                                         executor: ThreadExecutor = JobExecutor(),
                                         postExecutor: PostExecutionThread = UIThread()): Disposable {
    return this.map(mapper).applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
        resSuccess.value = it
        onSuccess?.invoke(it)
    }, {
        resError?.value = it
    })
}


fun <T, R> Single<List<T>>.publishListToObservableResource(res: ObservableResource<List<R>>,
                                                           onSuccess: ((data: List<R>) -> Unit)? = null,
                                                           onError: ((data: Throwable) -> Unit)? = null,
                                                           mapper: (T) -> R = noMapper(),
                                                           executor: ThreadExecutor = JobExecutor(),
                                                           postExecutor: PostExecutionThread = UIThread()): Disposable {
    res.loading.value = true
    return this.flattenAsFlowable { it }.map(mapper).toList()
        .applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
            onSuccess(res, it)
            onSuccess?.invoke(it)
        }, {
            onError(it, res, onError)
        })
}

fun <T, R> Flowable<List<T>>.publishListToObservableResource(res: ObservableResource<List<R>>,
                                                             mapper: (T) -> R = noMapper(),
                                                             executor: ThreadExecutor = JobExecutor(),
                                                             postExecutor: PostExecutionThread = UIThread()): Disposable {
    res.loading.value = true
    return this.flatMapIterable { ls -> ls }.map(mapper).toList()
        .applyAsyncSchedulers(Schedulers.from(executor), postExecutor).subscribe({
            onSuccess(res, it)
        }, {
            onError(it, res)
        })
}


fun Disposable?.addTo(composite: CompositeDisposable): Unit {
    if (this != null)
        composite.add(this)
}

inline fun <T> T.toSingle():Single<T> = Single.just(this)

private fun <R> onSuccess(res: ObservableResource<R>, it: R) {
    res.loading.value = false
    res.value = it
}

private fun <R> appendOnSuccess(res: ObservableResource<List<R>>, it: List<R>) {
    res.loading.value = false
    if (res.value == null)
        res.value = it
    else {
        val newList = mutableListOf<R>()
        newList.addAll(res.value!!)
        newList.addAll(it)
        res.value = newList
    }
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
