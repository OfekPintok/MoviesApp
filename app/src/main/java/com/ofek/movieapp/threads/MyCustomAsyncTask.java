/*
 * Created by Ofek Pintok on 12/15/18 2:09 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/14/18 10:05 PM
 */

package com.ofek.movieapp.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.ofek.movieapp.interfaces.IAsyncTaskEvents;

public class MyCustomAsyncTask {

    private volatile boolean mCancelled = false;
    private Thread mBackgroundThread;

    private IAsyncTaskEvents mIAsyncTaskEvents;

    MyCustomAsyncTask (IAsyncTaskEvents iAsyncTaskEvents) {
        mIAsyncTaskEvents = iAsyncTaskEvents;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     */
    protected void onPreExecute() {
        mIAsyncTaskEvents.onPreExecuteI();
    }

    /**
     * Runs on new thread after {@link #onPreExecute()} and before {@link #onPostExecute()}.
     */
    protected void doInBackground(int toBeCounted) {
        for(int i = toBeCounted; i <= 10; i++ ) {
            if (isCancelled()) {
                return;
            }
            publishProgress(i);
            SystemClock.sleep(500);
        }
    }

    /**
     * Runs on the UI thread after {@link #doInBackground}
     */
    protected void onPostExecute(){
        mIAsyncTaskEvents.onPostExecuteI();
    }

    /**
     * @param toBeCounted is clearly the integer that holds the number of the counts.
     */
    public void execute(final int toBeCounted) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // The first step of the execution
                onPreExecute();
                mBackgroundThread = new Thread("Handler_executor_thread") {
                    @Override
                    public void run() {
                        // The second step of the execution
                        doInBackground(toBeCounted);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // The third step of the execution
                                onPostExecute();
                            }
                        });
                    }
                };
                mBackgroundThread.start();
            }
        });
    }


    /**
     * onProgressUpdate connects the thread's updated work with the UI.
     */
    protected void onProgressUpdate (Integer progress) {
        mIAsyncTaskEvents.onProgressUpdateI(progress);
    }

    /**
     * Get a Runnable with some functionality and post it via the Handler
     * Handler has to be connected to the main looper.
     */
    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * Create a new Runnable that has the functionality to call onProgressUpdate
     * and let it have the progress parameter
     * @param progress is the current parameter to be sent to the UI thread.
     */
    protected void publishProgress(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onProgressUpdate(progress);
            }
        });
    }

    public void cancel() {
        mCancelled = true;
        if (mBackgroundThread != null) {
            mBackgroundThread.interrupt();
        }
        mIAsyncTaskEvents.onCancelI();
    }

    public boolean isCancelled() {
        return mCancelled;
    }
}
