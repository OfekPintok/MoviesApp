

/*
 * Created by Ofek Pintok on 12/1/18 8:29 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/1/18 8:29 PM
 */

package com.ofek.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

public class MovieModel implements Parcelable {

    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mTrailerUrl;
    @DrawableRes
    private int mImageRes;

    public MovieModel(String title, String overview, String releaseDate, String trailerUrl,
                      @DrawableRes int imageRes) {
        this.mTitle = title;
        this.mOverview = overview;
        this.mImageRes = imageRes;
        this.mReleaseDate = releaseDate;
        this.mTrailerUrl = trailerUrl;
    }

    protected MovieModel(Parcel in) {
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mTrailerUrl = in.readString();
        mImageRes = in.readInt();
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

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public int getmImageRes() {
        return mImageRes;
    }


    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmTrailerUrl() {
        return mTrailerUrl;
    }

    public static Creator<MovieModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mTrailerUrl);
        parcel.writeInt(mImageRes);
    }
}
