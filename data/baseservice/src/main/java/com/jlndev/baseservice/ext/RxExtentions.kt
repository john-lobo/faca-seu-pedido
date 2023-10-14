package com.jlndev.baseservice.ext

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

fun <T> Single<T>.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this.subscribe({
        // EMPTY
    }, {
        // EMPTY
    }))
}

fun Completable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this.subscribe({
        // EMPTY
    }, {
        // EMPTY
    }))
}

fun <T> Single<T>.processSingle(scheduler: BaseSchedulerProvider): Single<T> {
    return this
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
}

fun Completable.processCompletable(scheduler: BaseSchedulerProvider): Completable {
    return this
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
}

