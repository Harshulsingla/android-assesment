package com.example.booksviewer.domain.models;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class BookModel implements Serializable {

    private static final long serialVersionUID = 1L; // Optional: Add a serialVersionUID for versioning

    @SerializedName("id")
    private String bookId;

    @SerializedName("etag")
    private String etag;

    @SerializedName("selfLink")
    private String selfLink;

    @SerializedName("volumeInfo")
    private VolumeInfoModel volumeInfo;

    @SerializedName("saleInfo")
    private SaleInfoModel saleInfo;

    @SerializedName("searchInfo")
    private SearchInfoModel searchInfo;

    // New field for tracking if the book is saved
    private boolean isSaved;

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfoModel getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfoModel volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public SaleInfoModel getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(SaleInfoModel saleInfo) {
        this.saleInfo = saleInfo;
    }

    public SearchInfoModel getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfoModel searchInfo) {
        this.searchInfo = searchInfo;
    }

    // Getter and Setter for isSaved
    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
