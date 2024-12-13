package com.example.booksviewer.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;
import com.example.booksviewer.domain.relation.BookWithDetails;

import java.util.List;

@Dao
public interface BookDao {

    // Insert or update a single book entity and related entities
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBook(BookEntity bookEntity);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSaleInfo(SaleInfoEntity saleInfoEntity);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchInfo(SearchInfoEntity searchInfoEntity);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVolumeInfo(VolumeInfoEntity volumeInfoEntity);


    // Fetch all books with their related entities
    @Transaction
    @Query("SELECT * FROM tbl_books")
    List<BookWithDetails> getAllBooksWithDetails();

    // Fetch a single book with its related entities by book ID
    @Transaction
    @Query("SELECT * FROM tbl_books WHERE id = :bookId LIMIT 1")
    BookWithDetails getBookWithDetailsById(long bookId);

    @Transaction
    @Query("DELETE FROM tbl_books WHERE clm_book_id = :bookId")
    int deleteBookById(String bookId);
}
