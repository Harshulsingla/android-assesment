package com.example.booksviewer.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(
        tableName = "tbl_volume_info",
        foreignKeys = @ForeignKey(
                entity = BookEntity.class,
                parentColumns = "id",
                childColumns = "clm_book_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class VolumeInfoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "clm_book_id")
    private String bookId;

    @ColumnInfo(name = "clm_title")
    private String title;

    @ColumnInfo(name = "clm_authors")
    private String authors;

    @ColumnInfo(name = "clm_publisher")
    private String publisher;

    @ColumnInfo(name = "clm_publishedDate")
    private String publishedDate;

    @ColumnInfo(name = "clm_description")
    private String description;

    @ColumnInfo(name = "clm_imageLinks")
    private String imageLinks;

    @ColumnInfo(name = "clm_categories")
    private String categories;

    @ColumnInfo(name = "clm_pageCount")
    private int pageCount;

    @ColumnInfo(name = "clm_previewLink")  // Adding the preview link
    private String previewLink;

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(String imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }
}
