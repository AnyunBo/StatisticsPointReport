@file:JvmName("ThreadUtil")

package com.library.ktx

import android.os.Looper

/**
 * 判断当前线程是否为主线程
 */
fun Thread.isMainThread() = Looper.myLooper() == Looper.getMainLooper()

/**
 * 判断当前线程是否为工作者线程(非主线程)
 */
fun Thread.isWorkerThread() = !this.isMainThread()
