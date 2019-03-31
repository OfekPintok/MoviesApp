package com.ofek.movieapp.repo;

import android.content.Context;

import com.ofek.movieapp.database.DatabaseComponent;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;
import com.ofek.movieapp.network.NetworkComponent;
import com.ofek.movieapp.network.RestClient;

import java.util.List;

public class AppRepository {

    public static void getMovies(final Context context, final Listener<List<MovieModel>> listener) {
        // call a request from the server for new data.
        NetworkComponent.searchMovies(context, new OnMovieBackgroundTask() {
            @Override
            public void onFinishedTask(List<MovieModel> movieList) {
                if (movieList != null) {
                    DatabaseComponent.insertAllMovies(context, movieList);
                    listener.onSuccess(movieList);
                }
            }

            @Override
            public void taskFailed(String e) {
                // Couldn't get the data from the network component for any reason.
                // Pass the error message back to listener.
                listener.onFailure(e);

                if (RestClient.isFirstLoad()) {
                    // Try to get cached movies from the local database.
                    DatabaseComponent.getAllMovies(context, new OnMovieBackgroundTask() {
                        @Override
                        public void onFinishedTask(List<MovieModel> cachedMovies) {
                            if (cachedMovies != null) {
                                listener.onSuccess(cachedMovies);
                            }
                        }

                        @Override
                        public void taskFailed(String e) {
                            // No cached data. Do nothing.
                        }
                    });
                }
            }
        });
    }

    public static void deleteMovies(Context context) {
        DatabaseComponent.deleteAllMovies(context);
        RestClient.resetLoadingParameter();
    }

    public static void getVideos(final Context context, int movieId, final Listener<String> listener) {
        // Try to get cached video from local database.
        // If there is none, make a request from the server.
        DatabaseComponent.getVideo(context, movieId, new OnVideoBackgroundTask() {
            @Override
            public void onFinishedTask(VideoModel video) {
                if (video != null) {
                    listener.onSuccess(video.getKey());
                } else {
                    // No cached data.
                    NetworkComponent.searchVideo(context, movieId, new Listener<VideoModel>() {

                        @Override
                        public void onSuccess(VideoModel videoModel) {
                            DatabaseComponent.insertVideo(context, videoModel);
                            listener.onSuccess(videoModel.getKey());
                        }

                        @Override
                        public void onFailure(String e) {
                            listener.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void taskFailed(String e) {

            }
        });
    }
}
