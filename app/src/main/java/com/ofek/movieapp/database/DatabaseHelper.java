package com.ofek.movieapp.database;

import android.content.Context;

import com.ofek.movieapp.models.MovieModel;
import com.ofek.movieapp.models.VideoModel;

import java.util.Collection;
import java.util.List;

public class DatabaseHelper {

    private static DatabaseHelper INSTANCE;
    private AppRepository appRepository;

    private DatabaseHelper(Context context) {
        appRepository = new AppRepository(context);
    }

    public static DatabaseHelper getDatabaseHelper(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context);
        }
        return INSTANCE;
    }

    public List<MovieModel> getAllMovies() {
        return appRepository.getAllMovies();
    }

    public void insertAllMovies(Collection<MovieModel> movies) {
        appRepository.insertAllMovies(movies);
    }

    public void deleteAllMovies() {
        appRepository.deleteAllMovies();
    }

    public VideoModel getVideo(Integer movieId) {
        return appRepository.getVideo(movieId);
    }

    public void insertVideo(VideoModel videoModel) {
        appRepository.insertVideo(videoModel);
    }

}
