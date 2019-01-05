/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 12:25 AM
 */

package com.ofek.movieapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.fragments.MoviesDetailsFragment;
import com.ofek.movieapp.R;

import static com.ofek.movieapp.models.MovieList.sMovieList;

// This activity hosts the fragments
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_POSITION = "extra.item-position";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

            mViewPager = findViewById(R.id.pager);
            mViewPager.setAdapter(new DetailsViewPagerAdapter(getSupportFragmentManager()));

            int itemPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);

            mViewPager.setCurrentItem(itemPosition, false);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    public class DetailsViewPagerAdapter extends FragmentStatePagerAdapter {

        public DetailsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            MovieModel movieModel = sMovieList.get(i);
            return MoviesDetailsFragment.newInstance(movieModel);
        }

        @Override
        public int getCount() {
            return sMovieList.size();
        }
    }

}


