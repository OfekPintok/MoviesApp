package com.ofek.movieapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MovieModel;

public class DownloadActivity extends AppCompatActivity {

    private static final String ARG_MOVIE_MODEL = "extra.arg_movie_model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    public static void startActivity(Context context, MovieModel movieModel) {
        Intent intent = new Intent(context ,DownloadActivity.class);
        intent.putExtra(ARG_MOVIE_MODEL, movieModel);
        context.startActivity(intent);
    }
}
