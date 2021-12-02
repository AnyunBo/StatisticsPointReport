package com.library.io

import android.os.Handler
import android.os.HandlerThread
import com.library.ktx.*
import com.library.wrapper.randomIntWithin

import io.reactivex.rxjava3.functions.Consumer
import java.io.File

/**
 * 异步数据化持久类
 * 线程策略
 *   1、  非UI线程，避免阻塞UI线程
 *   2、  指定工作者线程读和写，避免并发问题
 * @property  persistFile 数据持久化目标文件(不建议使用外置存储)
 * @constructor
 */
class AsyncDataPersistence(private val persistFile: File) {

    /**
     * 便于将读写操作执行在指定的线程中
     */
    private var handler: Handler? = null

    /**
     * 所有数据持久化(读和写)都需要在该线程中执行
     */
    private var handlerThread: HandlerThread? = null

    private val handlerThreadName by lazy {
        "AsyncDataPersist-${randomIntWithin(10)}"
    }

    init {
        ensureHandlerThread()
    }

    /**
     * 执行异步线程
     *
     */
    private fun ensureHandlerThread() {
        if (handlerThread == null) {
            initHandlerThread()
        }
    }

    /**
     * 初始化异步线程
     *
     */
    private fun initHandlerThread() {
        val thread = HandlerThread(handlerThreadName)
        thread.start()
        handlerThread = thread
        handler = Handler(thread.looper)
    }

    /**
     * 持久化数据
     *   1、 数据为null，不执行持久化
     *   2、 handler未初始化，不执行持久化
     *
     *  consumer只在debug模式下数据返回
     */
    fun persist(data: String?, consumer: Consumer<String?>?) {
        checkPersistCondition(data).runWhenTrue {
            handler?.safePost {
                data.appendToFile(persistFile.path)
                debugRun {
                    consumer?.accept(persistFile.readContent())
                }
            }
        }
    }

    private fun checkPersistCondition(data: String?): Boolean {
        if (data.isNullOrEmpty()) {
            return false
        }
        ensureHandlerThread()
        return true
    }

    private fun checkObtainDataCondition(consumer: Consumer<String>?): Boolean {
        if (consumer == null) {
            return false
        }
        ensureHandlerThread()
        return true
    }

    /**
     * 获取持久化的数据,consumer回调中使用
     * 不回调的情况
     *   1、 handler为初始化
     *   2、 读取出现异常
     */
    fun obtainAndEmptyData(consumer: Consumer<String>?) {
        checkObtainDataCondition(consumer).runWhenTrue {
            handler?.safePost {
                persistFile.readAndEmpty()?.let {
                    consumer?.accept(it)
                }
            }
        }
    }

    fun obtainData(consumer: Consumer<String>?) {
        checkObtainDataCondition(consumer).runWhenTrue {
            handler?.safePost {
                persistFile.readContent()?.let {
                    consumer?.accept(it)
                }
            }
        }
    }

}