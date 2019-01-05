

/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 4:36 PM
 */

package com.ofek.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable {

    private int mMovieId;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mImageUrl;
    private String mBackImageUrl;

    public MovieModel(int movieId, String title, String overview, String releaseDate,
                      String mImageUrl, String mBackImageUrl) {
        this.mMovieId = movieId;
        this.mTitle = title;
        this.mOverview = overview;
        this.mImageUrl = mImageUrl;
        this.mReleaseDate = releaseDate;
        this.mBackImageUrl = mBackImageUrl;
    }

    protected MovieModel(Parcel in) {
        mMovieId = in.readInt();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mImageUrl = in.readString();
        mBackImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mImageUrl);
        dest.writeString(mBackImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public int getMovieId() {
        return mMovieId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getmBackImageUrl() {
        return mBackImageUrl;
    }

}