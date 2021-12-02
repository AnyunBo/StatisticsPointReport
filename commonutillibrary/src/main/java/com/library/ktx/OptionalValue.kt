package com.library.ktx

open class OptionalValue<M>(val value : M?)

/**
 * 快捷方式，生成OptionalValue
 */
fun <M> optionalValueOf(value: M?): OptionalValue<M?> {
    return OptionalValue(value)
}