/*
 * Created by Ofek Pintok on 1/14/19 11:36 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 2:49 PM
 */

package com.ofek.movieapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ofek.movieapp.models.VideoModel;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM VideoModel WHERE movieId = :movieId")
    VideoModel getVideo(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoModel videoModel);
}
