
/*
 * Created by Ofek Pintok on 12/15/18 11:55 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/15/18 11:02 AM
 */

package com.ofek.movieapp.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ofek.movieapp.interfaces.MovieClickListener;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.adapters.MoviesAdapter;
import com.ofek.movieapp.R;
import com.ofek.movieapp.services.BackgroundServicesActivity;
import com.ofek.movieapp.threads.AsyncTaskActivity;
import com.ofek.movieapp.threads.ThreadsHandlerActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ofek.movieapp.activities.DetailsActivity.EXTRA_ITEM_POSITION;

public class MoviesActivity extends AppCompatActivity implements MovieClickListener {

    public static final List<MovieModel> MOVIES_LIST = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

            mRecyclerView = findViewById(R.id.movie_RecyclerView);

            if(savedInstanceState == null) {
                loadMovies(MOVIES_LIST);
            }

            //Initialize RecyclerView
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setHasFixedSize(true);
            moviesAdapter = new MoviesAdapter(MOVIES_LIST, this, this);
            mRecyclerView.setAdapter(moviesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.async_task) {
            Intent intent = new Intent(this, AsyncTaskActivity.class);
            startActivity(intent);
        }

        if(itemId == R.id.threads_handler) {
            Intent intent = new Intent (this, ThreadsHandlerActivity.class);
            startActivity(intent);
        }

        if(itemId == R.id.background_services) {
            Intent intent = new Intent(this, BackgroundServicesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

        @Override
    public void onMovieClicked(int itemPosition) {
        if(itemPosition < 0) return;
        MovieModel movie = MOVIES_LIST.get(itemPosition);
        if(movie == null || movie.getmTitle().isEmpty()) return;

        Intent intent = new Intent (this, DetailsActivity.class);
        intent.putExtra(EXTRA_ITEM_POSITION, itemPosition);
        startActivity(intent);
    }

    private void loadMovies(List<MovieModel> movieModel) {
        movieModel.add(new MovieModel
                (getString(R.string.Logan_title),
                        getString(R.string.Logan_overview),
                        getString(R.string.Logan_release),
                        getString(R.string.Logan_url),
                        R.drawable.a));

        movieModel.add(new MovieModel
                (getString(R.string.starwars_title),
                        getString(R.string.starwars_overview),
                        getString(R.string.starwars_release),
                        getString(R.string.starwars_url),
                        R.drawable.b));

        movieModel.add(new MovieModel
                (getString(R.string.Deadpool_title),
                        getString(R.string.deadpool_overview),
                        getString(R.string.deadpool_release),
                        getString(R.string.deadpool_url),
                        R.drawable.c));

        movieModel.add(new MovieModel
                (getString(R.string.Spiderman_title),
                        getString(R.string.spiderman_overview),
                        getString(R.string.spiderman_release),
                        getString(R.string.spiderman_url),
                        R.drawable.d));

        movieModel.add(new MovieModel
                (getString(R.string.Avatar_title),
                        getString(R.string.avatar_overview),
                        getString(R.string.avatar_release),
                        getString(R.string.avatar_url),
                        R.drawable.e));
    }
}
