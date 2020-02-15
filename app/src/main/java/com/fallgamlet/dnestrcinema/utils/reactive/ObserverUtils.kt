package com.fallgamlet.dnestrcinema.utils.reactive

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber


object ObserverUtils {


    fun <T> emptyDisposableObserver(): EmptyDisposableObserver<T> {
        return EmptyDisposableObserver()
    }


    class EmptyObserver<T> : Observer<T> {

        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(t: T) {

        }

        override fun onError(e: Throwable) {}

        override fun onComplete() {

        }
    }

    class EmptyDisposableSubscriber<T> : DisposableSubscriber<T>() {

        override fun onNext(t: T) {

        }

        override fun onError(t: Throwable) {

        }

        override fun onComplete() {

        }
    }

    class EmptyDisposableObserver<T> : DisposableObserver<T>() {

        override fun onNext(t: T) {

        }

        override fun onError(e: Throwable) {

        }

        override fun onComplete() {

        }
    }

}
