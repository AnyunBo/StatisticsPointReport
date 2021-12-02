package com.library.wrapper

import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 *  CompletableObserver 简单实现，避免直接实现 CompletableObserver 导致的书写麻烦
 */
open class CompletableObserverAdapter : CompletableObserver {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(t: Throwable) {
        t.printStackTrace()
    }

}