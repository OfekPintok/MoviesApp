/*
 * Created by Ofek Pintok on 1/14/19 11:36 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/14/19 6:52 PM
 */

package com.ofek.movieapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ofek.movieapp.models.MovieModel;

import java.util.Collection;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieModel ORDER BY mPopularity DESC")
    List<MovieModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<MovieModel> movies);

    @Query("DELETE FROM MovieModel")
    void deleteAll();
}

