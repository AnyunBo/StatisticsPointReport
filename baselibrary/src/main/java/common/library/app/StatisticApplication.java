package common.library.app;

import common.library.BuildConfig;
import common.library.base.BaseApplication;

public class StatisticApplication extends BaseApplication {

    public static String CHANNEL_ID = BuildConfig.isBetaChannel ? "test" : "vivo";    //	渠道号
    //大数据埋点的索引值
    public static int RECORD_INDEX = 0;
    private static StatisticApplication instance;

    public static StatisticApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RECORD_INDEX = 0;
        instance = this;
    }


}
