/*
 * Created by Ofek Pintok on 1/14/19 11:36 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 2:28 PM
 */

package com.ofek.movieapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VideoModel {

    @PrimaryKey
    private int movieId;
    private String id;
    private String key;

    public VideoModel(int movieId, String id, String key) {
        this.movieId = movieId;
        this.id = id;
        this.key = key;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
