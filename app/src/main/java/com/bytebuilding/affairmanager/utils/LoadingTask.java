package com.bytebuilding.affairmanager.utils;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.bytebuilding.affairmanager.activities.SplashScreen;

/**
 * Created by atlas on 15.03.17.
 */

public class LoadingTask extends AsyncTask<String, Integer, Integer> {

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
            return 1;
        } else {
            doSomeStuff();
            return 0;
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

            // Update the progress bar after every step
            int progress = (int) ((i / (float) count) * 100);
            publishProgress(progress);

            // Do some long loading things
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
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
        if (result == 0) {
            loadingTaskFinishedListener.onPreferencesUndetected();
        } else loadingTaskFinishedListener.onPreferencesDetected();
        //loadingTaskFinishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }
}
