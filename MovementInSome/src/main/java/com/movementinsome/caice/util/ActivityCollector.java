package com.movementinsome.caice.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 2017/12/20.
 */

public class ActivityCollector {
    private static List<Activity> activityList=new ArrayList<>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void removeAllActivity(){
        for (Activity activity:activityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
