package com.ofek.movieapp.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.services.DownloadService;

public class DownloadActivity extends AppCompatActivity {

    public static final String ARG_MOVIE_MODEL = "extra.arg_movie_model";
    public static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int PERMISSIONS_REQUEST_CODE = 1;
    public static final String ARG_FILE_PATH = "extra.arg_file_path";
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String filePath = intent.getStringExtra(ARG_FILE_PATH);
                if(!TextUtils.isEmpty(filePath)) {
                    showImage(filePath);
                }
            }
        };

        if(isPermissionGranted()) {
            startDownloadService();
        } else {
            requestPermission();
        }
    }

    private void showImage(String filePath) {
        ImageView imageView = findViewById(R.id.download_imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    // Handling permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadService();
            } else {
                // User denied the permission. disable the functionality that depends on the permission.
                this.finish();
            }
        }
    }

    public static void startActivity(Context context, MovieModel movieModel) {
        Intent intent = new Intent(context ,DownloadActivity.class);
        intent.putExtra(ARG_MOVIE_MODEL, movieModel);
        context.startActivity(intent);
    }


    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // User doesn't have permission yet.
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
            // In case the user needs to see an explanation about permission.
            showExplainingRationaleDialog();
        } else {
            // No explanation is needed, ask for permission directly.
            requestWritePermission();
        }
    }

    private void showExplainingRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_exp_title);
        builder.setMessage(R.string.permission_exp_msg);
        builder.setPositiveButton(R.string.permission_exp_OK, (dialog, which) -> requestWritePermission());
        builder.setNegativeButton(R.string.permission_exp_cancel, (dialog, which) -> this.finish() );
        builder.show();
    }

    private void requestWritePermission() {
        // PERMISSIONS_REQUEST_CODE is an app-defined int constant.
        // It should be transferred later on as requestCode.
        ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, PERMISSIONS_REQUEST_CODE);
    }

    private void startDownloadService() {

        MovieModel movieModel = getIntent().getParcelableExtra(ARG_MOVIE_MODEL);
        if(movieModel == null) return;

        DownloadService.startService(this, movieModel.getBackImageUrl());
    }

}
