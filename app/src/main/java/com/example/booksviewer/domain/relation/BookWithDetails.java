package com.example.booksviewer.domain.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;

import java.util.List;

public class BookWithDetails {

    @Embedded
    public BookEntity bookEntity;

    @Relation(
            parentColumn = "clm_book_id",
            entity = SaleInfoEntity.class,
            entityColumn = "clm_book_id"
    )
    public SaleInfoEntity saleInfoEntity;

    @Relation(
            parentColumn = "clm_book_id",
            entity = SearchInfoEntity.class,
            entityColumn = "clm_book_id"
    )
    public SearchInfoEntity searchInfoEntity;

    @Relation(
            parentColumn = "clm_book_id",
            entity = VolumeInfoEntity.class,
            entityColumn = "clm_book_id"
    )
    public VolumeInfoEntity volumeInfoEntity;
}
