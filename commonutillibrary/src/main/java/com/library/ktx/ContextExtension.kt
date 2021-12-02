package com.library.ktx

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager

fun Context.defaultPref() = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.savePrefValue(key: String, value: String): Boolean {
    return true.also {
        defaultPref().edit().putString(key, value).apply()
    }
}

fun Context.readPrefValue(key: String): String {
    return defaultPref().getString(key, "").toString()
}

/**
 * 将Context尝试转换为Activity，如果失败，返回null
 */
fun Context.asActivity(): Activity? {
    return asType<Activity>()
}

fun Context.saveBooleanValue(key: String, value: Boolean): Boolean {
    return true.also {
        defaultPref().edit().putBoolean(key, value).apply()
    }
}

fun Context.readBooleanValue(key: String): Boolean {
    return defaultPref().getBoolean(key, false)
}