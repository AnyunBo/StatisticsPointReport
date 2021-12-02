@file:JvmName("BooleanUtil")
package com.library.ktx

/**
 * 仅在Boolean?实例为true的时候执行block
 */
inline fun Boolean?.runWhenTrue(block: () -> Unit) {
    if (this == true) {
        block()
    }
}

