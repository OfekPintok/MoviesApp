/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 6:03 PM
 */

package com.ofek.movieapp.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ofek.movieapp.network.MoviesService.BASE_API_URL;

public class RestClient {

    private static MoviesService moviesService;
    private static String nextPageToLoad = "1";

    private static OkHttpClient httpClient = new OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("page", nextPageToLoad)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);

            }).build();

    static MoviesService getMoviesService() {
        if (moviesService == null) {
            // Build Retrofit client
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            moviesService = retrofit.create(MoviesService.class);
        }
        return moviesService;
    }

    static void increaseLoadingParameter() {
        int nextPage = Integer.parseInt(nextPageToLoad) + 1;
        nextPageToLoad = String.valueOf(nextPage);
    }

    public static void resetLoadingParameter() {
        nextPageToLoad = "1";
    }

    public static boolean isFirstLoad() {
        return nextPageToLoad.equals("1");
    }
}
