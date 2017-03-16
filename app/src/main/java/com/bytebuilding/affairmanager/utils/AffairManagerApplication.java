package com.bytebuilding.affairmanager.utils;

import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

/**
 * Created by turki on 08.12.2016.
 */

public class AffairManagerApplication extends MultiDexApplication {

    SharedPreferences preferences = null;

    private static boolean isVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);
    }

    public static boolean isVisible() {
        return isVisible;
    }

    public static void activityResumed() {
        isVisible = true;
    }

    public static void activityPaused() {
        isVisible = false;
    }
}
