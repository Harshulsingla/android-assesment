package com.example.booksviewer.application;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BooksViewer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}