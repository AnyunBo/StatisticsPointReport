package com.library.ktx

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.view.View
import com.library.wrapper.getValueSafely
import io.reactivex.rxjava3.core.Observable

fun View?.makeVisible() {
    this?.visibility = View.VISIBLE
}

fun View?.makeInvisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.makeGone() {
    this?.visibility = View.GONE
}

fun View?.makeTransparent() {
    this?.setBackgroundColor(Color.TRANSPARENT)
}

fun Uri?.isHttpLink(): Boolean {
    return if (this == null) {
        false
    } else {
        scheme?.startsWith("http") == true
    }
}

fun Context.startActivitySafely(action: String, dataUriString: String): Boolean {
    return try {
        Intent(action).apply {
            data = Uri.parse(dataUriString)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            startActivity(it)
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.startActivitySafely(intent: Intent): Boolean {
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

private fun Context.getLocationManager(): LocationManager? {
    return (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
}

fun Context.isGPSProviderEnabled(): Boolean {
    return getValueSafely {
        getLocationManager()?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
    } ?: false
}

fun Context.isNetworkProviderEnabled(): Boolean {
    return getValueSafely {
        getLocationManager()?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
    } ?: false
}

fun <T> nullOptionObservable(): Observable<OptionalValue<T>> {
    return Observable.just(nullBackedOptionalValue())
}

fun <T> nullBackedOptionalValue(): OptionalValue<T> = OptionalValue(null)

/**
 * 安全执行block，捕获其中可能发生的异常
 * 如果没有异常，返回true，否则返回false
 */
inline fun safeRun(block: () -> Unit): Boolean {
    var safeRan = false
    try {
        block()
        safeRan = true
    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return safeRan
}

/**
 * 安全执行某个操作，可以接收返回值
 */
@JvmOverloads
inline fun <T> safeRun(fallback: T? = null, block: () -> T?): T? {
    return try {
        block()
    } catch (t: Throwable) {
        t.printStackTrace()
        fallback
    }
}
