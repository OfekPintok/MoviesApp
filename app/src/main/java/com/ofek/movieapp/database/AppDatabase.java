/*
 * Created by Ofek Pintok on 1/15/19 7:38 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/15/19 7:25 AM
 */

package com.ofek.movieapp.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ofek.movieapp.interfaces.MovieDao;
import com.ofek.movieapp.interfaces.VideoDao;
import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

@Database(entities = {MovieModel.class, VideoModel.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static final String DATABASE_NAME = "Movies";

    public abstract MovieDao movieDao();

    public abstract VideoDao videoDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
    return INSTANCE;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

}
