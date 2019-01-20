/*
 * Created by Ofek Pintok on 1/15/19 7:38 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/15/19 7:38 AM
 */

package com.ofek.movieapp.database;

import android.content.Context;
import android.os.AsyncTask;

import com.ofek.movieapp.interfaces.MovieDao;
import com.ofek.movieapp.interfaces.VideoDao;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

import java.util.Collection;
import java.util.List;

public class AppRepository {

    private MovieDao movieDao;
    private VideoDao videoDao;
    private static VideoModel sVideoModel;
    private static List<MovieModel> SMovieList;


    AppRepository (Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.movieDao = db.movieDao();
        this.videoDao = db.videoDao();
    }

    //QUERY FUNCTIONS

    public List<MovieModel> getAllMovies() {
        new getAllMoviesAsyncTask(movieDao).execute();
        return SMovieList;
    }

    public void insertAllMovies(Collection<MovieModel> movies) {
        new insertAllMoviesAsyncTask(movieDao).execute(movies);
    }

    public void deleteAllMovies() { new deleteAllMoviesAsyncTask(movieDao).execute(); }

    public VideoModel getVideo(Integer movieId) {
        new getVideoAsyncTask(videoDao).execute(movieId);
        return sVideoModel;
    }

    public void insertVideo(VideoModel videoModel){
        new insertVideoAsyncTask(videoDao).execute(videoModel);
    }


    //CLASSES

    private static class getAllMoviesAsyncTask extends AsyncTask<Void, Void, List<MovieModel>> {

        MovieDao asyncMovieDao;

        getAllMoviesAsyncTask(MovieDao movieDao) {
            asyncMovieDao = movieDao;
        }

        @Override
        protected List<MovieModel> doInBackground(Void... voids) {
            return asyncMovieDao.getAll();
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            SMovieList = movieModels;
            super.onPostExecute(movieModels);
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

        getVideoAsyncTask(VideoDao videoDao) {
            asyncVideoDao = videoDao;
        }

        @Override
        protected VideoModel doInBackground(Integer... movieId) {
            return asyncVideoDao.getVideo(movieId[0]);
        }

        @Override
        protected void onPostExecute(VideoModel videoModel) {
            sVideoModel = videoModel;
            super.onPostExecute(videoModel);
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
