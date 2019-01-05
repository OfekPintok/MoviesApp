/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 6:03 PM
 */

package com.ofek.movieapp.network;

import com.ofek.movieapp.interfaces.MoviesService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ofek.movieapp.interfaces.MoviesService.BASE_API_URL;

public class RestClient {

    private static MoviesService moviesService;

    public static MoviesService getMoviesService() {
        if (moviesService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            moviesService = retrofit.create(MoviesService.class);
        }
        return moviesService;
    }

}
