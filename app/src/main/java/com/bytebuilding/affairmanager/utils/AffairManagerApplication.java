package com.bytebuilding.affairmanager.utils;

import android.app.Application;

import com.bytebuilding.affairmanager.activities.SplashScreen;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AffairManagerApplication extends Application {

    private static boolean isVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this.getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .encryptionKey(CryptoUtils.KEY64.getBytes())
                .build();
        Realm.setDefaultConfiguration(configuration);

        SplashScreen.preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);
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
