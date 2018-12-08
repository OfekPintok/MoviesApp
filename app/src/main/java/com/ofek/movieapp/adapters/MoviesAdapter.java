/*
 * Created by Ofek Pintok on 12/1/18 8:29 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/1/18 4:36 PM
 */

package com.ofek.movieapp.adapters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofek.movieapp.R;
import com.ofek.movieapp.interfaces.MovieClickListener;
import com.ofek.movieapp.models.MovieModel;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements Parcelable {

    private List<MovieModel> mList;
    private LayoutInflater mInfalter;
    private MovieClickListener mMovieClickListener;

    public MoviesAdapter (List<MovieModel> list, MovieClickListener listener , Context context) {
        this.mList = list;
        mMovieClickListener = listener;
        mInfalter = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view =  mInfalter.inflate(R.layout.item_new_movie, viewGroup, false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder viewHolder, int i) {
        //viewHolder is the "row" in the Recycler view
    viewHolder.bind(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieImage;
        public final TextView titleTv;
        public final TextView overviewTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Hold the elements of the movies row in the Recycler view
            movieImage = itemView.findViewById(R.id.movie_image);
            titleTv = itemView.findViewById(R.id.movie_title);
            overviewTv = itemView.findViewById(R.id.movie_overview);
            itemView.setOnClickListener(this);
        }

        public void bind(MovieModel movieModel) {
            // Set the data inside the fields
            movieImage.setImageResource(movieModel.getmImageRes());
            titleTv.setText(movieModel.getmTitle());
            overviewTv.setText(movieModel.getmOverview());
        }

        @Override
        public void onClick(View view) {
            if(mMovieClickListener != null)
                mMovieClickListener.onMovieClicked(getAdapterPosition());
        }
    }
}