package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookResponseModel {

    @SerializedName("kind")
    private String kind;

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private List<BookModel> items;

    // Getters and Setters
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<BookModel> getItems() {
        return items;
    }

    public void setItems(List<BookModel> items) {
        this.items = items;
    }
}
