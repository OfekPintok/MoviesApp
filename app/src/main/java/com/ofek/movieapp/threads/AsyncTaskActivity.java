/*
 * Created by Ofek Pintok on 12/15/18 2:12 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/14/18 11:18 PM
 */

package com.ofek.movieapp.threads;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ofek.movieapp.R;

public class AsyncTaskActivity extends AppCompatActivity implements IAsyncTaskEvents {

    private ThreadFragment mThreadFragment;
    private CounterAsyncTask counterAsyncTask;
    final static private String BUNDLE_CURRENT_COUNT = "BUNDLE_CURRENT_COUNT";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        if (savedInstanceState == null) {
        // Create new threadFragment fragment
        mThreadFragment = new ThreadFragment();

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.asynctask_layout, mThreadFragment)
                .commit();

        } else {
            if(mThreadFragment == null) {
                mThreadFragment =
                        (ThreadFragment) getSupportFragmentManager().findFragmentById(R.id.asynctask_layout);
            }
            if (counterAsyncTask == null) {
                counterAsyncTask = new CounterAsyncTask(this);
            }

            String currentS = savedInstanceState.getString(BUNDLE_CURRENT_COUNT);
            if (currentS != null) {
                if (currentS.equals(getString(R.string.done_counting)) || currentS.equals(getString(R.string.thread_start_msg))) {
                    mThreadFragment.updateTextView(currentS);
                    } else {
                    counterAsyncTask.execute(Integer.valueOf(currentS));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_CURRENT_COUNT, mThreadFragment.getCountString());
    }

    // When clicking on Create
    @Override
    public void createAsyncTaskI() {
        if (counterAsyncTask == null) {
            counterAsyncTask = new CounterAsyncTask(this);
            Toast.makeText(this, getString(R.string.task_creating), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.task_already_running), Toast.LENGTH_SHORT).show();
        }
    }

    // When clicking on Start
    @Override
    public void startAsyncTaskI() {
        if (counterAsyncTask != null) {
        counterAsyncTask.execute(0);
        } else {
            Toast.makeText(this, getString(R.string.task_create_first), Toast.LENGTH_SHORT).show();
        }
    }

    // When clicking on Cancel
    @Override
    public void cancelAsyncTaskI() {
        if (counterAsyncTask != null) {
            counterAsyncTask.cancel(true);
            counterAsyncTask = new CounterAsyncTask(this);
        } else {
            Toast.makeText(this, getString(R.string.task_no_tasks), Toast.LENGTH_SHORT).show();
        }
    }

    // When task's canceled
    @Override
    public void onCancelI() {
        Toast.makeText(this, getString(R.string.task_canceled), Toast.LENGTH_SHORT).show();
    }

    // Before executing the task
    @Override
    public void onPreExecuteI() {
        mThreadFragment.updateTextView("");
    }

    // After executing the task
    @Override
    public void onPostExecuteI() {
        if(counterAsyncTask != null) {
            if (!counterAsyncTask.isCancelled()) {
                counterAsyncTask = new CounterAsyncTask(this);
            } else {
                mThreadFragment.updateTextView(getString(R.string.done_counting));
            }
        }
    }

    // While execute is on the run
    @Override
    public void onProgressUpdateI(Integer toCount) {
        mThreadFragment.updateTextView(String.valueOf(toCount));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (counterAsyncTask != null) {
            counterAsyncTask.cancel(true);
        }
    }

    // When task is destroyed
    @Override
    protected void onDestroy() {
        if (counterAsyncTask != null) {
            counterAsyncTask.cancel(true);
            counterAsyncTask = null;
        }
        super.onDestroy();
    }


    private static class CounterAsyncTask extends AsyncTask<Integer, Integer, String> {
        // This class has the ability to run a for loop that counts to 10
        // while running on the worker thread
        // but it is dangerous to update the UI from here

        private IAsyncTaskEvents mIAsyncTaskEvents;

        CounterAsyncTask(IAsyncTaskEvents iAsyncTaskEvents) {
            this.mIAsyncTaskEvents = iAsyncTaskEvents;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int currentCount = 0;
            if (integers.length == 1) { // One digit
                currentCount = integers[0]; // Get value of the digit
            } else {
                currentCount = 10; // Max number to count is 10
            }
            for (int length = 10; currentCount <= length; currentCount++) {
                if(isCancelled()) {
                    return null;
                }
                publishProgress(currentCount);
                SystemClock.sleep(500);
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            if(mIAsyncTaskEvents != null) {
                mIAsyncTaskEvents.onPreExecuteI();
            }
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if(mIAsyncTaskEvents != null) {
                mIAsyncTaskEvents.onPostExecuteI();
            }
        }

        @Override
        public void onProgressUpdate(Integer... toCount) {
            super.onProgressUpdate(toCount);
            if (mIAsyncTaskEvents != null) {
                mIAsyncTaskEvents.onProgressUpdateI(toCount[0]);
            }
        }

    }


}

