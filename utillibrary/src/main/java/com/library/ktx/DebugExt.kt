@file:JvmName("DebugUtil")

package com.library.ktx

import com.library.BuildConfig

/**
 * 只在Debug配置下执行runnable
 */
inline fun debugRun(runnable: () -> Unit) {
    if (BuildConfig.DEBUG) {
        runnable()
    }
}

