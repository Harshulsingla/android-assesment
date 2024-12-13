package com.example.dictionary.application;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class Dictionary extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
