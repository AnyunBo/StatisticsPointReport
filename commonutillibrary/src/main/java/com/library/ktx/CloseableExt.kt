package com.library.ktx

import java.io.Closeable
import java.io.IOException

/**
 * 关闭Closeable对象（InputStream等），包含异常处理
 */
fun Closeable?.closeSafely() {
    try {
        this?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}