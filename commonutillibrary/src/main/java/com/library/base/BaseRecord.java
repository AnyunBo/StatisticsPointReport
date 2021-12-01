package com.library.base;

import com.library.config.StatisticConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseRecord {

    private Map<String, String> params;

    public BaseRecord() {
        params = new LinkedHashMap<>();
    }

    public Map<String, String> getParams() {
        return params;
    }

    public BaseRecord setParams(Map<String, String> prams) {
        params.putAll(prams);
        return this;
    }

    protected BaseRecord setUuid(String uuid) {
        params.put(StatisticConfig.KEY_UUID, uuid);
        return this;
    }

    protected BaseRecord setImei(String imei) {
        params.put(StatisticConfig.KEY_IMEI, imei);
        return this;
    }

    protected BaseRecord setUid(String uid) {
        params.put(StatisticConfig.KEY_UID, uid);
        return this;
    }

    public BaseRecord setChannelId(String channelId) {
        params.put(StatisticConfig.KEY_CHANNEL_ID, channelId);
        return this;
    }

    public BaseRecord setOt(String ot) {
        params.put(StatisticConfig.KEY_OT, ot);
        return this;
    }

    /**
     * view 点击
     */
    public BaseRecord asViewClick() {
        params.put(StatisticConfig.KEY_OT, StatisticConfig.OT_VIEW_CLICK);
        return this;
    }

    /**
     * view曝光
     */
    public BaseRecord asViewShown() {
        params.put(StatisticConfig.KEY_OT, StatisticConfig.OT_VIEW_SHOWN);
        return this;
    }


    /**
     * 页面PV曝光
     */
    public BaseRecord asPageView() {
        params.put(StatisticConfig.KEY_OT, StatisticConfig.OT_PAGE_VIEW);
        return this;
    }

    /**
     * view滑动
     */
    public BaseRecord asViewScroll() {
        params.put(StatisticConfig.KEY_OT, StatisticConfig.OT_SCROLL);
        return this;
    }

    /**
     * app启动
     */
    public BaseRecord asAppStart() {
        params.put(StatisticConfig.KEY_OT, StatisticConfig.OT_APP_START);
        return this;
    }

    public BaseRecord setTag(String tag) {
        params.put(StatisticConfig.KEY_TAG, tag);
        return this;
    }

    public BaseRecord setElementContent(String elementContent) {
        params.put(StatisticConfig.KEY_ELEMENT_CONTENT, elementContent);
        return this;
    }

    public BaseRecord setModeId(String modeId) {
        params.put(StatisticConfig.KEY_MODE_ID, modeId);
        return this;
    }

    public BaseRecord setPaid(String aPaid) {
        params.put(StatisticConfig.KEY_PAID, aPaid);
        return this;
    }

    public BaseRecord setLastPaid(String lastPaid) {
        params.put(StatisticConfig.KEY_LAST_PAID, lastPaid);
        return this;
    }

    public BaseRecord setUrl(String url) {
        params.put(StatisticConfig.KEY_URL, url);
        return this;
    }

    public BaseRecord setOperationSource(String aOs) {
        params.put(StatisticConfig.KEY_OS, aOs);
        return this;
    }

    public BaseRecord setRid(String aRid) {
        params.put(StatisticConfig.KEY_RID, aRid);
        return this;
    }

    public BaseRecord setSpmWithoutTime(String spmWithoutTime) {
        params.put(StatisticConfig.KEY_SPM, spmWithoutTime + "," + System.currentTimeMillis());
        return this;
    }

    public BaseRecord setElement_Position(String element_position) {
        params.put(StatisticConfig.KEY_ELEMENT_POSITION, element_position);
        return this;
    }

    public BaseRecord setDebugTag(String buriedPointName) {
        params.put(StatisticConfig.BURIED_POINT_NAME, buriedPointName);
        return this;
    }

    public abstract void submit();

}
