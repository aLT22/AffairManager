package com.bytebuilding.affairmanager.utils;

import android.os.AsyncTask;
import android.widget.ProgressBar;

/**
 * Created by atlas on 15.03.17.
 */

public class LoadingTask extends AsyncTask<String, Integer, Integer> {

    public interface LoadingTaskFinishedListener {
        void onTaskFinished();
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
        if(resourcesDontAlreadyExist()){
            downloadResources();
        }
        //Возвращаем здесь какой-либо ответ после завершения операций в бекграунде
        return -1;
    }

    private boolean resourcesDontAlreadyExist() {
        /*
        * В данном методе "чекаем", есть ли before-downloaded данные
        * */
        // Here you would query your app's internal state to see if this download had been performed before
        // Perhaps once checked save this in a shared preference for speed of access next time
        return true; // returning true so we show the splash every time
    }

    private void downloadResources() {
        // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
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
        loadingTaskFinishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }
}
