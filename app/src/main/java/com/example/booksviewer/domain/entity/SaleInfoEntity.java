package com.example.booksviewer.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.booksviewer.domain.entity.BookEntity;

@Entity(
        tableName = "tbl_sale_info",
        foreignKeys = @ForeignKey(
                entity = BookEntity.class,
                parentColumns = "id",
                childColumns = "clm_book_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class SaleInfoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "clm_book_id")
    private String bookId;

    @ColumnInfo(name = "clm_saleability")
    private String saleability;

    @ColumnInfo(name = "clm_isEbook")
    private boolean isEbook;

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

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
