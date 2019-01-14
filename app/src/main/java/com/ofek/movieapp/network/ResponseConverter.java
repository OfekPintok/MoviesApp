/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 2:19 PM
 */

package com.ofek.movieapp.network;

import com.ofek.movieapp.interfaces.MoviesService;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.MovieResponse;
import com.ofek.movieapp.models.MoviesListResponse;
import com.ofek.movieapp.models.VideoModel;
import com.ofek.movieapp.models.VideoResponse;
import com.ofek.movieapp.models.VideosListResponse;

import java.util.ArrayList;
import java.util.List;

public class ResponseConverter {

    // Getting movies from response
    public static ArrayList<MovieModel> movieResponseConvert(MoviesListResponse body) {
        ArrayList<MovieModel> result = new ArrayList<>();

        // Run over the received results
        for(MovieResponse movieResponse : body.getResults()) {
            // Create a new MovieModel with the specific fields that I want!
            MovieModel currentMovie =
                    new MovieModel(movieResponse.getId(), movieResponse.getTitle(), movieResponse.getPopularity(),
                            movieResponse.getOverview(), movieResponse.getReleaseDate(),
                            MoviesService.POSTER_URL + movieResponse.getPosterPath(),
                            MoviesService.BACKDROP_URL + movieResponse.getBackdropPath());
            result.add(currentMovie);
        }
        return result;
    }

    // Getting Youtube address of the trailer from response
    public static VideoModel getTrailerUrl(VideosListResponse body) {
            List<VideoResponse> results = body.getResults();
            if (results != null && !results.isEmpty()) {
                VideoResponse videoResponse = results.get(0);
                return new VideoModel(body.getId(), videoResponse.getId(), videoResponse.getKey());
            }
        return null;
    }
}
