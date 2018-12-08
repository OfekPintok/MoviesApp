/*
 * Created by Ofek Pintok on 12/4/18 3:39 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/4/18 3:39 PM
 */

package com.ofek.movieapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ofek.movieapp.R;
import com.ofek.movieapp.interfaces.IAsyncTaskEvents;


public class ThreadFragment extends Fragment implements View.OnClickListener {

    private TextView countTv;
    private IAsyncTaskEvents iAsyncTaskEvents;

    public ThreadFragment () {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread, container, false);

        Button createBtn = (Button) view.findViewById(R.id.thread_create),
                startBtn = (Button) view.findViewById(R.id.thread_start),
                cancelBtn = (Button) view.findViewById(R.id.thread_cancel);
        countTv = (TextView) view.findViewById(R.id.thread_request_msg);

        createBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        if(savedInstanceState != null) {
            countTv.setText(String.valueOf(savedInstanceState.getInt("Test1")));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Test1", countTv.getText().toString());
    }

    // When the fragment attach the activity
    // get the parent activity as an interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity hostActivity = getActivity();

        if (hostActivity instanceof IAsyncTaskEvents) {
            iAsyncTaskEvents = (IAsyncTaskEvents)hostActivity;
        }
    }

    // When the fragment detach the activity
    @Override
    public void onDetach() {
        super.onDetach();
        iAsyncTaskEvents = null;
    }


    @Override
    public void onClick(View view) {

        if(iAsyncTaskEvents == null) {
            return;
        }

        switch (view.getId()) {
            case (R.id.thread_create):
                iAsyncTaskEvents.createAsyncTaskI();
                break;

            case (R.id.thread_start):
                iAsyncTaskEvents.startAsyncTaskI();
                break;

            case (R.id.thread_cancel):
                iAsyncTaskEvents.cancelAsyncTaskI();
                break;
        }
    }

    public void updateTextView(String toUpdate) {
        if (toUpdate != null) {
            countTv.setText(toUpdate);
        }
    }

    public String getCountString () {
        return countTv.getText().toString();
    }
}
