package common.library.support

import android.annotation.SuppressLint
import android.content.Context
import common.library.app.StatisticApplication


/**
 * 增加一个因获取时序导致  StatisticApplication.getInstance为null的问题的补救方案
 */
//明确这里不会传入组件的上下文示例，进行lint压制警告
@SuppressLint("StaticFieldLeak")
object AppContextHolder {
    private var appBaseContext: Context? = null

    /**
     * 在Application attachBaseContext时调用
     */
    fun updateAppBaseContext(baseContext: Context?) {
        appBaseContext = baseContext
    }

    fun getAppBaseContext(): Context? {
        return appBaseContext
    }

    /**
     * 更加安全的获取应用上下文对象
     */
    fun getAppContext(): Context? {
        return StatisticApplication.getInstance() ?: appBaseContext
    }
}