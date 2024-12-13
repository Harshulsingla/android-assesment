package com.example.booksviewer.domain.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ImageLinksModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("smallThumbnail")
    private String smallThumbnail;

    @SerializedName("thumbnail")
    private String thumbnail;

    // Getters and Setters
    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "ImageLinksModel{" +
                "thumbnail='" + thumbnail + '\'' +
                ", smallThumbnail='" + smallThumbnail + '\'' +
                // Add other fields as necessary
                '}';
    }
}
