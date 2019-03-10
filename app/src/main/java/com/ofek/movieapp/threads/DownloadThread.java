package com.ofek.movieapp.threads;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DownloadThread extends Thread {

    private final String imageUrl;
    private final DownloadCallBack downloadCallBack;
    private long mLastUpdateTime;
    private int progress = 0;

    public DownloadThread(String url, DownloadCallBack downloadCallBack) {
        imageUrl = url;
        this.downloadCallBack = downloadCallBack;
    }

    @Override
    public void run() {
        // Create file
        File file = createFile();
        if (file == null) {
            downloadCallBack.onError("Can't create file");
            return;
        }

        // Create connection
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // On connection error
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                downloadCallBack.onError("Server returned HTTP response code: " + connection.getResponseCode() +
                        ":" + connection.getResponseMessage());
            }

            int fileLength = connection.getContentLength();
            Log.d("TAG", "File size: " + fileLength / 1024 + " KB");

            // Input stream (Downloading file)
            inputStream = new BufferedInputStream(url.openStream(), 8192);

            // Output stream (Saving file)
            fos = new FileOutputStream(file.getPath());

            byte[] data = new byte[1024];
            int next = inputStream.read(data);
            while(next != -1) {
                fos.write(data, 0, next);
                updateProgress(fos, fileLength);
                next = inputStream.read(data);
            }
            downloadCallBack.onDownloadFinished(file.getPath());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            downloadCallBack.onError("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallBack.onError("IOException: " + e.getMessage());
        }
    }

    private File createFile() {
        File mediaStorageDirectory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator);
        // Create the storage directory if it does not exists
        if(!mediaStorageDirectory.exists()) {
            if(!mediaStorageDirectory.mkdir()) {
                return null;
            }
        }
        // Create a media file name
        String imageName = createImageFileName() + ".jpg";
        return new File(mediaStorageDirectory.getPath() + File.separator + imageName);
    }

    @NonNull
    private String createImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return "File_" + timeStamp;
    }

    private void updateProgress(FileOutputStream fos, int fileLength) throws IOException {
        if(mLastUpdateTime == 0 || System.currentTimeMillis() > mLastUpdateTime + 500) {
            int count = ((int) fos.getChannel().size() * 100 / fileLength);
            if(count > progress) {
                progress = count;
                mLastUpdateTime = System.currentTimeMillis();
                downloadCallBack.onProgressUpdate(progress);
            }
        }
    }



    public interface DownloadCallBack {
        void onProgressUpdate(int percent);
        void onDownloadFinished(String filePath);
        void onError(String error);
    }
}
