package com.ofek.movieapp.database;

import android.content.Context;

import com.ofek.movieapp.repo.OnMovieBackgroundTask;
import com.ofek.movieapp.repo.OnVideoBackgroundTask;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

import java.util.Collection;

public class DatabaseComponent {

    public static void getAllMovies(Context context, OnMovieBackgroundTask taskListener) {
        DatabaseHelper.getDatabaseHelper(context).getAllMovies(taskListener);
    }

    public static void insertAllMovies(Context context, Collection<MovieModel> movies) {
        DatabaseHelper.getDatabaseHelper(context).insertAllMovies(movies);
    }

    public static void deleteAllMovies(Context context) {
        DatabaseHelper.getDatabaseHelper(context).deleteAllMovies();
    }

    public static void getVideo(Context context, Integer movieId, OnVideoBackgroundTask taskListener) {
        DatabaseHelper.getDatabaseHelper(context).getVideo(movieId, taskListener);
    }

    public static void insertVideo(Context context, VideoModel videoModel) {
        DatabaseHelper.getDatabaseHelper(context).insertVideo(videoModel);
    }

}
