package com.library.wrapper

import android.app.Activity
import com.library.ktx.asType
import com.library.ktx.safeRun
import com.library.ktx.wasActivityAlreadyDestroyed

import io.reactivex.rxjava3.disposables.Disposable
import java.lang.IllegalStateException

/**
 * observer safeRun Activity or Fragment
 */
abstract class ObserverSafeAdapter<T>(private val likelyContext: Any? = null) : ObserverAdapter<T>() {
    override fun onError(e: Throwable) {
        e.printStackTrace()
        safeRun {
            onSafeError(e)
        }
    }

    override fun onComplete() {}

    override fun onSubscribe(disposable: Disposable) {
        if(likelyContext?.asType<Activity>()?.wasActivityAlreadyDestroyed() == true){
            disposable.dispose()
        }
    }

    override fun onNext(t: T) {
        val safeRan = safeRun {
            onSafeNext(t)
        }

        if (!safeRan) {
            onError(IllegalStateException())
        }
    }

    open fun onSafeError(t: Throwable){
        t.printStackTrace()
    }
    abstract fun onSafeNext(t: T)

}
