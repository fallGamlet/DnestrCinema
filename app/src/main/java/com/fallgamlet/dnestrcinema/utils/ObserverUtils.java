package com.fallgamlet.dnestrcinema.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;


public class ObserverUtils {

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
