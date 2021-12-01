package com.library.config;

/**
 * 埋点上报字段
 */
public class StatisticConfig {
    /*** 大数据埋点当条的名称，方便测试，上报的时候移除*/
    public final static String BURIED_POINT_NAME = "buriedPointName";
    /**** 请求成功*/
    public final static int CODE_OK = 0;
    /*** &符号*/
    public final static String STR_AND_CONNECTION = "&";
    /*** =等号*/
    public final static String STR_EQUAL_CONNECTION = "=";
    /*** $等号*/
    public final static String STR_MONEY_CONNECTION = "\\$";
    /*** 设置统计的LogApi*/
    public static String UPLOAD_USER_LOG_API = "http://datacollect.secoo.com/c/dcs.gif";      // 正式环境
    /***1:pv*/
    public static String OT_PAGE_VIEW = "1";
    /***2:点击*/
    public static String OT_VIEW_CLICK = "2";
    /***3:滑动*/
    public static String OT_SCROLL = "3";
    /*** 4：曝光 */
    public static String OT_VIEW_SHOWN = "4";
    /*** 5.启动*/
    public static String OT_APP_START = "5";
    /*** 5.启动*/
    public static final String KEY_IDX = "s.aidx";
    /*** 1:pv，2:点击，3:滑动*/
    public static final String KEY_OT = "s.ot";
    /**
     * 上次所在页面插码ID<br>
     * 如果是访问的第一个页面或发生在第一个页面上的事件操作，则last_page_id=page_id=op_id,否则为上一个页面的op_id
     */
    public static final String KEY_LAST_PAID = "s.lpaid";
    /*** 页面来源*/
    public static final String KEY_OS = "s.os";
    /*** tag*/
    public static final String KEY_TAG = "tag";
    /*** pv中进行操作id描述*/
    public static final String KEY_ID = "s.id";
    /*** 设备uuid*/
    public static final String KEY_UUID = "s.uuid";
    /*** 设备imei*/
    public static final String KEY_IMEI = "s.imei";
    /*** uid*/
    public static final String KEY_UID = "s.uid";
    /*** 渠道id*/
    public static final String KEY_CHANNEL_ID = "s.cid";
    /*** 页面id*/
    public static final String KEY_PAID = "s.paid";
    /*** 请求id*/
    public static final String KEY_RID = "s.rid";
    /*** 模块id*/
    public static final String KEY_MODE_ID = "module_id";
    /*** spm*/
    public static final String KEY_SPM = "spm";
    /*** 元素位置*/
    public static final String KEY_ELEMENT_POSITION = "element_position";
    /*** 元素内容*/
    public static final String KEY_ELEMENT_CONTENT = "element_content";
    /*** url*/
    public static final String KEY_URL = "url";


}
