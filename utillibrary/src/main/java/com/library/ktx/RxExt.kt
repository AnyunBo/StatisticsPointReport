@file:JvmName("RxUtil")

package com.library.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.library.wrapper.CompletableObserverAdapter
import com.library.wrapper.ObserverAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers


fun bitmapObservableFromRes(
    context: Context,
    @DrawableRes fallbackDrawableId: Int
): Observable<Bitmap> {
    return Observable.just(fallbackDrawableId).map {
        BitmapFactory.decodeResource(context.resources, fallbackDrawableId)
    }.subscribeOn(Schedulers.io())
}

/**
 * 快捷方法，避免重复的Rx样板代码，在IO线程中执行mapper，然后将结果返回并在主线程中执行consumer方法
 */
fun <T> mapOnIOThreadConsumeOnMainThread(mapper: (String) -> T?, consumer: (T?) -> Unit) {
    Observable.just("").map(mapper)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : ObserverAdapter<T?>() {
            override fun onError(e: Throwable) {
                super.onError(e)
                consumer.invoke(null)
            }

            override fun onNext(t: T?) {
                super.onNext(t)
                consumer.invoke(t)
            }
        })
}

/**
 * 订阅到计算线程中,用于计算耗时；
 * 注意：本地IO和网络不要使用这个方法
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Single<T>.subscribeOnComputation(): Single<T> {
    return this.subscribeOn(Schedulers.computation())
}

/**
 * 在主线程中观察
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Single<T>.observeOnMainThread(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}


fun <T> Observable<T>.subscribeOnIOThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

/**
 * 快捷方式，返回一个RxJava的异常处理Consumer
 */
fun errorConsumer(): Consumer<Throwable> {
    return Consumer {
        it?.printStackTrace()
    }
}

/**
 * 执行一个计算密集的操作
 */
@SuppressLint("CheckResult")
inline fun runOnComputationThread(crossinline block: () -> Unit) {
    Completable.fromAction {
        block()
    }.subscribeOn(Schedulers.computation()).subscribe(CompletableObserverAdapter())
}

/**
 * 执行在子线程的操作
 */
@SuppressLint("CheckResult")
inline fun runOnIOThread(crossinline block: () -> Unit) {
    Completable.fromAction {
        block()
    }.subscribeOn(Schedulers.io()).subscribe(CompletableObserverAdapter())
}
