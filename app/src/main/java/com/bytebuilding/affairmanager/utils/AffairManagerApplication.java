package com.bytebuilding.affairmanager.utils;

import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AffairManagerApplication extends MultiDexApplication {

    SharedPreferences preferences = null;

    private static boolean isVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("AffairManager.realm")
                .encryptionKey(key)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);

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
