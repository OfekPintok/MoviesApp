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
import java.util.concurrent.ExecutionException;

public class AppRepository {

    private MovieDao movieDao;
    private VideoDao videoDao;

    AppRepository (Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.movieDao = db.movieDao();
        this.videoDao = db.videoDao();
    }

    //QUERY FUNCTIONS

    public List<MovieModel> getAllMovies() {
        getAllMoviesAsyncTask moviesAsyncTask = new getAllMoviesAsyncTask(movieDao);

        // .get waits til the task is finished, since pulling the list from db takes time
        // and the list is needed asap
        try {
            moviesAsyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After background task is finished, return the received movies list.
        return moviesAsyncTask.getMoviesListFromAsyncTask();
    }

    public void insertAllMovies(Collection<MovieModel> movies) {
        new insertAllMoviesAsyncTask(movieDao).execute(movies);
    }

    public void deleteAllMovies() { new deleteAllMoviesAsyncTask(movieDao).execute(); }

    public VideoModel getVideo(Integer movieId) {
        getVideoAsyncTask videoAsyncTask = new getVideoAsyncTask(videoDao);
        videoAsyncTask.execute(movieId);

        // After background run is finished, return the received video.
        return videoAsyncTask.getVideoFromAsyncTask();
    }

    public void insertVideo(VideoModel videoModel){
        new insertVideoAsyncTask(videoDao).execute(videoModel);
    }


    //CLASSES

    private static class getAllMoviesAsyncTask extends AsyncTask<Void, Void, Void> {

        MovieDao asyncMovieDao;
        List<MovieModel> movieList;

        getAllMoviesAsyncTask(MovieDao movieDao) {
            asyncMovieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieList =  asyncMovieDao.getAll();
            return null;
        }

        public List<MovieModel> getMoviesListFromAsyncTask() {
            return movieList;
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

    private static class getVideoAsyncTask extends AsyncTask<Integer, Void, Void> {

        VideoDao asyncVideoDao;
        VideoModel videoModel;

        getVideoAsyncTask(VideoDao videoDao) {
            asyncVideoDao = videoDao;
        }

        @Override
        protected Void doInBackground(Integer... movieId) {
            videoModel = asyncVideoDao.getVideo(movieId[0]);
            return null;
        }

        public VideoModel getVideoFromAsyncTask() {
            return videoModel;
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
