/*
 * Created by Ofek Pintok on 12/15/18 11:55 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/15/18 11:49 PM
 */

package com.ofek.movieapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.ofek.movieapp.R;

public class WorkOfIntentService extends IntentService {

    private boolean isDestroyed;
    private static final String TAG = "WorkOfIntentService";

    public WorkOfIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isDestroyed = false;
        showToast(getString(R.string.service_started));
        for(int i = 0; i <= 100 && !isDestroyed; i++) {
            SystemClock.sleep(100);
            Intent broadcastIntent =
                    new Intent(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
            broadcastIntent
                    .putExtra(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
            sendBroadcast(broadcastIntent);
        }
        showToast(getString(R.string.intent_service_finished));
    }

    private void showToast (final String msg) {
        Intent intent =
                new Intent(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
        intent.putExtra(BackgroundServicesActivity.BackgroundProgressReceiver.SERVICE_STATUS, msg);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }
}
