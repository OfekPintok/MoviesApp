package com.ofek.movieapp.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ofek.movieapp.R;
import com.ofek.movieapp.repo.Listener;
import com.ofek.movieapp.repo.OnMovieBackgroundTask;
import com.ofek.movieapp.models.MoviesListResponse;
import com.ofek.movieapp.models.VideoModel;
import com.ofek.movieapp.models.VideosListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkComponent {

    public static void searchMovies(Context context, OnMovieBackgroundTask taskListener) {
        // Create a call that gets the popular movies from the API
        Call<MoviesListResponse> call = RestClient.getMoviesService().searchPopularMovies();

        call.enqueue(new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesListResponse> call,
                                   @NonNull Response<MoviesListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Pass the results back to the repository
                        taskListener.onFinishedTask(ResponseConverter.movieResponseConvert(response.body()));
                    } else {
                        taskListener.taskFailed(context.getString(R.string.no_data_in_response));
                    }
                }
                // Increase the counter of the next page to be loaded
                RestClient.increaseLoadingParameter();
            }

            @Override
            public void onFailure(@NonNull Call<MoviesListResponse> call, @NonNull Throwable t) {
                Log.i("failure", "network failure");
                taskListener.taskFailed(context.getString(R.string.network_error));
            }
        });
    }

    public static void searchVideo(Context context, int movieId, final Listener<VideoModel> listener) {
        Call<VideosListResponse> videoResponse =
                RestClient.getMoviesService().getVideos(movieId);

        videoResponse.enqueue(new retrofit2.Callback<VideosListResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideosListResponse> call,
                                   @NonNull Response<VideosListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Convert the response and get the video URL
                        VideoModel convertedVideoModel = ResponseConverter.getTrailerUrl(response.body());
                        if (convertedVideoModel != null) {
                            listener.onSuccess(convertedVideoModel);
                        } else {
                        // Response is successful but doesn't contain any data
                        listener.onFailure(context.getString(R.string.no_data_in_response));
                    }
                }
            }}

            @Override
            public void onFailure(Call<VideosListResponse> call, Throwable t) {
                listener.onFailure(context.getString(R.string.network_error));
            }
        });
    }
}
