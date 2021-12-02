package common.library.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.multidex.MultiDexApplication;

import common.library.support.AppContextHolder;

public class BaseApplication extends MultiDexApplication {

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppContextHolder.INSTANCE.updateAppBaseContext(base);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 重写 getResource 方法，防止系统字体影响
     * 禁止app字体大小跟随系统字体大小调节
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            Configuration configuration = resources.getConfiguration();
            configuration.setToDefaults();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }
}
