package com.serviceindeed.xuebao.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class CommonUtil {

    //获取版本号  
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        }
        catch (NameNotFoundException e) {
            return null;
        }
    }

    //获取版本号(内部识别号)  
    public static Integer getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        }
        catch (NameNotFoundException e) {
            return null;
        }
    }

}
