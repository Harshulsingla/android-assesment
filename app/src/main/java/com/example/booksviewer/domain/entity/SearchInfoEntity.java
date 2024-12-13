package com.example.booksviewer.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.booksviewer.domain.entity.BookEntity;

@Entity(
        tableName = "tbl_search_info",
        foreignKeys = @ForeignKey(
                entity = BookEntity.class,
                parentColumns = "id",
                childColumns = "clm_book_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class SearchInfoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "clm_book_id")
    private String bookId;

    @ColumnInfo(name = "clm_textSnippet")
    private String textSnippet;

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
}
