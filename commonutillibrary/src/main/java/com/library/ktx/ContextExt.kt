@file:JvmName("ContextUtil")

package com.library.ktx

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.library.util.PackageUtil
import com.library.wrapper.AppStateHelper
import common.library.app.StatisticApplication
import common.library.support.AppContextHolder

val appContext: Context
    get() {
        return AppContextHolder.getAppContext() ?: StatisticApplication.getInstance()
    }

fun Context.isAppInstalled(packageName: String) = PackageUtil.isAppInstalled(this, packageName)

/**
 * 快捷方式获取AppState
 */
@Suppress("unused")
val Context.appState: AppStateHelper.Message
    get() {
        return AppStateHelper.getInstance().state
    }

@Suppress("NOTHING_TO_INLINE", "unused")
inline fun Context.isAppForeground(): Boolean {
    return AppStateHelper.getInstance().isForeground
}

@Suppress("NOTHING_TO_INLINE")
inline fun Context.isAppBackground(): Boolean {
    return !isAppForeground()
}

/**
 * 启动设置界面的应用详情页
 */
fun Context.startAppDetailPage(): Boolean {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.fromParts("package", packageName, null)
    return safeRun {
        startActivity(intent)
    }
}

fun Context.color(colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}