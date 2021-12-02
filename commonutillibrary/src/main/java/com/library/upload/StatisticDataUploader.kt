package com.library.upload


import android.content.Context
import androidx.annotation.Keep
import com.library.bean.NetworkStateChangedEvent
import com.library.bean.UsageEvent
import com.library.io.AsyncDataPersistence
import com.library.ktx.*
import com.library.util.NetWorkUtil
import com.library.util.isNotNullNorEmpty
import com.library.wrapper.AppStateHelper
import io.reactivex.rxjava3.functions.Consumer
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.IOException


/**
 * 上传到大数据的上报类，使用缓冲接近实时上报
 */
class StatisticDataUploader(
    context: Context,
    debounceTimeoutInMills: Long = DEFAULT_DEBOUNCED_TIMEOUT_IN_MILLIS
) : BufferedUsageEventUploader(context, debounceTimeoutInMills) {

    companion object {
        const val USAGE_EVENT_REPORT_URL = "https://datacollect.secoo.com/c/dcs.gif"

        /**
         * 大数据限定换行符
         */
        const val LINE_SEPARATOR = "\n"

        /**
         * 单次上报最大的埋点条数
         */
        const val MAX_USAGE_EVENT_COUNT_PER_REQUEST = 15

        /**
         * SharedPrefKey:单个网络请求上报的最大事件数量的键
         */
        const val PREF_KEY_USAGE_EVENT_COUNT_PER_REQUEST = "pref.key.usage_event_count_per_request"
    }

    /**** 收否是第一次上报 默认false*/
    private var isFirstUsageEventReported: Boolean = false

    /**
     * 单个网络请求上报的最大事件数量
     */
    private val maxUsageEventCountPerRequest by lazy {
        context.defaultPref()
            .getInt(PREF_KEY_USAGE_EVENT_COUNT_PER_REQUEST, MAX_USAGE_EVENT_COUNT_PER_REQUEST)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    private val persistence by lazy {
        AsyncDataPersistence(File(context.cacheDir, "statistic_upload_persist_log"))
    }

    private fun collectFirstUsageEvent(usageEvent: UsageEvent?) {
        usageEvent?.let {
            dispatchUpload(listOf(usageEvent))
        }
    }

    override fun collect(value: UsageEvent?) {
        //因大数据激活需要，第一条数据要求立即上报
        if (!isFirstUsageEventReported) {
            isFirstUsageEventReported = true
            collectFirstUsageEvent(value)
        } else {
            super.collect(value)
        }
    }

    /**
     * 读取之前上传失败的数据
     */
    override fun init() {
        EventBus.getDefault().register(this)
        resendFailedData()
    }

    /**
     * 发送之前失败的数据
     */
    private fun resendFailedData() {
        if (context.isNetworkAvailable()) {
            persistence.obtainAndEmptyData(Consumer<String> {
                uploadFailedData(it)
            })
        } else {
            //  resendFailedData network not available;skip
        }
    }

    private fun uploadFailedData(failedData: String) {
        runOnComputationThread {
            val failedUsageEvents = failedDataToUsageEvents(failedData)
            dispatchUpload(failedUsageEvents)
        }
    }

    /**
     * 将失败的字符串数据转成UsageEvent List
     */
    private fun failedDataToUsageEvents(failedData: String): List<UsageEvent> {
        return failedData.split(LINE_SEPARATOR)
            .filter { it.isNotNullNorEmpty() }
            .map(::usageEvent)
    }


    /**
     * 处理上传失败的数据，进行持久化
     */
    private fun onUploadFailure(
        failedData: String,
        usageEvents: List<UsageEvent>,
        reason: String?
    ) {
        debugRun {
            persistence.obtainData(Consumer {
                val usageEvents = failedDataToUsageEvents(it ?: "")
                //  "onUploadFailure previous persistence",
                //                    "usageEvents", usageEvents.map { event -> event.debugDistinctId },
                //                    "usageEvents.size", usageEvents.size
            })
        }


        persistence.persist(failedData + LINE_SEPARATOR, Consumer {
        })
    }

    /**
     * upload之前的检查，数据和网络情况
     */
    private fun checkUpload(uploadData: String, usageEvents: List<UsageEvent>): Boolean {
        if (uploadData.isEmpty()) {
            return false
        }
        if (!NetWorkUtil.isAvailable(context)) {
            onUploadFailure(uploadData, usageEvents, "bad_network")
            return false
        }

        return true
    }

    /**
     * 真实的上传数据处理方法
     *   * 失败后会加入持久化存储
     */
    private fun upload(usageEvents: List<UsageEvent>) {
        val uploadData = composeUploadData(usageEvents)
        checkUpload(uploadData, usageEvents).runWhenTrue {
            val uploadRequest = composeRequest(uploadData)
            okHttpClient.newCall(uploadRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    onUploadFailure(uploadData, usageEvents, e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {

                    } else {
                        onUploadFailure(
                            uploadData,
                            usageEvents,
                            "code=${response.code};message=${response.message}"
                        )
                    }
                    response.closeSafely()
                }
            })
        }
    }

    override fun dispatchUpload(usageEvents: List<UsageEvent>) {
        usageEvents.runWhen(usageEvents.isNotEmpty()) {
            /**
             * 上报一次上报过多数据，将数据进行切分。切分后，数据上报条数<= MAX_USAGE_EVENT_COUNT_PER_REQUEST
             */
            usageEvents.chunked(maxUsageEventCountPerRequest)
                .forEach(this@StatisticDataUploader::upload)
        }
    }

    /**
     * 生成请求对象
     */
    private fun composeRequest(body: String): Request {
        val mediaType = "application/octet-stream".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, body)
        return Request.Builder()
            .url(USAGE_EVENT_REPORT_URL)
            .post(requestBody)
            .build()
    }


    /**
     * 将UsageEvent转成数据，每个item之间增加换行符
     */
    private fun composeUploadData(usageEvents: List<UsageEvent>): String {
        return usageEvents.mapNotNull { it.data }.joinToString(LINE_SEPARATOR)
    }

    @Keep
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChanged(event: NetworkStateChangedEvent) {
        resendFailedData()
    }

    @Keep
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAppStateChanged(message: AppStateHelper.Message) {
        if (context.isAppForeground()) {
            resendFailedData()
        }
    }


}