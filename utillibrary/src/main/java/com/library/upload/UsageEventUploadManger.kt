package com.library.upload

import com.library.ktx.runOnComputationThread
import common.library.app.StatisticApplication

object UsageEventUploadManger {

    private val uploader: UsageEventUploader = StatisticDataUploader(StatisticApplication.getInstance())

    init {
        uploader.init()
    }

    fun collect(data: String?, tag: String?) {
        data?.let(::usageEvent)?.let { usageEvent ->
            runOnComputationThread {
                uploader.collect(usageEvent)
            }
        }
    }

}