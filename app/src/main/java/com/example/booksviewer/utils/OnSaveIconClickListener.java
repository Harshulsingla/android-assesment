package com.example.booksviewer.utils;


import com.example.booksviewer.domain.models.BookModel;

public interface OnSaveIconClickListener {
    void onHeartIconClick(BookModel book, int position);
}
