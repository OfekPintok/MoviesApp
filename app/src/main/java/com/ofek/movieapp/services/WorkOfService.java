/*
 * Created by Ofek Pintok on 12/15/18 11:55 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/15/18 11:52 PM
 */

package com.ofek.movieapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;

import com.ofek.movieapp.R;

public class WorkOfService extends Service {

    private boolean mIsDestroyed;
    private static final String TAG = "WorkOfService";
    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        // Start the HandlerThread
        thread.start();
        // Start the service using the background handler
        mServiceHandler = new ServiceHandler(thread.getLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIsDestroyed = false;
        showToast(getString(R.string.service_started));
        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);

        return START_STICKY;
    }

    protected void showToast(final String msg) {
        Intent intent = new Intent(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
        intent.putExtra(BackgroundServicesActivity.BackgroundProgressReceiver.SERVICE_STATUS, msg);
        sendBroadcast(intent);
    }

    /* Custom Handler class for handling services */
    private final class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            for(int i = 0; i <= 100 && !mIsDestroyed; i++) {
                // We'll calling mServiceHandler.sendMessage(message);
                // this method will be called from onStartCommand.
                SystemClock.sleep(100);
                Intent intent =
                        new Intent(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_UPDATE_ACTION);
                intent.putExtra(BackgroundServicesActivity.BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
                sendBroadcast(intent);
            }
            showToast(getString(R.string.service_done));
            // the msg.arg1 is the startId used in the onStartCommand,so we can track the running service here.
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onDestroy() {
        mIsDestroyed = true;
        super.onDestroy();
    }
}
