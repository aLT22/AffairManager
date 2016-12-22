package com.bytebuilding.affairmanager.utils;

import android.app.Application;

/**
 * Created by turki on 08.12.2016.
 */

public class AffairManagerApplication extends Application {

    private static boolean isVisible;

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
