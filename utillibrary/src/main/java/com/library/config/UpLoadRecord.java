package com.library.config;

import android.text.TextUtils;

import com.library.upload.UsageEventUploadManger;

import common.library.app.StatisticApplication;

import java.util.Map;
import java.util.Set;

public class UpLoadRecord extends BaseRecord {

    @Override
    public void submit() {
        String buriedPointName = "";
        StringBuilder buffer = new StringBuilder();
        Map<String, String> countMap = getParams();
        if (countMap != null && countMap.size() > 0) {
            //添加计数的索引值
            countMap.put(StatisticConfig.KEY_IDX, String.valueOf(++StatisticApplication.RECORD_INDEX));
            Set<String> key = countMap.keySet();
            for (String temp : key) {
                if (TextUtils.equals(StatisticConfig.BURIED_POINT_NAME, temp)) {
                    //去除自定的埋点名称
                    buriedPointName = countMap.get(temp);
                    continue;
                }
                String mapValue = countMap.get(temp);
                if (!TextUtils.isEmpty(mapValue) && mapValue != null) {
                    if (mapValue.contains(StatisticConfig.STR_AND_CONNECTION)) {
                        mapValue = mapValue.replaceAll(StatisticConfig.STR_AND_CONNECTION, StatisticConfig.STR_MONEY_CONNECTION);
                    }
                    buffer.append(temp).append(StatisticConfig.STR_EQUAL_CONNECTION).append(mapValue).append(StatisticConfig.STR_AND_CONNECTION);
                } else {
                    buffer.append(temp).append(StatisticConfig.STR_EQUAL_CONNECTION).append(StatisticConfig.STR_AND_CONNECTION);
                }
            }
        }
        String usageEventString = buffer.toString();
        if (!TextUtils.isEmpty(usageEventString)) {
            UsageEventUploadManger.INSTANCE.collect(usageEventString, buriedPointName);
        }
    }

}
