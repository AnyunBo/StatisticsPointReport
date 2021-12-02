@file:JvmName("UsageEventUtil")

package com.library.upload

import com.library.bean.UsageEvent
import com.library.BuildConfig
import com.library.util.isNotNullNorEmpty

/**
 * 使用事件是否合法
 */
fun UsageEvent.isValid(): Boolean {
    return data.isNotNullNorEmpty()
}

/**
 * UsageEvent调试唯一id,辅助上传数据验证
 */
val UsageEvent.debugDistinctId: Int?
    get() {
        return if (BuildConfig.DEBUG) {
            data?.hashCode()
        } else {
            null
        }
    }

/**
 * 快速创建UsageEvent实例
 */
@JvmName("create")
fun usageEvent(data: String?): UsageEvent {
    return UsageEvent(data)
}