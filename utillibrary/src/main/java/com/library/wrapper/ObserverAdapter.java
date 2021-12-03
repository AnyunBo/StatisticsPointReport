package com.library.wrapper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ObserverAdapter<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(@NotNull Disposable disposable) {
    }

    @Override
    public void onNext(T t) {
    }

}
