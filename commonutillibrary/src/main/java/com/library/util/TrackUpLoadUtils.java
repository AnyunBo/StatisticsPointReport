package com.library.util;

import android.content.Context;

import common.library.app.StatisticApplication;
import com.library.config.UpLoadRecord;

/**
 * 数据上报工具类
 */
public class TrackUpLoadUtils {

    private static volatile TrackUpLoadUtils trackUpLoadUtils;

    private TrackUpLoadUtils() {
    }

    public TrackUpLoadUtils getInstance() {
        if (trackUpLoadUtils == null) {
            synchronized (TrackUpLoadUtils.class) {
                if (trackUpLoadUtils == null) {
                    trackUpLoadUtils = new TrackUpLoadUtils();
                }
            }
        }
        return trackUpLoadUtils;
    }

    public UpLoadRecord init(Context context) {
        UpLoadRecord record = new UpLoadRecord();
        setUp(context, record);
        return record;
    }

    /**
     * 初始化，统计中必须添加的字段这个一般别动
     *
     * @param context
     */

    private void setUp(Context context, UpLoadRecord record) {
        try {
            record.setUuid("")
                    .setImei("")
                    .setChannelId(StatisticApplication.CHANNEL_ID)
                    .setUid("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
