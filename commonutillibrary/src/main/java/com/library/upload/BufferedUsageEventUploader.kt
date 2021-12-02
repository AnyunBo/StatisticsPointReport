package com.library.upload

import android.content.Context
import android.text.format.DateUtils
import com.library.bean.UsageEvent
import com.library.wrapper.ObserverAdapter
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * 数据上报管理抽象类
 * 该功能包含了缓冲设置，缓冲时间由debounceTimeoutInMills决定
 */
abstract class BufferedUsageEventUploader(val context: Context, debounceTimeoutInMills: Long) : UsageEventUploader {

    private val serializedSubject = PublishSubject.create<UsageEvent>().toSerialized()
    private val debounce = serializedSubject.debounce(debounceTimeoutInMills, TimeUnit.MILLISECONDS)

    init {
        initBufferedSubject()
    }

    /**
     * 分发上传数据
     */
    abstract fun dispatchUpload(usageEvents: List<UsageEvent>)

    /**
     * 初始化serializedSubject
     */
    private fun initBufferedSubject() {
        serializedSubject.buffer(debounce).subscribe(object : ObserverAdapter<List<UsageEvent>>() {
            override fun onNext(t: List<UsageEvent>) {
                super.onNext(t)
                dispatchUpload(t)
            }
        })
    }

    /**
     * 数据上报管理类，收集数据的方法
     */
    override fun collect(value: UsageEvent?) {

        value?.let(serializedSubject::onNext)
    }

    companion object {
        const val DEFAULT_DEBOUNCED_TIMEOUT_IN_MILLIS = 2 * DateUtils.SECOND_IN_MILLIS
    }
}