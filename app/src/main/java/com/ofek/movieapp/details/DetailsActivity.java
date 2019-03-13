/*
 * Created by Ofek Pintok on 1/15/19 7:38 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/15/19 7:29 AM
 */

package com.ofek.movieapp.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.R;

import java.util.List;
import java.util.Stack;

// This activity hosts the fragments
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_POSITION = "extra.item-position";
    public static final String EXTRA_STACK_STATE = "extra.stack-state";
    public static final String EXTRA_MOVIE_LIST = "extra.movie-list";
    private ViewPager mViewPager;
    private Stack<Integer> mFragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        List<MovieModel> movieModels = getIntent().getParcelableArrayListExtra(EXTRA_MOVIE_LIST);
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new DetailsViewPagerAdapter(movieModels, getSupportFragmentManager()));
        int itemPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);
        // Create a stack that saves the swipes order of the user
        mFragmentStack = new Stack<>();

        // Restore the destroyed stack (if was exist).
        if (savedInstanceState != null) {
            int[] restoredScrollOrder = savedInstanceState.getIntArray(EXTRA_STACK_STATE);
            if (restoredScrollOrder != null && mFragmentStack.isEmpty()) {
                for (int aRestoredScroll : restoredScrollOrder) {
                    mFragmentStack.push(aRestoredScroll);
                }
            }
        } else {
            // Push current item
            mFragmentStack.push(itemPosition);
        }
        mViewPager.setCurrentItem(itemPosition, false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mFragmentStack.push(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //  Saving stack states
        // *We won't save the current position since it would be re-initialized after the recreation
        if(!mFragmentStack.isEmpty()) {
            mFragmentStack.pop();
        }

        int stackSize = mFragmentStack.size();
        int[] scrollOrder = new int[stackSize];

        // Create the exactly same order of the stack saved in an array
        for(int i = stackSize-1; i > 0; i--) {
            scrollOrder[i] = mFragmentStack.pop();
        }
        outState.putIntArray(EXTRA_STACK_STATE, scrollOrder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // Pop out the currently watched fragment
        if(mFragmentStack.size() > 1) {
            mFragmentStack.pop();
        }

        // Set the previous item as the current item
        if(!mFragmentStack.isEmpty()) {
            mViewPager.setCurrentItem(mFragmentStack.pop());
        }

        if (mFragmentStack.isEmpty()) {
            // If the stack is currently empty, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
    }

    public class DetailsViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<MovieModel> movies;

        DetailsViewPagerAdapter(List<MovieModel> movies, FragmentManager fm) {
            super(fm);
            this.movies = movies;
        }

        @Override
        public Fragment getItem(int i) {
            MovieModel movieModel = movies.get(i);
            return MoviesDetailsFragment.newInstance(movieModel);
        }

        @Override
        public int getCount() {
            return movies.size();
        }
    }

}