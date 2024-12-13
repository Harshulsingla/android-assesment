package com.example.booksviewer.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tbl_books",
        indices = {@Index(value = "clm_book_id", unique = true)}
)
public class BookEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "clm_book_id")
    private String bookId;

    @ColumnInfo(name = "clm_etag")
    private String etag;

    @ColumnInfo(name = "clm_selfLink")
    private String selfLink;


    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

}
