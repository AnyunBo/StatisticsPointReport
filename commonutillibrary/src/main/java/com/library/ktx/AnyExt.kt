package com.library.ktx

/**
 * 将一种类型转换为另一种类型,如果类型转换不允许，返回null
 */
inline fun <reified T> Any.asType(): T? {
    return if (this is T) {
        this
    } else {
        null
    }
}

fun <T> T?.nonNull(value: T): T {
    return this ?: value
}

/**
 * 过滤T，如果T符合filter过滤条件，则返回T，否则返回null
 */
inline fun <T> T.match(filter: (T) -> Boolean): T? {
    return if (filter.invoke(this)) {
        this
    } else {
        null
    }
}

/**
 * 当condition为true的情况下，应用effectOn，否则不应用
 */
inline fun <T : Any> T.applyWhen(condition: Boolean, effectOn: (T) -> T): T {
    return if (condition) {
        effectOn(this)
    } else {
        this
    }
}

/**
 * 当condition条件满足，执行block()方法，否则不执行任何操作
 */
inline fun <T : Any> T.runWhen(condition: Boolean, block: (T) -> Unit) {
    runWhen(condition, block, {})
}

/**
 * 当condition条件满足，则执行hitBlock，否则执行missedBlock
 */
inline fun <T : Any> T.runWhen(
    condition: Boolean,
    hitBlock: (T) -> Unit,
    missedBlock: (T) -> Unit
) {
    if (condition) {
        hitBlock(this)
    } else {
        missedBlock(this)
    }
}

/**
 * 将 T? 实例转换成 R 实例
 * 执行表现：
 *   1、 如果T？实例为null，直接返回fallbackValue
 *   2、 如果T? 实例不为null，则执行nonNullValueTransform得到处理结果，如果处理结果为null，返回fallbackValue,否则返回该处理结果
 */
inline fun <T, R> T?.transform(fallbackValue: R, nonNullValueTransform: (T) -> R?): R {
    return if (this == null) {
        fallbackValue
    } else {
        nonNullValueTransform(this) ?: fallbackValue
    }
}
