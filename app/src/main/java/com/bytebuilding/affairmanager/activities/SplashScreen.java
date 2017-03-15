package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
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

public class SplashScreen extends AppCompatActivity implements LoadingTask.LoadingTaskFinishedListener {

    @BindView(R.id.splashScreen_imageView)
    ImageView splashImage;
    @BindView(R.id.splashScreen_progressBar)
    ProgressBar splashProgressBar;
    @BindView(R.id.splashScreen_textView)
    TextView splashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        splashProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(this)
                .build());

        new LoadingTask(splashProgressBar, this).execute("test_params");
    }

    @Override
    public void onTaskFinished() {
        completeSplash();
    }

    private void completeSplash(){
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
