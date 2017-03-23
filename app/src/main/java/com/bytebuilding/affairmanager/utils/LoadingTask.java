package com.bytebuilding.affairmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bytebuilding.affairmanager.activities.LoginActivity;
import com.bytebuilding.affairmanager.activities.SplashScreen;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by atlas on 15.03.17.
 */

public class LoadingTask extends AsyncTask<String, Integer, Integer> {

    public static final int APP_HAS_PREFS = 1;
    public static final int APP_DO_NOT_HAVE_PREFS = 0;

    public interface LoadingTaskFinishedListener {
        void onPreferencesDetected();
        void onPreferencesUndetected();
    }

    private final ProgressBar splashProgressBar;
    private final LoadingTaskFinishedListener loadingTaskFinishedListener;

    public LoadingTask(ProgressBar splashProgressBar,
                       LoadingTaskFinishedListener loadingTaskFinishedListener) {
        this.splashProgressBar = splashProgressBar;
        this.loadingTaskFinishedListener = loadingTaskFinishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (checkPreferences()) {
            doSomeStuff();
            return APP_HAS_PREFS;
        } else {
            doSomeStuff();
            return APP_DO_NOT_HAVE_PREFS;
        }
    }

    private boolean checkPreferences() {
        if (SplashScreen.preferences.contains("login")) {
            return true;
        } else return false;
    }

    private void doSomeStuff() {
        int count = 5;
        for (int i = 0; i < count; i++) {
            int progress = (int) ((i / (float) count) * 100);
            publishProgress(progress);
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {
                Log.e("InterruptedException", "class-LoadingTask; method-doSomeStuff()");
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        splashProgressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        if (result == APP_HAS_PREFS) {
            loadingTaskFinishedListener.onPreferencesDetected();
        } else loadingTaskFinishedListener.onPreferencesUndetected();
    }
}