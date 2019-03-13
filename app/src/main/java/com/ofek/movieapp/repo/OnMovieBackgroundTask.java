package com.ofek.movieapp.repo;

import com.ofek.movieapp.models.MovieModel;

import java.util.List;

public interface OnMovieBackgroundTask {

    void onFinishedTask(List<MovieModel> movieList);

    void taskFailed(String e);
}
