/*
 * Created by Ofek Pintok on 1/15/19 7:38 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/15/19 7:29 AM
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

import java.util.ArrayList;
import java.util.Stack;

import static com.ofek.movieapp.models.MovieList.sMovieList;

// This activity hosts the fragments
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_POSITION = "extra.item-position";
    public static final String EXTRA_STACK_STATE = "extra.stack-state";
    private ViewPager mViewPager;
    private Stack<Integer> fragmentStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new DetailsViewPagerAdapter(getSupportFragmentManager()));

        int itemPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);

        // Create a stack that saves the swipes order of the user, inside the ViewPager
        fragmentStack = new Stack<>();

        // Restore the destroyed stack, if was exist
        if(savedInstanceState != null) {
            int[] restoredScrollOrder = savedInstanceState.getIntArray(EXTRA_STACK_STATE);
            if (restoredScrollOrder != null && fragmentStack.isEmpty()) {
                for (int aRestoredScroll : restoredScrollOrder) {
                    fragmentStack.push(aRestoredScroll);
                }
            }
        } else {
            fragmentStack.push(itemPosition);
        }


        mViewPager.setCurrentItem(itemPosition, false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                fragmentStack.push(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // We won't save the current position since it would be saved after the recreation
        if(!fragmentStack.isEmpty()) {
            fragmentStack.pop();
        }

        int stackSize = fragmentStack.size();
        int[] scrollOrder = new int[stackSize];

        // Create the exactly same order of the stack inside an array
        for(int i = stackSize-1; i > 0; i--) {
            scrollOrder[i] = fragmentStack.pop();
        }
        outState.putIntArray(EXTRA_STACK_STATE, scrollOrder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // Pop out the currently watched fragment
        if(fragmentStack.size() > 1) {
            fragmentStack.pop();
        }
            // Set the previous item as the current item
            mViewPager.setCurrentItem(fragmentStack.pop());
        if (fragmentStack.isEmpty()) {
            // If the stack is currently empty, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
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


