package com.bytebuilding.affairmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.bytebuilding.affairmanager.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Turkin A. on 16.05.17.
 */

public class Advertisement {

    public static final String INTERSTITIAL_TRIGGER = "INTERSTITIAL_TRIGGER";

    public static final int INTERSTITIAL_TRIGGER_VALUE = 1;

    public static final String ADMOB_PREFERENCES = "ADMOB_PREFERENCES";

    public static void saveTriggerValueInPreferences(Context context, String key, String param, int value) {
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        preferences.edit().putInt(param, value).apply();
    }

    public static int loadTriggerValueFromPreferences(Context context, String key, String param) {
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        if (key.equals(ADMOB_PREFERENCES)) {
            return preferences.getInt(param, 1);
        } else {
            return preferences.getInt(param, 1000);
        }
    }

    public static void showBannerEnterActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.enterActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingEnterActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingEnterActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.activity_enter);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void showBannerLoginActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.loginActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingLoginActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingLoginActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.login_container);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void showBannerMainOnlineActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.mainOnlineActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingMainOnlineActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingMainOnlineActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.fragment_container);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void showBannerMainOfflineActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.mainOfflineActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingMainOfflineActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingMainOfflineActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.activity_main);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void showBannerSignUpActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.signUpActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingSignUpActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingSignUpActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.signUp_container);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void showBannerDetailActivity(final Activity activity) {
        final AdView banner = (AdView) activity.findViewById(R.id.detailActivity_banner);

        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                setUpContentViewPaddingDetailActivity(activity, banner.getHeight());
            }
        });
    }

    public static void setUpContentViewPaddingDetailActivity(Activity activity, int padding) {
        View view = activity.findViewById(R.id.detailActivity_container);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

}
