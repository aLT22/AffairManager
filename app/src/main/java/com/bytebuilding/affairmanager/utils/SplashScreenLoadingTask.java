package com.bytebuilding.affairmanager.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.bytebuilding.affairmanager.activities.SplashScreen;

public class SplashScreenLoadingTask extends AsyncTask<String, Integer, Integer> {

    public static final int APP_HAS_PREFS = 1;
    public static final int APP_DO_NOT_HAVE_PREFS = 0;

    public interface LoadingTaskFinishedListener {
        void onPreferencesDetected();
        void onPreferencesUndetected();
    }

    private final ProgressBar splashProgressBar;
    private final LoadingTaskFinishedListener loadingTaskFinishedListener;

    public SplashScreenLoadingTask(ProgressBar splashProgressBar,
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
        return SplashScreen.preferences.contains("login");
    }

    private void doSomeStuff() {
        int count = 5;
        for (int i = 0; i < count; i++) {
            int progress = (int) ((i / (float) count) * 100);
            publishProgress(progress);
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {
                Log.e("InterruptedException", "class-SplashScreenLoadingTask; method-doSomeStuff()");
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