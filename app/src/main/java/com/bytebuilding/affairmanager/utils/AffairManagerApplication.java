package com.bytebuilding.affairmanager.utils;

import android.app.Application;
import android.content.Intent;

import com.bytebuilding.affairmanager.activities.LoginActivity;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.facebook.FacebookSdk;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AffairManagerApplication extends Application {

    private static boolean isVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        VKSdk.initialize(this);
        vkAccessTokenTracker.startTracking();

        FacebookSdk.sdkInitialize(this);

        Realm.init(this.getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .encryptionKey(CryptoUtils.KEY64.getBytes())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        SplashScreen.preferences = getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE);
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

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
}
