package com.movementinsome.caice.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by zzc on 2017/8/15.
 */

public class ApkUpdateUtil {

    public static String getVersion(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    public static String getApkName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.packageName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "版本号未知";
        }
    }

}
