package com.example.booksviewer.data.db;

import com.example.booksviewer.data.db.dao.BookDao;
import com.example.booksviewer.domain.relation.BookWithDetails;

import javax.inject.Inject;
import java.util.List;

public class DbService {
    private final BookDao bookDao;

    @Inject
    public DbService(RoomDbHelper db) {
        this.bookDao = db.bookDao();
    }

    // Fetch all books with their related entities
    public List<BookWithDetails> getAllBooksWithDetails() {
        return bookDao.getAllBooksWithDetails();
    }

    // Fetch a single book with its related entities by book ID
    public BookWithDetails getBookWithDetailsById(long bookId) {
        return bookDao.getBookWithDetailsById(bookId);
    }

    // Insert or update a single book with related entities
    public long insertBookWithDetails(BookWithDetails bookWithDetails) {
        // Insert each entity individually
        long bookId = bookDao.insertBook(bookWithDetails.bookEntity); // Insert BookEntity
        bookWithDetails.saleInfoEntity.setBookId(String.valueOf(bookId)); // Set bookId for SaleInfoEntity
        bookWithDetails.searchInfoEntity.setBookId(String.valueOf(bookId)); // Set bookId for SearchInfoEntity
        bookWithDetails.volumeInfoEntity.setBookId(String.valueOf(bookId)); // Set bookId for VolumeInfoEntity

        // Insert related entities
        bookDao.insertSaleInfo(bookWithDetails.saleInfoEntity);
        bookDao.insertSearchInfo(bookWithDetails.searchInfoEntity);
        bookDao.insertVolumeInfo(bookWithDetails.volumeInfoEntity);

        return bookId;
    }

    // Insert or update a list of books with related entities
    public List<Long> insertBooksWithDetails(List<BookWithDetails> booksWithDetails) {
        for (BookWithDetails bookWithDetails : booksWithDetails) {
            insertBookWithDetails(bookWithDetails); // Reuse the insert method for each book
        }
        return null; // You may return appropriate values if needed
    }


    public int deleteBookById(String bookId) {
        return bookDao.deleteBookById(bookId);
    }
}
