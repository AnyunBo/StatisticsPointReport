package com.library.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class NetWorkUtil {

    /***  无网*/
    public static final int NETWORK_TYPE_NOT_AVAILABLE = 0;
    /***  WiFi*/
    public static final int NETWORK_TYPE_WIFI = 1;
    /***  其他*/
    public static final int NETWORK_TYPE_OTHERS = 2;
    /***  2G*/
    public static final int NETWORK_TYPE_2G = 3;
    public static final String NETWORK_STR_TYPE_2G = "2G";
    /***  3G*/
    public static final int NETWORK_TYPE_3G = 4;
    public static final String NETWORK_STR_TYPE_3G = "3G";
    /***  4G*/
    public static final int NETWORK_TYPE_4G = 5;
    public static final String NETWORK_STR_TYPE_4G = "4G";
    /***  5G*/
    public static final int NETWORK_TYPE_5G = 6;
    public static final String NETWORK_STR_TYPE_5G = "5G";
    /*** 未知网络*/
    public static final String NETWORK_STR_TYPE_UN_KNOW = "unknown";
    /*** 没有连接网络*/
    public static final int NETWORK_NONE = -1;
    /*** 移动网络*/
    public static final int NETWORK_MOBILE = 0;
    /*** 无线网络*/
    public static final int NETWORK_WIFI = 1;


    public static int getNetWorkState(Context context) {
        if (context == null) {
            return NETWORK_NONE;
        }
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    /**
     * 判断网络是否可用
     * <p>需添加权限 android.permission.ACCESS_NETWORK_STATE</p>
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }

    /**
     * 获取活动网路信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        } else {
            try {
                return cm.getActiveNetworkInfo();
            } catch (SecurityException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 是否是漫游
     *
     * @param context
     * @return true 漫游
     */
    public static boolean isNetworkRoaming(@NonNull Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (networkInfo == null) {
            return false;
        } else {
            return networkInfo.isRoaming();
        }
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        final boolean isNetworkOK = isAvailable(context);
        if (isNetworkOK) {
            final NetworkInfo networkInfo = getActiveNetworkInfo(context);
            if (networkInfo == null) {
                return NETWORK_TYPE_OTHERS;
            } else {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_TYPE_WIFI;
                } else {
                    final String mobileNetworkType = getMobileNetworkType(context);
                    if (NETWORK_STR_TYPE_5G.equals(mobileNetworkType)) {
                        return NETWORK_TYPE_5G;
                    } else if (NETWORK_STR_TYPE_4G.equals(mobileNetworkType)) {
                        return NETWORK_TYPE_4G;
                    } else if (NETWORK_STR_TYPE_3G.equals(mobileNetworkType)) {
                        return NETWORK_TYPE_3G;
                    } else if (NETWORK_STR_TYPE_2G.equals(mobileNetworkType)) {
                        return NETWORK_TYPE_2G;
                    } else {
                        return NETWORK_TYPE_OTHERS;
                    }
                }
            }
        } else {
            return NETWORK_TYPE_NOT_AVAILABLE;
        }
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    @NonNull
    public static String getMobileNetworkType(Context context) {
        if (context == null) {
            return NETWORK_STR_TYPE_UN_KNOW;
        }
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                int networkType = mTelephonyManager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NETWORK_STR_TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NETWORK_STR_TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NETWORK_STR_TYPE_4G;
                    case TelephonyManager.NETWORK_TYPE_NR:
                        return NETWORK_STR_TYPE_5G;
                    default:
                        return NETWORK_STR_TYPE_UN_KNOW;
                }
            } else {
                return NETWORK_STR_TYPE_UN_KNOW;
            }
        } else {
            return NETWORK_STR_TYPE_UN_KNOW;
        }
    }

}