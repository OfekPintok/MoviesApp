package com.ofek.movieapp.repo;

public interface Listener<Type> {
    void onSuccess(Type type);

    void onFailure(String e);
}
