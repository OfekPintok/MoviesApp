package com.ofek.movieapp.repo;

import com.ofek.movieapp.models.VideoModel;

public interface OnVideoBackgroundTask {

    void onFinishedTask(VideoModel video);

    void taskFailed(String e);
}
