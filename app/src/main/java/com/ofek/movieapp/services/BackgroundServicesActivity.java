/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 12/17/18 7:48 PM
 */

package com.ofek.movieapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ofek.movieapp.R;

import java.util.Locale;

import static com.ofek.movieapp.services.BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION;

public class BackgroundServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private BackgroundProgressReceiver mBackgroundProgressReceiver;

    private boolean mIsIntentServiceStarted;
    private boolean mIsServiceStarted;
    private Toast mToast;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_services);

        // Get reference for the buttons
        Button startIntentService = findViewById(R.id.button_start_intent_service);
        Button startService = findViewById(R.id.button_start_service);

        // Set onClickListener events for the buttons
        startIntentService.setOnClickListener(this);
        startService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start_intent_service) {
            if(mIsServiceStarted) {
                stopService(new Intent(this, WorkOfService.class));
                mIsServiceStarted = false;
            }
            if(!mIsIntentServiceStarted) {
                startService(new Intent(this, WorkOfIntentService.class));
                mIsIntentServiceStarted = true;
            } else {
                Toast.makeText(this, getString(R.string.service_is_already_running), Toast.LENGTH_SHORT).show();
            }
        } // End of IntentService if case

        if (view.getId() == R.id.button_start_service) {
            if(mIsIntentServiceStarted) {
                stopService(new Intent(this, WorkOfIntentService.class));
                mIsIntentServiceStarted = false;
            }
            if(!mIsServiceStarted) {
                startService(new Intent(this, WorkOfService.class));
                mIsServiceStarted = true;
            } else {
                Toast.makeText(this, getString(R.string.service_is_already_running), Toast.LENGTH_SHORT).show();
            }
        } // End of Service if case
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeForProgressUpdate();
    }


    @Override
    protected void onPause() {
        if(mBackgroundProgressReceiver != null) {
            unregisterReceiver(mBackgroundProgressReceiver);
        }
        super.onPause();
    }

    private void subscribeForProgressUpdate() {
        if (mBackgroundProgressReceiver == null) {
            mBackgroundProgressReceiver = new BackgroundProgressReceiver();
        }
        IntentFilter progressUpdateActionFilter = new IntentFilter(PROGRESS_UPDATE_ACTION);
        registerReceiver(mBackgroundProgressReceiver, progressUpdateActionFilter);
    }


    public class BackgroundProgressReceiver extends BroadcastReceiver {

        public static final String PROGRESS_UPDATE_ACTION = "PROGRESS_UPDATE_ACTION";
        public static final String PROGRESS_VALUE_KEY = "PROGRESS_VALUE_KEY";
        public static final String SERVICE_STATUS = "SERVICE_STATUS";

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get reference to the progress TextView and update it
            TextView progressPercent = findViewById(R.id.tv_progress_percent);

            // Receive the progress value
            int progress = intent.getIntExtra(PROGRESS_VALUE_KEY, -1);

            String text;

            if (progress >= 0) {
                if (progress == 100) {
                    text = getString(R.string.Done);
                    mIsIntentServiceStarted = false;
                    mIsServiceStarted = false;
                } else {
                    text = String.format(Locale.getDefault(), "%d%%", progress);
                }
                progressPercent.setText(text);
            }

            String msg = intent.getStringExtra(SERVICE_STATUS);
            if (msg != null) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (isFinishing()) {
            if (mIsIntentServiceStarted) {
                stopService(new Intent(this, WorkOfIntentService.class));
            }
            if (mIsServiceStarted) {
                stopService(new Intent(this, WorkOfService.class));
            }
            if (mToast != null) {
                mToast.cancel();
            }
        }
        super.onDestroy();
    }
}
