package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SaleInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

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
