package com.ofek.movieapp.viewmodel;

import com.ofek.movieapp.models.MovieModel;

import java.util.List;

public interface OnLoadingMovies {

    void loadMovies(List<MovieModel> movies);

    }
