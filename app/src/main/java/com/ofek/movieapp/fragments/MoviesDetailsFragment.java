/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 7:41 PM
 */

package com.ofek.movieapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ofek.movieapp.R;
import com.ofek.movieapp.database.AppDatabase;
import com.ofek.movieapp.database.DatabaseHelper;
import com.ofek.movieapp.interfaces.MoviesService;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;
import com.ofek.movieapp.network.ResponseConverter;
import com.ofek.movieapp.network.RestClient;
import com.ofek.movieapp.models.VideosListResponse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Response;

public class MoviesDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String BUNDLE_MOVIE = "MovieModel-data";

    private MovieModel mMovieModel;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private ImageView mBackImage;
    private View progressBar;

    public MoviesDetailsFragment() {

    }

    public static MoviesDetailsFragment newInstance(MovieModel movieModel) {
        MoviesDetailsFragment moviesDetailsFragment = new MoviesDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_MOVIE, movieModel);
        moviesDetailsFragment.setArguments(bundle);

        return moviesDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieModel = getArguments().getParcelable(BUNDLE_MOVIE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get reference to the views
        mTitle = view.findViewById(R.id.fragment_title);
        mReleaseDate = view.findViewById(R.id.fragment_release_date);
        mOverview = view.findViewById(R.id.fragment_overview);
        mBackImage = view.findViewById(R.id.fragment_imageView);
        progressBar = view.findViewById(R.id.fragment_progressBar);

        Button mTrailerBtn = view.findViewById(R.id.fragment_button);
        mTrailerBtn.setOnClickListener(this);

        setMovieDetails(mMovieModel);
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (mMovieModel == null) return;

        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) return;

        final Context context = fragmentActivity.getApplicationContext();
        if (context == null) return;

        final VideoModel videoModel =
                DatabaseHelper.getDatabaseHelper(context).getVideo(mMovieModel.getMovieId());//????
        if (videoModel != null) {
            playTrailer(videoModel.getKey());
            return;
        }

    Call<VideosListResponse> videoResponse =
            RestClient.getMoviesService().getVideos(mMovieModel.getMovieId());

    videoResponse.enqueue(new retrofit2.Callback<VideosListResponse>() {
        @Override
        public void onResponse(@NonNull Call<VideosListResponse> call,
                               @NonNull Response<VideosListResponse> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    // Convert the response and get the video URL
                    VideoModel convertedVideoModel = ResponseConverter.getTrailerUrl(response.body());
                    if (convertedVideoModel != null) {
                        DatabaseHelper.getDatabaseHelper(context).insertVideo(convertedVideoModel);
                        playTrailer(convertedVideoModel.getKey());
                    }

                } else {
                    // Response is successful but doesn't contain any data
                    Toast.makeText(getContext(),
                            getString(R.string.no_data_in_response),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<VideosListResponse> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(),
                        getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
        }
    });
    }

    public void setMovieDetails (MovieModel movieModel) {
        Picasso picasso = Picasso.get();
        mTitle.setText(movieModel.getTitle());
        mReleaseDate.setText(movieModel.getReleaseDate());
        mOverview.setText(movieModel.getOverview());
        picasso.load(movieModel.getBackImageUrl())
                .into(mBackImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("onSuccess", "Back Image loaded");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("onError", "Can't load image: error ["+e+"]");
                    }
                });
    }

    void playTrailer (String key) {
        String trailerUrl = MoviesService.YOUTUBE_URL + key;
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
        startActivity(browser);
        progressBar.setVisibility(View.GONE);
    }

}
