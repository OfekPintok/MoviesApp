package com.ofek.movieapp.download;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.ofek.movieapp.R;
import com.ofek.movieapp.threads.DownloadThread;

public class DownloadService extends Service {

    public static final String URL = "extra.URL";
    public static final int RUNNING_NOTIFICATION_ID = 120;
    public static final String CHANNEL_DEFAULT_IMPORTANCE = "channel";
    public static final String BROADCAST_ACTION = "com.ofek.movieapp.DOWNLOAD_COMPLETE";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();

        // Call for download thread method with the image URL that stored inside the intent
        String url = intent.getStringExtra(URL);
        if (url != null) {
            startDownloadThread(url);
        } else {
            Toast.makeText(this, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startDownloadThread(String url) {
        new DownloadThread(url, new DownloadThread.DownloadCallBack() {
            @Override
            public void onProgressUpdate(int percent) {
                updateNotification(percent);
            }

            @Override
            public void onDownloadFinished(String filePath) {
                sendBroadcastMsgDownloadComplete(filePath);
                stopSelf();
            }

            @Override
            public void onError(String error) {
                Log.e("TAG", "DownloadService, DownloadThread, Error: " + error);
                stopSelf();
            }
        }).start();
    }

    private void sendBroadcastMsgDownloadComplete(String filePath) {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(DownloadActivity.ARG_FILE_PATH, filePath);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static void startService(Activity activity, String url) {
        // Called from other activity; this method starts DownloadService.
        Intent intent = new Intent(activity, DownloadService.class);
        intent.putExtra(URL, url);
        activity.startService(intent);
    }

    private void startForeground() {
        createNotificationChannel();
        Notification notification = createNotification(0);
        startForeground(RUNNING_NOTIFICATION_ID, notification);
    }

    private Notification createNotification(int progress) {
        Intent notificationIntent = new Intent(this, DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String strProgress = getString(R.string.notification_progress, progress);

        return new NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(strProgress)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setProgress(100, progress, false)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Creation of notification channel
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            // Get notification manager
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(mChannel);
        }
    }

    private void updateNotification(int progress) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            Notification notification = createNotification(progress);
            notificationManager.notify(RUNNING_NOTIFICATION_ID, notification);
        }
    }

}
