@file:JvmName("FileUtil")

package com.library.ktx

import java.io.File

/**
 * 将字符串内容写到文件（会冲掉之前的数据）
 * filePath不正确返回false
 * 字符串为nul，返回false
 */
fun String?.writeToFile(filePath: String?): Boolean {
    return this?.ioWork(filePath) { _filePath, _data ->
        _filePath.toFile().writeText(_data)
        true
    } ?: false
}

/**
 * 字符串执行IO操作，通常为写入文件
 */
inline fun String.ioWork(
    filePath: String?,
    ioWork: (filePath: String, data: String) -> Boolean
): Boolean {
    if (filePath.isNullOrEmpty()) {
        return false
    }

    //detect thread policy
    assertWorkerThread("String.ioWork")

    return safeRun(false) {
        ioWork(filePath, this)
    } ?: false
}

/**
 * 将字符串追加到文件中（不冲掉之前的内容）
 */
fun String?.appendToFile(filePath: String?): Boolean {
    return this?.ioWork(filePath) { _filePath, _data ->
        _filePath.toFile().appendText(_data)
        true
    } ?: false
}

/**
 * 支持链式转化为File
 */
private fun String.toFile(): File {
    return File(this)
}

/**
 * 支持String?转成File
 */
fun String?.toFileSafely(): File? {
    return this?.toFile()
}

/**
 * 封装的读取文件内容，处理了线程策略和异常问题
 */
fun File.readContent(): String? {
    assertWorkerThread("readContent")
    return safeRun(null) {
        readText()
    }
}

/**
 * 清空文件内容
 */
fun File.emptyContent(): Boolean {
    assertWorkerThread("readContent")
    return safeRun(false) {
        writeText("")
        true
    } ?: false
}

/**
 * 读取文件内容并清空
 */
fun File.readAndEmpty(): String? {
    assertWorkerThread("readContent")
    val text = readContent()
    emptyContent()
    return text
}