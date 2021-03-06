package com.bytebuilding.affairmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.SplashScreenLoadingTask;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashScreen extends AppCompatActivity implements SplashScreenLoadingTask
        .LoadingTaskFinishedListener {

    public static final String PREFERENCES_NAME = "AffairManagerPreferences";
    public static final String PREFERENCES_VERSION_CODE_KEY = "AffairManager";

    @BindView(R.id.splashScreen_imageView) ImageView splashImage;
    @BindView(R.id.splashScreen_progressBar) ProgressBar splashProgressBar;
    @BindView(R.id.splashScreen_textView) TextView splashTextView;

    public static SharedPreferences preferences = null;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        unbinder = ButterKnife.bind(this);

        preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        splashProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(this)
                .build());

        new SplashScreenLoadingTask(splashProgressBar, this).execute("test_params");
    }

    @Override
    public void onPreferencesDetected() {
        if (isNetworkAvailable()) {
            if (preferences.getString("type", "").equals(getResources().getStringArray(R.array
                    .registration_type_in_preferences)[3])) {
                goToEnterActivity();
            } else {
                goToMainOnlineActivity();
            }
        } else {
            goToMainOfflineActivity();
        }
    }

    private void goToMainOfflineActivity() {
        Intent intent = new Intent(this, MainOfflineActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainOnlineActivity() {
        Intent intent = new Intent(this, MainOnlineActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToEnterActivity() {
        Intent intent = new Intent(this, EnterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPreferencesUndetected() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            goToMainOfflineActivity();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();

        super.onDestroy();
    }
}
