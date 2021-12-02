package com.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class PackageUtil {


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getPackagesWithMainIntentFilter(context).contains(packageName);
        } catch (Exception e) {
            e.printStackTrace();
            return getPackagesWithMainIntentFilter(context).contains(packageName);
        }
    }

    /**
     * This is a backup method to get the installed packages on the device. It tries to query the activities those can help the intent with {@LINK Intent#ACTION_MAIN}
     * Note the PackageManager.getInstalledPackages may return null/empty if it's rejected by the users on some Chinese Roms.
     *
     * @param context
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    public static List<String> getPackagesWithMainIntentFilter(@NonNull Context context) {
        try {
            HashSet<String> installedPackages = new HashSet<>();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0);
                if (resolveInfoList != null) {
                    for (ResolveInfo resolveInfo : resolveInfoList) {
                        installedPackages.add(resolveInfo.activityInfo.packageName);
                    }
                }
            }
            return new LinkedList<>(installedPackages);
        } catch (Throwable t) {
            t.printStackTrace();
            return new LinkedList<String>();
        }
    }

}
