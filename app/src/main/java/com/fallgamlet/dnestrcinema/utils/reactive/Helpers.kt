package com.fallgamlet.dnestrcinema.utils.reactive

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

fun <T> Single<T>.mapTrue() =
    this.map { true }
        .onErrorReturnItem(false)

fun <T> Observable<T>.mapTrue() =
    this.map { true }
        .onErrorReturnItem(false)


fun <T> Single<T>.emitSubject(subject: Subject<T>) =
    this.doOnSuccess { subject.onNext(it) }

fun <T> Observable<T>.emitSubject(subject: Subject<T>) =
    this.doOnNext { subject.onNext(it) }


fun <T> Single<T>.emitSubjectError(subject: Subject<T>) =
    this.doOnError { subject.onError(it) }

fun <T> Observable<T>.emitSubjectError(subject: Subject<T>) =
    this.doOnError { subject.onError(it) }

fun <T> Observable<T>.onErrorToSubjectNext(subject: Subject<Throwable>) =
    this.doOnError { subject.onNext(it) }


fun <T> Single<T>.subscribeDisposable() =
    this.toObservable()
        .subscribeDisposable()

fun <T> Observable<T>.subscribeDisposable() =
    this.subscribe(ObserverUtils.emptyDisposableObserver())


fun <T> Single<T>.subscribeWithDisposable() =
    this.toObservable()
        .subscribeWithDisposable()

fun <T> Observable<T>.subscribeWithDisposable() =
    this.subscribeWith(ObserverUtils.emptyDisposableObserver())


fun <T> Single<T>.schedulersIoToUi() =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.schedulersIoToUi() =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
