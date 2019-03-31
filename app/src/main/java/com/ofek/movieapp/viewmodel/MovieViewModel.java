package com.ofek.movieapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import com.ofek.movieapp.repo.AppRepository;
import com.ofek.movieapp.repo.Listener;
import com.ofek.movieapp.models.MovieModel;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ViewState> mViewState = new MutableLiveData<>();

    public LiveData<ViewState> getViewState() {
        return mViewState;
    }

    private void updateViewState(boolean showLoading, List<MovieModel> movies) {
        ViewState updatedViewState = new ViewState();

        // Update states from the given data.
        updatedViewState.showLoading = showLoading;
        if (movies != null) {
            updatedViewState.movies.addAll(movies);
        } else {
            updatedViewState.movies.clear();
        }
        mViewState.setValue(updatedViewState);
    }

    public void loadData(Context context) {
        // Show loading animation.
        updateViewState(true, null);

        AppRepository.getMovies(context, new Listener<List<MovieModel>>() {
            @Override
            public void onSuccess(List<MovieModel> movieModels) {
                updateViewState(false, movieModels);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
