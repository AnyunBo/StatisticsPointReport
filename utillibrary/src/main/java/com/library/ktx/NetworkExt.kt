@file:JvmName("NetworkUtil")

package com.library.ktx

import android.content.Context
import com.library.util.NetWorkUtil

/**
 * 是否正在使用 WIFI 连接
 */
fun Context.isUsingWifiNetwork(): Boolean {
    return NetWorkUtil.getNetWorkState(this) == NetWorkUtil.NETWORK_WIFI
}

/**
 * 判断网络是否可用
 */
fun Context.isNetworkAvailable() = NetWorkUtil.isAvailable(this)