/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 7:41 PM
 */

package com.ofek.movieapp.details;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofek.movieapp.repo.AppRepository;
import com.ofek.movieapp.R;
import com.ofek.movieapp.download.DownloadActivity;
import com.ofek.movieapp.repo.Listener;
import com.ofek.movieapp.network.MoviesService;
import com.ofek.movieapp.models.MovieModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MoviesDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String BUNDLE_MOVIE = "MovieModel-data";

    private MovieModel mMovieModel;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private ImageView mBackImage;
    private View mProgressBar;

    public MoviesDetailsFragment() {
        // Requires empty constructor.
    }

    public static MoviesDetailsFragment newInstance(MovieModel movieModel) {
        MoviesDetailsFragment moviesDetailsFragment = new MoviesDetailsFragment();

        // Save current movie for lifecycle's awareness
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

        // Get reference of views
        mTitle = view.findViewById(R.id.fragment_title);
        mReleaseDate = view.findViewById(R.id.fragment_release_date);
        mOverview = view.findViewById(R.id.fragment_overview);
        mBackImage = view.findViewById(R.id.fragment_imageView);
        mProgressBar = view.findViewById(R.id.fragment_progressBar);

        Button mTrailerBtn = view.findViewById(R.id.fragment_trailer_button);
        mTrailerBtn.setOnClickListener(this);

        ImageButton mDownloadImg = view.findViewById(R.id.fragment_download_ibutton);
        mDownloadImg.setOnClickListener(this);

        setMovieDetails(mMovieModel);
    }

    @Override
    // Fragment's click event handlers
    public void onClick(View view) {
        // Trailer button click event
        if (view.getId() == R.id.fragment_trailer_button) {
            showLoading();
            if (mMovieModel == null) return;

            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity == null) return;

            final Context context = fragmentActivity.getApplicationContext();
            if (context == null) return;

            AppRepository.getVideos(context, mMovieModel.getMovieId(), new Listener<String>() {
                @Override
                public void onSuccess(String key) {
                    playTrailer(key);
                }
                @Override
                public void onFailure(String e) {

                        }
                });

        }

        // Download Button Click Event
        if(view.getId() == R.id.fragment_download_ibutton) {
            DownloadActivity.startActivity(getContext(), mMovieModel);
        }

    }

    public void setMovieDetails (MovieModel movieModel) {
        // Set relevant text
        mTitle.setText(movieModel.getTitle());
        mReleaseDate.setText(String.format("Release date: %s", movieModel.getReleaseDate()));
        mOverview.setText(movieModel.getOverview());

        // Load image with Picasso
        Picasso picasso = Picasso.get();
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
        hideLoading();
    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

}
