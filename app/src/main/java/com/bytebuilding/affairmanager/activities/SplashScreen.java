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
import com.bytebuilding.affairmanager.utils.LoadingTask;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity implements LoadingTask
        .LoadingTaskFinishedListener {

    @BindView(R.id.splashScreen_imageView)
    ImageView splashImage;
    @BindView(R.id.splashScreen_progressBar)
    ProgressBar splashProgressBar;
    @BindView(R.id.splashScreen_textView)
    TextView splashTextView;

    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);

        splashProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(this)
                .build());

        new LoadingTask(splashProgressBar, this).execute("test_params");
    }

    @Override
    public void onPreferencesDetected() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(this, MainOnlineActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainOfflineActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPreferencesUndetected() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(this, EnterActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainOfflineActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
