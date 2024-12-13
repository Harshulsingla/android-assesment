package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SearchInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

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
