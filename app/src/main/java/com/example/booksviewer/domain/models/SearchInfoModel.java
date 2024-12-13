package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;

public class SearchInfoModel {

    @SerializedName("textSnippet")
    private String textSnippet;

    // Getters and Setters
    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
}
