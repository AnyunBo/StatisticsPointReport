@file: JvmName("RxJavaUtil")

package com.library.ktx

import android.content.Context
import com.library.wrapper.ObserverSafeAdapter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * timer操作符实现一个延时操作，类似Handler postDelay
 */
fun Context.postDelay(delay: Long, unit: TimeUnit, run: (Any) -> Unit = {}) {
    Observable.timer(delay, unit)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : ObserverSafeAdapter<Long>(this) {
            override fun onSafeError(t: Throwable) {
                run(0)
            }

            override fun onSafeNext(t: Long) {
                run(t)
            }
        })
}
