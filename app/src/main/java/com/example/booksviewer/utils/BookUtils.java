package com.example.booksviewer.utils;

import android.util.Log;

import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.domain.models.SaleInfoModel;
import com.example.booksviewer.domain.models.SearchInfoModel;
import com.example.booksviewer.domain.models.VolumeInfoModel;
import com.example.booksviewer.domain.relation.BookWithDetails;

public class BookUtils {

    private static final String TAG = "BookUtils";

    public static void saveBookToDb(DbService dbService, BookModel book) {
        if (dbService == null || book == null || book.getBookId() == null) {
            Log.e(TAG, "Invalid arguments: DbService or BookModel or BookId is null");
            return;
        }

        BookWithDetails bookWithDetails = mapBookToEntity(book);

        // Run database operation in a background thread
        new Thread(() -> {
            try {
                dbService.insertBookWithDetails(bookWithDetails);
                Log.d(TAG, "Book saved to DB: " + book.getBookId());
            } catch (Exception e) {
                Log.e(TAG, "Error saving book to DB", e);
            }
        }).start();
    }

    public static void unSaveBook(DbService dbService, BookModel book) {
        if (dbService == null || book == null || book.getBookId() == null) {
            Log.e(TAG, "Invalid arguments: DbService or BookModel or BookId is null");
            return;
        }

        // Run database operation in a background thread
        new Thread(() -> {
            try {
                long result = dbService.deleteBookById(book.getBookId());
                if (result > 0) {
                    Log.d(TAG, "Book deleted successfully with ID: " + book.getBookId());
                } else {
                    Log.e(TAG, "Failed to delete book with ID: " + book.getBookId());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting book from DB", e);
            }
        }).start();
    }

    public static BookWithDetails mapBookToEntity(BookModel book) {
        if (book == null) {
            Log.e(TAG, "BookModel is null");
            return new BookWithDetails();
        }

        BookWithDetails bookWithDetails = new BookWithDetails();
        bookWithDetails.bookEntity = mapBookEntity(book);
        bookWithDetails.saleInfoEntity = mapSaleInfo(book.getSaleInfo());
        bookWithDetails.searchInfoEntity = mapSearchInfo(book.getSearchInfo());
        bookWithDetails.volumeInfoEntity = mapVolumeInfo(book.getVolumeInfo());
        return bookWithDetails;
    }

    private static BookEntity mapBookEntity(BookModel book) {
        if (book == null) {
            Log.e(TAG, "BookModel is null in mapBookEntity");
            return new BookEntity();
        }

        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookId(book.getBookId());
        bookEntity.setSelfLink(book.getSelfLink() != null ? book.getSelfLink() : "");
        bookEntity.setEtag(book.getEtag() != null ? book.getEtag() : "");
        return bookEntity;
    }

    private static SaleInfoEntity mapSaleInfo(SaleInfoModel saleInfo) {
        if (saleInfo == null) {
            Log.w(TAG, "SaleInfoModel is null");
            return new SaleInfoEntity();
        }

        SaleInfoEntity saleInfoEntity = new SaleInfoEntity();
        saleInfoEntity.setEbook(saleInfo.isEbook());
        saleInfoEntity.setSaleability(saleInfo.getSaleability() != null ? saleInfo.getSaleability() : "");
        return saleInfoEntity;
    }

    private static SearchInfoEntity mapSearchInfo(SearchInfoModel searchInfo) {
        if (searchInfo == null) {
            Log.w(TAG, "SearchInfoModel is null");
            return new SearchInfoEntity();
        }

        SearchInfoEntity searchInfoEntity = new SearchInfoEntity();
        searchInfoEntity.setTextSnippet(searchInfo.getTextSnippet() != null ? searchInfo.getTextSnippet() : "");
        return searchInfoEntity;
    }

    private static VolumeInfoEntity mapVolumeInfo(VolumeInfoModel volumeInfo) {
        if (volumeInfo == null) {
            Log.w(TAG, "VolumeInfoModel is null");
            return new VolumeInfoEntity();
        }

        VolumeInfoEntity volumeInfoEntity = new VolumeInfoEntity();
        volumeInfoEntity.setAuthors(volumeInfo.getAuthors() != null ? volumeInfo.getAuthors().toString() : "");
        volumeInfoEntity.setCategories(volumeInfo.getCategories() != null ? volumeInfo.getCategories().toString() : "");
        volumeInfoEntity.setDescription(volumeInfo.getDescription() != null ? volumeInfo.getDescription() : "");
        volumeInfoEntity.setPageCount(volumeInfo.getPageCount());
        volumeInfoEntity.setImageLinks(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().toString() : "");
        volumeInfoEntity.setPreviewLink(volumeInfo.getPreviewLink() != null ? volumeInfo.getPreviewLink() : "");
        volumeInfoEntity.setPublishedDate(volumeInfo.getPublishedDate() != null ? volumeInfo.getPublishedDate() : "");
        volumeInfoEntity.setTitle(volumeInfo.getTitle() != null ? volumeInfo.getTitle() : "");
        volumeInfoEntity.setPublisher(volumeInfo.getPublisher() != null ? volumeInfo.getPublisher() : "");
        return volumeInfoEntity;
    }
}
