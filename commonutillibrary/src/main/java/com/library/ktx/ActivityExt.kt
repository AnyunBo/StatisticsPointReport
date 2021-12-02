@file:JvmName("ActivityUtil")

package com.library.ktx

import android.app.Activity
import android.content.Context
import android.os.Build

/**
 * 判断Activity是否已经销毁，尤其是用来提前判断Glide图片加载
 */
fun Activity.wasAlreadyDestroyed(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed
}

/**
 * 判断Activity是否没有销毁
 */
fun Activity.wasNotDestroyed() = !wasAlreadyDestroyed()

/**
 * 判断context(可能是Activity) 是否已经被销毁掉了
 *   1、 如果是Activity，判断真实的销毁情况
 *   2、 如果context是null，也认定其销毁掉，返回true
 *   3、 如果不是context，返回false,不代表任何一个activity销毁掉
 */
@Suppress("RedundantIf")
fun Context?.wasActivityAlreadyDestroyed(): Boolean {
    return when {
        this is Activity -> wasAlreadyDestroyed()
        this == null -> true
        else -> false
    }
}