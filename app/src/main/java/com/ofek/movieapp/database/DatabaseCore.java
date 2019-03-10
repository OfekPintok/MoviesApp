package com.ofek.movieapp.database;

import android.content.Context;

import com.ofek.movieapp.interfaces.OnFinishedBackgroundTask;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

import java.util.Collection;

public class DatabaseCore {

    public static void getAllMovies(Context context,OnFinishedBackgroundTask taskListener) {
        DatabaseHelper.getDatabaseHelper(context).getAllMovies(taskListener);
    }

    public static void insertAllMovies(Context context, Collection<MovieModel> movies) {
        DatabaseHelper.getDatabaseHelper(context).insertAllMovies(movies);
    }

    public static void deleteAllMovies(Context context) {
        DatabaseHelper.getDatabaseHelper(context).deleteAllMovies();
    }

    public static VideoModel getVideo(Context context, Integer movieId) {
        return DatabaseHelper.getDatabaseHelper(context).getVideo(movieId);
    }

    public static void insertVideo(Context context, VideoModel videoModel) {
        DatabaseHelper.getDatabaseHelper(context).insertVideo(videoModel);
    }

}
