package com.fallgamlet.dnestrcinema.utils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * Created by fallgamlet on 02.10.17.
 */
public class ObserverUtils {

    public static <T> Observable<T> wrapSafeDisposable(Observable<T> observable) {
        return Observable.create(emitter -> {
            observable.subscribe(
                    value -> {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(value);
                        }
                    },
                    throwable -> {
                        if (!emitter.isDisposed()) {
                            emitter.tryOnError(throwable);
                        }
                    },
                    () -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();

                        }
                    }
            );
        });
    }

    public static <T> Flowable<T> wrapSafeDisposable(Flowable<T> flowable) {
        return flowable;
    }


    public static <T> EmptyObserver<T> emptyObserver() {
        return new EmptyObserver<>();
    }

    public static <T> EmptyDisposableSubscriber<T> emptyDisposableSubscriber() {
        return new EmptyDisposableSubscriber<>();
    }

    public static <T> EmptyDisposableObserver<T> emptyDisposableObserver() {
        return new EmptyDisposableObserver<>();
    }


    public static class EmptyObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {

        }
    }

    public static class EmptyDisposableSubscriber<T> extends DisposableSubscriber<T> {

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    }

    public static class EmptyDisposableObserver<T> extends DisposableObserver<T> {

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

}
