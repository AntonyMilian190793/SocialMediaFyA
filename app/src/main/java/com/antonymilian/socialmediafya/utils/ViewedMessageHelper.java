package com.antonymilian.socialmediafya.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;

import java.util.List;

public class ViewedMessageHelper {

    public static void updateOnline(boolean status, final Context context){
        UsersProvider usersProvider = new UsersProvider();
        AuthProvider authProvider = new AuthProvider();
        if(authProvider.getUid() != null){
            if(isApplicationSentToBackground(context)){
                usersProvider.updateOnline(authProvider.getUid(), status);
            }else if(status){
                usersProvider.updateOnline(authProvider.getUid(), status);
            }
        }
    }

    public static boolean isApplicationSentToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
//If your app is the process in foreground, then it's not in running in background
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
