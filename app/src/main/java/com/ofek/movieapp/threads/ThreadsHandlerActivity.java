/*
 * Created by Ofek Pintok on 12/15/18 2:09 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/14/18 10:58 PM
 */

package com.ofek.movieapp.threads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ofek.movieapp.R;
import com.ofek.movieapp.fragments.ThreadFragment;
import com.ofek.movieapp.interfaces.IAsyncTaskEvents;

public class ThreadsHandlerActivity extends AppCompatActivity implements IAsyncTaskEvents {

    private ThreadFragment threadFragment;
    private MyCustomAsyncTask myCustomAsyncTask;
    final static private String BUNDLE_CURRENT_COUNT = "BUNDLE_CURRENT_COUNT";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        if (savedInstanceState == null) {
            // Create new threadFragment fragment
            threadFragment = new ThreadFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.asynctask_layout, threadFragment)
                    .commit();
        } else {
            if (threadFragment == null) {
                threadFragment =
                        (ThreadFragment) getSupportFragmentManager().findFragmentById(R.id.asynctask_layout);
            }
            if (myCustomAsyncTask == null) {
                myCustomAsyncTask = new MyCustomAsyncTask(this);
            }
            String currentS = savedInstanceState.getString(BUNDLE_CURRENT_COUNT);
            if (currentS != null) {
                if (currentS.equals(getString(R.string.done_counting)) || currentS.equals(getString(R.string.thread_start_msg))) {
                    threadFragment.updateTextView(currentS);
                } else {
                    myCustomAsyncTask.execute(Integer.valueOf(currentS));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String toKeep = threadFragment.getCountString();
        outState.putString(BUNDLE_CURRENT_COUNT, toKeep);
    }


    @Override
    public void onPreExecuteI() {
        threadFragment.updateTextView("");
    }

    @Override
    public void onPostExecuteI() {
        if(myCustomAsyncTask != null) {
            if (myCustomAsyncTask.isCancelled()) {
                myCustomAsyncTask = null;
            } else {
                threadFragment.updateTextView(getString(R.string.done_counting));
            }
        }
    }

    @Override
    public void onProgressUpdateI(Integer integer) {
        threadFragment.updateTextView(integer.toString());
    }

    @Override
    public void createAsyncTaskI() {
        if(myCustomAsyncTask == null) {
            myCustomAsyncTask = new MyCustomAsyncTask(this);
            Toast.makeText(this, getString(R.string.task_creating), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.task_already_running), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startAsyncTaskI() {
        if((myCustomAsyncTask != null) && !myCustomAsyncTask.isCancelled() ) {
            myCustomAsyncTask.execute(0);
        } else {
            Toast.makeText(this, getString(R.string.task_create_first), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelAsyncTaskI() {
        if(myCustomAsyncTask != null) {
            myCustomAsyncTask.cancel();
        } else {
            Toast.makeText(this, getString(R.string.task_no_tasks), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelI() {
        Toast.makeText(this, getString(R.string.task_canceled), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myCustomAsyncTask != null) {
            myCustomAsyncTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        if(myCustomAsyncTask != null) {
            myCustomAsyncTask.cancel();
            myCustomAsyncTask = null;
        }
        super.onDestroy();
    }
}
