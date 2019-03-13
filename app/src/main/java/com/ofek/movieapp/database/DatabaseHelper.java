/*
 * Created by Ofek Pintok on 1/15/19 7:38 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/15/19 7:38 AM
 */

package com.ofek.movieapp.database;

import android.content.Context;
import android.os.AsyncTask;

import com.ofek.movieapp.repo.OnMovieBackgroundTask;
import com.ofek.movieapp.repo.OnVideoBackgroundTask;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

import java.util.Collection;
import java.util.List;

class DatabaseHelper {

    private static DatabaseHelper INSTANCE;

    private MovieDao movieDao;
    private VideoDao videoDao;

    DatabaseHelper(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.movieDao = db.movieDao();
        this.videoDao = db.videoDao();
    }

    static DatabaseHelper getDatabaseHelper(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context);
        }
        return INSTANCE;
    }

    //QUERY FUNCTIONS

    void getAllMovies(OnMovieBackgroundTask taskListener) {
        getAllMoviesAsyncTask moviesAsyncTask = new getAllMoviesAsyncTask(movieDao, taskListener);

        moviesAsyncTask.execute();
    }

    void insertAllMovies(Collection<MovieModel> movies) {
        new insertAllMoviesAsyncTask(movieDao).execute(movies);
    }

    void deleteAllMovies() { new deleteAllMoviesAsyncTask(movieDao).execute(); }

    void getVideo(Integer movieId, OnVideoBackgroundTask taskListener) {
        getVideoAsyncTask videoAsyncTask = new getVideoAsyncTask(videoDao, taskListener);

        videoAsyncTask.execute(movieId);
    }

    void insertVideo(VideoModel videoModel){
        new insertVideoAsyncTask(videoDao).execute(videoModel);
    }


    //CLASSES

    private static class getAllMoviesAsyncTask extends AsyncTask<Void, Void, List<MovieModel>> {

        MovieDao asyncMovieDao;
        OnMovieBackgroundTask taskListener;

        getAllMoviesAsyncTask(MovieDao movieDao, OnMovieBackgroundTask listener) {
            asyncMovieDao = movieDao;
            taskListener = listener;
        }

        @Override
        protected List<MovieModel> doInBackground(Void... voids) {
            return asyncMovieDao.getAll();
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            super.onPostExecute(movieModels);

            // After background task is finished, return the received movies list via listener;
            taskListener.onFinishedTask(movieModels);
        }
    }

    private static class insertAllMoviesAsyncTask extends AsyncTask<Collection<MovieModel>, Void, Void> {

        MovieDao asyncMovieDao;

        insertAllMoviesAsyncTask(MovieDao movieDao) {
            asyncMovieDao = movieDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Collection<MovieModel>... collections) {
            asyncMovieDao.insertAll(collections[0]);
            return null;
        }

    }

    private static class deleteAllMoviesAsyncTask extends AsyncTask<Void, Void, Void> {

        MovieDao asyncMovieDao;

        deleteAllMoviesAsyncTask(MovieDao movieDao) {
            asyncMovieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncMovieDao.deleteAll();
            return null;
        }
    }

    private static class getVideoAsyncTask extends AsyncTask<Integer, Void, VideoModel> {

        VideoDao asyncVideoDao;
        OnVideoBackgroundTask taskListener;

        getVideoAsyncTask(VideoDao videoDao, OnVideoBackgroundTask taskListener) {
            asyncVideoDao = videoDao;
            this.taskListener = taskListener;
        }

        @Override
        protected VideoModel doInBackground(Integer... movieId) {
            return asyncVideoDao.getVideo(movieId[0]);
        }

        @Override
        protected void onPostExecute(VideoModel videoModel) {
            super.onPostExecute(videoModel);

            // After background task is finished, return the received movies list via listener;
            taskListener.onFinishedTask(videoModel);
        }
    }

    private static class insertVideoAsyncTask extends AsyncTask<VideoModel, Void, Void> {

        VideoDao asyncVideoDao;

        insertVideoAsyncTask(VideoDao videoDao) {
            asyncVideoDao = videoDao;
        }

        @Override
        protected Void doInBackground(VideoModel... videoModels) {
            asyncVideoDao.insert(videoModels[0]);
            return null;
        }
    }
}
