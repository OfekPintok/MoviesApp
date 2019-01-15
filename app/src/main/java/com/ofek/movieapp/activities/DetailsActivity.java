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

import java.util.Stack;

import static com.ofek.movieapp.models.MovieList.sMovieList;

// This activity hosts the fragments
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_POSITION = "extra.item-position";
    private ViewPager mViewPager;
    private Stack<Integer> fragmentStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        fragmentStack = new Stack<>();
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new DetailsViewPagerAdapter(getSupportFragmentManager()));

        int itemPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);
        fragmentStack.push(itemPosition);

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


