package com.library.wrapper

import com.library.ktx.postDelay
import common.library.app.StatisticApplication
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun randomIntWithin(maxBoundary: Int): Int {
    return Random.nextInt(maxBoundary)
}

/**
 * 判断first和second均为非null时执行consumer调用，避免变量导致过多?.let的麻烦
 */
inline fun <F, S> nonNull(first: F?, second: S?, consumer: (F, S) -> Unit) {
    nonNull(first, second, Unit) { firstValue, secondValue, _ ->
        consumer.invoke(firstValue, secondValue)
    }
}

/**
 *  * 判断first,second,third 均为非null时执行consumer调用，避免变量导致过多?.let的麻烦
 */
inline fun <F, S, T> nonNull(first: F?, second: S?, third: T?, consumer: (F, S, T) -> Unit) {
    if (first != null && second != null && third != null) {
        consumer.invoke(first, second, third)
    }
}

/**
 * 安全的获取值的信息，其过程中发生异常会自动处理，返回null
 */
inline fun <T> getValueSafely(getValueAction: () -> T?): T? {
    return try {
        getValueAction()
    } catch (t: Throwable) {
        null
    }
}

fun runOnUIThread(action: () -> Unit) {
    StatisticApplication.getInstance().postDelay(0, TimeUnit.MILLISECONDS) {
        action()
    }
}