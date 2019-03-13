
/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 2:02 PM
 */

package com.ofek.movieapp.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ofek.movieapp.repo.AppRepository;
import com.ofek.movieapp.viewmodel.MovieViewModel;
import com.ofek.movieapp.details.DetailsActivity;
import com.ofek.movieapp.repo.Listener;
import com.ofek.movieapp.viewmodel.OnLoadingMovies;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.R;
import com.ofek.movieapp.network.RestClient;
import com.ofek.movieapp.services.BackgroundServicesActivity;
import com.ofek.movieapp.threads.AsyncTaskActivity;
import com.ofek.movieapp.threads.ThreadsHandlerActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ofek.movieapp.details.DetailsActivity.EXTRA_ITEM_POSITION;

public class MoviesActivity extends AppCompatActivity implements MovieClickListener, OnLoadingMovies,
        View.OnClickListener{

    private MoviesAdapter mMoviesAdapter;
    private RecyclerView mRecyclerView;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        mProgressBar = findViewById(R.id.main_progress);

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.movie_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        // Set click listeners for buttons
        Button delButton = findViewById(R.id.main_delete_button);
        delButton.setOnClickListener(this);
        Button addButton = findViewById(R.id.main_add_page_button);
        addButton.setOnClickListener(this);

        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        // Register for observation of any change in the view model.
        viewModel.getViewState().observe(this, viewState -> {
            if(viewState != null) {
                if (viewState.showLoading) {
                    showLoading();
                } else {
                    hideLoading();
                }
                if(viewState.movies != null) {
                   loadMovies(viewState.movies);
                }
            }
        });

        if(savedInstanceState == null) {
            // Reset the loading counter whenever the application shows up (before it starts).
            RestClient.resetLoadingParameter();
            // Load cached data. when loading complete, inform listener.
            viewModel.loadData(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    // Activity's menu options handling
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // Async task activity click event
        if (itemId == R.id.async_task) {
            Intent intent = new Intent(this, AsyncTaskActivity.class);
            startActivity(intent);
        }
        // Threads activity click event
        if (itemId == R.id.threads_handler) {
            Intent intent = new Intent(this, ThreadsHandlerActivity.class);
            startActivity(intent);
        }
        // Background service activity click event
        if (itemId == R.id.background_services) {
            Intent intent = new Intent(this, BackgroundServicesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(int itemPosition) {
        if (itemPosition < 0) return;

        Intent intent = new Intent(this, DetailsActivity.class);
        // Put current item position (the one that was clicked).
        intent.putExtra(EXTRA_ITEM_POSITION, itemPosition);
        // Put current movie list.
        intent.putParcelableArrayListExtra(DetailsActivity.EXTRA_MOVIE_LIST,
                (ArrayList<? extends Parcelable>) mMoviesAdapter.getData());
        startActivity(intent);
    }

    @Override
    // Activity's click event handlers
    public void onClick(View v) {
        // Delete button click event
        if(v.getId() == R.id.main_delete_button) {
            AppRepository.deleteMovies(this);
            clearAdapter();
        }

        // New page button click event
        if(v.getId() == R.id.main_add_page_button) {
            AppRepository.getMovies(this, new Listener<List<MovieModel>>() {
                @Override
                // Received data that contains only the next page of movies.
                public void onSuccess(List<MovieModel> newMovies) {
                    List<MovieModel> movieList = mMoviesAdapter.getData();
                    movieList.addAll(newMovies);
                    loadMovies(movieList);
                }

                @Override
                public void onFailure(String msg) {
                    onError(msg);
                }
            });
        }
    }

    @Override
    public void loadMovies(List<MovieModel> movies) {
            // Create new adapter with the current list.
            mMoviesAdapter = new MoviesAdapter(movies, MoviesActivity.this);
            mRecyclerView.setAdapter(mMoviesAdapter);
    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void clearAdapter() {
        mMoviesAdapter.clearData();
    }

    private void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
