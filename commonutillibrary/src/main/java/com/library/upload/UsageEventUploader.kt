package com.library.upload

import com.library.bean.UsageEvent

/**
 * 用户使用事件上报接口，
 */
interface UsageEventUploader {

    /**
     * 用来收集上报事件的方法
     */
    fun collect(value: UsageEvent?)

    /**
     * 做一些前置的操作
     */
    fun init()
}