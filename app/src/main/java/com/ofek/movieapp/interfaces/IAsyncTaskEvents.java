/*
 * Created by Ofek Pintok on 12/4/18 6:51 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 12/4/18 6:51 PM
 */

package com.ofek.movieapp.interfaces;

public interface IAsyncTaskEvents {

    void onPreExecuteI();
    void onPostExecuteI();
    void onProgressUpdateI(Integer integer);
    void createAsyncTaskI();
    void startAsyncTaskI();
    void cancelAsyncTaskI();
    void onCancelI();
}
