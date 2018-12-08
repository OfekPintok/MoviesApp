/*
 * Created by Ofek Pintok on 12/1/18 8:29 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/1/18 7:44 PM
 */

package com.ofek.movieapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MovieModel;

public class MoviesDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String BUNDLE_MOVIE = "MovieModel-data";

    private MovieModel mMovieModel;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private ImageView mImageRes;

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
        mImageRes = view.findViewById(R.id.fragment_imageView);

        Button mTrailerBtn = view.findViewById(R.id.fragment_button);
        mTrailerBtn.setOnClickListener(this);

        setMovieDetails(mMovieModel);
    }

    @Override
    public void onClick(View view) {
    if(mMovieModel == null) return;

    String url = mMovieModel.getmTrailerUrl();
    if(url.isEmpty()) return;

    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);

    }

    public void setMovieDetails (MovieModel movieModel) {
        mTitle.setText(movieModel.getmTitle());
        mReleaseDate.setText(movieModel.getmReleaseDate());
        mOverview.setText(movieModel.getmOverview());
        mImageRes.setImageResource(movieModel.getmImageRes());
    }
}
