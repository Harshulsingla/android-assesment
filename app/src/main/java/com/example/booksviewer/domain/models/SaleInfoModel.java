package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;

public class SaleInfoModel {

    @SerializedName("saleability")
    private String saleability;

    @SerializedName("isEbook")
    private boolean isEbook;

    // Getters and Setters
    public String getSaleability() {
        return saleability;
    }

    public void setSaleability(String saleability) {
        this.saleability = saleability;
    }

    public boolean isEbook() {
        return isEbook;
    }

    public void setEbook(boolean ebook) {
        isEbook = ebook;
    }
}