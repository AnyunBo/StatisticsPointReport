@file:JvmName("StringExtension")

package com.library.util

import android.text.TextUtils

fun String.notEmpty(valueIfEmpty: String): String {
    return if (this.isEmpty()) {
        valueIfEmpty
    } else {
        this
    }
}

fun String?.isNotNullNorEmpty(): Boolean {
    return !TextUtils.isEmpty(this)
}

fun String?.brToNewLine(): String? {
    return this?.replace("<br>", "\n")?.replace("<br/>", "\n")
}