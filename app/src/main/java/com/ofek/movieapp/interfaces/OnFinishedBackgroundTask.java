package com.ofek.movieapp.interfaces;

import com.ofek.movieapp.models.MovieModel;

import java.util.List;

public interface OnFinishedBackgroundTask {

    void onFinishedTask(List<MovieModel> movieList);
}
