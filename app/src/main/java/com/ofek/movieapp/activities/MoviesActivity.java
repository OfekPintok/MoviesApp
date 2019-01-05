
/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 4:49 PM
 */

package com.ofek.movieapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ofek.movieapp.network.RestClient;
import com.ofek.movieapp.interfaces.MovieClickListener;
import com.ofek.movieapp.adapters.MoviesAdapter;
import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MoviesListResponse;
import com.ofek.movieapp.network.ResponseConverter;
import com.ofek.movieapp.services.BackgroundServicesActivity;
import com.ofek.movieapp.threads.AsyncTaskActivity;
import com.ofek.movieapp.threads.ThreadsHandlerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ofek.movieapp.activities.DetailsActivity.EXTRA_ITEM_POSITION;
import static com.ofek.movieapp.models.MovieList.sMovieList;

public class MoviesActivity extends AppCompatActivity implements MovieClickListener {

    private MoviesAdapter moviesAdapter;
    private RecyclerView mRecyclerView;
    private View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        progressBar = findViewById(R.id.main_progress);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.movie_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null) {
            loadMovies();
        } else {
            moviesAdapter = new MoviesAdapter(sMovieList, this, this);
            mRecyclerView.setAdapter(moviesAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.async_task) {
            Intent intent = new Intent(this, AsyncTaskActivity.class);
            startActivity(intent);
        }

        if (itemId == R.id.threads_handler) {
            Intent intent = new Intent(this, ThreadsHandlerActivity.class);
            startActivity(intent);
        }

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
        intent.putExtra(EXTRA_ITEM_POSITION, itemPosition);
        startActivity(intent);
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);

        // Create a call that gets the popular movies from the API
        Call<MoviesListResponse> call = RestClient.getMoviesService().searchPopularMovies();

        // Reset all the data that the static movie list holds
        sMovieList.clear();

        // Enqueue the call
        call.enqueue(new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesListResponse> call,
                                   @NonNull Response<MoviesListResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Add all the converted results into the list
                        sMovieList.addAll(ResponseConverter.movieResponseConvert(response.body()));
                        // Create and set the adapter with the current list
                        moviesAdapter =
                                new MoviesAdapter(sMovieList, MoviesActivity.this, MoviesActivity.this);
                        mRecyclerView.setAdapter(moviesAdapter);
                    } else {
                        Toast.makeText(MoviesActivity.this,
                                getString(R.string.no_data_in_response),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesListResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("failure", "network failure");
                Toast.makeText(MoviesActivity.this,
                        getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
