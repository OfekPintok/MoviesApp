/*
 * Created by Ofek Pintok on 12/4/18 1:03 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/4/18 1:03 PM
 */

package com.ofek.movieapp.threads;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ofek.movieapp.R;
import com.ofek.movieapp.fragments.ThreadFragment;
import com.ofek.movieapp.interfaces.IAsyncTaskEvents;

public class AsyncTaskActivity extends AppCompatActivity implements IAsyncTaskEvents {

    private ThreadFragment threadFragment;
    private CounterAsyncTask counterAsyncTask;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        if (savedInstanceState == null) {
        // Create new threadFragment fragment
        threadFragment = new ThreadFragment();

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.asynctask_layout, threadFragment)
                .commit();

        } else {
            if(threadFragment == null) {
                threadFragment =
                        (ThreadFragment) getSupportFragmentManager().findFragmentById(R.id.asynctask_layout);
            }
            if (counterAsyncTask == null) {
                counterAsyncTask = new CounterAsyncTask(this);
            }

            String currentS = savedInstanceState.getString("Test");
            if (currentS != null) {
                if (currentS.equals(getString(R.string.done_counting))) {
                    threadFragment.updateTextView(currentS);
                } else {
                    counterAsyncTask.execute(Integer.valueOf(currentS));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Test", threadFragment.getCountString());
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
        threadFragment.updateTextView("");
    }

    // After executing the task
    @Override
    public void onPostExecuteI() {
        threadFragment.updateTextView(getString(R.string.done_counting));
        if (!counterAsyncTask.isCancelled()) {
            counterAsyncTask = new CounterAsyncTask(this);
        }
    }

    // While execute is on the run
    @Override
    public void onProgressUpdateI(Integer toCount) {
        threadFragment.updateTextView(String.valueOf(toCount));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (counterAsyncTask != null) {
            counterAsyncTask.cancel(true);
        }
    }

    // When task's destroyed
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

        public CounterAsyncTask (IAsyncTaskEvents iAsyncTaskEvents) {
            this.mIAsyncTaskEvents = iAsyncTaskEvents;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int currentCount = 0;
            if (integers.length == 1) { // One digit
                currentCount = integers[0].intValue(); // Get value of the digit
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

