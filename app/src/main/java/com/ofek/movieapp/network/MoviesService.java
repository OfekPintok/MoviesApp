/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 4:38 PM
 */

package com.ofek.movieapp.network;

import com.ofek.movieapp.models.MoviesListResponse;
import com.ofek.movieapp.models.VideosListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesService {

    // Base URL
    String BASE_URL = "https://api.themoviedb.org";
    String BASE_API_URL = BASE_URL + "/3/movie/";
    String CATEGORY = "popular";

    // Images paths
    String POSTER_URL = "https://image.tmdb.org/t/p/w342"; //w342, w780 goes for the size of the image
    String BACKDROP_URL = "https://image.tmdb.org/t/p/w780";

    // Base video
    String MOVIE_ID = "movie_id";
    String VIDEO = "{" + MOVIE_ID + "}/videos";
    String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    // API KEY
    String API_KEY = "628f500130fdf53db6dca16d421e69c8";
    String API_QUERY = "?api_key=" + API_KEY;

    // Popular movies path
    String POPULAR_MOVIES_PATH = CATEGORY + API_QUERY;

    // Video path
    String VIDEO_QUERY_PATH = BASE_API_URL + VIDEO + API_QUERY;

    // https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>
    @GET(POPULAR_MOVIES_PATH)
    Call<MoviesListResponse> searchPopularMovies();

    // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>
    @GET(VIDEO_QUERY_PATH)
    Call<VideosListResponse> getVideos(@Path(MOVIE_ID) int movieId);

}
