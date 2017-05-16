package com.bytebuilding.affairmanager.utils;

import android.app.Activity;
import android.view.View;

import com.bytebuilding.affairmanager.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Turkin A. on 16.05.17.
 */

public class Advertisement {

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
