/*
 * Created by Ofek Pintok on 1/14/19 11:34 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 7:39 PM
 */

package com.ofek.movieapp.list;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofek.movieapp.R;
import com.ofek.movieapp.models.MovieModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements Parcelable {

    private final List<MovieModel> mMoviesList;
    private MovieClickListener mMovieClickListener;
    private Picasso mPicasso;

    MoviesAdapter(List<MovieModel> list, MovieClickListener listener) {
        mMoviesList = new ArrayList<>(list);
        notifyDataSetChanged();
        mMovieClickListener = listener;
        mPicasso = Picasso.get();
    }

    List<MovieModel> getData() {
        return mMoviesList;
    }

    void clearData() {
        mMoviesList.clear();
        notifyDataSetChanged();
    }

    private MoviesAdapter(Parcel in) {
        mMoviesList = in.createTypedArrayList(MovieModel.CREATOR);
    }

    public static final Creator<MoviesAdapter> CREATOR = new Creator<MoviesAdapter>() {
        @Override
        public MoviesAdapter createFromParcel(Parcel in) {
            return new MoviesAdapter(in);
        }

        @Override
        public MoviesAdapter[] newArray(int size) {
            return new MoviesAdapter[size];
        }
    };

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view =  LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.item_new_movie, viewGroup, false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder viewHolder, int i) {
        //viewHolder holds a single row of the Recycler view
          viewHolder.bind(mMoviesList.get(i));
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mMoviesList);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView movieImage;
        final TextView titleTv;
        final TextView overviewTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Hold the elements of each movie in the Recycler view
            movieImage = itemView.findViewById(R.id.movie_image);
            titleTv = itemView.findViewById(R.id.movie_title);
            overviewTv = itemView.findViewById(R.id.movie_overview);
            itemView.setOnClickListener(this);
        }

        void bind(MovieModel movieModel) {
            // Set the data inside the fields
            mPicasso.load(movieModel.getImageUrl())
                    .into(movieImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.i("onSuccess", "image loaded");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("onError", "onError() called with: e = [" + e + "]");
                        }
                    });
            titleTv.setText(movieModel.getTitle());
            overviewTv.setText(movieModel.getOverview());
        }

        @Override
        public void onClick(View view) {
            if(mMovieClickListener != null)
                mMovieClickListener.onMovieClicked(getAdapterPosition());
        }
    }
}