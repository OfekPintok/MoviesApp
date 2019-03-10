
/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 2:02 PM
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
import android.widget.Button;
import android.widget.Toast;

import com.ofek.movieapp.database.DatabaseCore;
import com.ofek.movieapp.interfaces.OnFinishedBackgroundTask;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.network.RestClient;
import com.ofek.movieapp.interfaces.MovieClickListener;
import com.ofek.movieapp.adapters.MoviesAdapter;
import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MoviesListResponse;
import com.ofek.movieapp.network.ResponseConverter;
import com.ofek.movieapp.services.BackgroundServicesActivity;
import com.ofek.movieapp.threads.AsyncTaskActivity;
import com.ofek.movieapp.threads.ThreadsHandlerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ofek.movieapp.activities.DetailsActivity.EXTRA_ITEM_POSITION;
import static com.ofek.movieapp.models.MovieList.sMovieList;

public class MoviesActivity extends AppCompatActivity implements MovieClickListener, View.OnClickListener,
        OnFinishedBackgroundTask {

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

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.movie_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        // Set click listeners for buttons
        Button delButton = findViewById(R.id.main_delete_button);
        delButton.setOnClickListener(this);
        Button addButton = findViewById(R.id.main_add_page_button);
        addButton.setOnClickListener(this);

        if (savedInstanceState == null) {
            // Erase the data of the static movie list
            sMovieList.clear();
            RestClient.resetLoadingParameter();

            // Load cached data. when loading complete, inform listener.
            // @onFinishedTask will try to update the list from the server.
            DatabaseCore.getAllMovies(this, this);
        } else {
            moviesAdapter = new MoviesAdapter(sMovieList, this, this);
            mRecyclerView.setAdapter(moviesAdapter);
        }

    }

    @Override
    public void onFinishedTask(List<MovieModel> cachedMovies) {
        if (cachedMovies != null) {
            sMovieList.addAll(cachedMovies);
        }
        // Update the movies list if it is possible.
        loadMovies();
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_delete_button) {
            // Remove movies on background
            DatabaseCore.deleteAllMovies(this);

            // Reset everything back to beginning
            sMovieList.clear();
            RestClient.resetLoadingParameter();

            // Update the adapter
            moviesAdapter.setData(sMovieList);
        }

        if(v.getId() == R.id.main_add_page_button) {
            loadMovies();
        }
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);

        // Create and set the adapter with the current list
        moviesAdapter =
                new MoviesAdapter(sMovieList, MoviesActivity.this, MoviesActivity.this);
        mRecyclerView.setAdapter(moviesAdapter);


    }
}
