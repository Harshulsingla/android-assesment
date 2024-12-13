package com.example.booksviewer.utils;

import android.util.Log;

import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.domain.models.ImageLinksModel;
import com.example.booksviewer.domain.models.SaleInfoModel;
import com.example.booksviewer.domain.models.SearchInfoModel;
import com.example.booksviewer.domain.models.VolumeInfoModel;
import com.example.booksviewer.domain.relation.BookWithDetails;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                book.setSaved(true);
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
                    book.setSaved(false);
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

    public static BookModel mapBookWithDetailsToModel(BookWithDetails bookWithDetails) {
        if (bookWithDetails == null) {
            Log.w("BookUtils", "BookWithDetails is null, returning an empty BookModel");
            return new BookModel();
        }

        BookModel bookModel = new BookModel();
        try {
            BookEntity bookEntity = bookWithDetails.bookEntity;
            SaleInfoEntity saleInfoEntity = bookWithDetails.saleInfoEntity;
            SearchInfoEntity searchInfoEntity = bookWithDetails.searchInfoEntity;
            VolumeInfoEntity volumeInfoEntity = bookWithDetails.volumeInfoEntity;

            // Map BookEntity
            if (bookEntity != null) {
                bookModel.setBookId(safeString(bookEntity.getBookId()));
                bookModel.setSelfLink(safeString(bookEntity.getSelfLink()));
                bookModel.setEtag(safeString(bookEntity.getEtag()));
            } else {
                Log.w("BookUtils", "BookEntity is null in BookWithDetails");
            }

            // Map SaleInfoEntity
            if (saleInfoEntity != null) {
                SaleInfoModel saleInfo = new SaleInfoModel();
                saleInfo.setEbook(saleInfoEntity.isEbook());
                saleInfo.setSaleability(safeString(saleInfoEntity.getSaleability()));
                bookModel.setSaleInfo(saleInfo);
            } else {
                Log.w("BookUtils", "SaleInfoEntity is null in BookWithDetails");
            }

            // Map SearchInfoEntity
            if (searchInfoEntity != null) {
                SearchInfoModel searchInfo = new SearchInfoModel();
                searchInfo.setTextSnippet(safeString(searchInfoEntity.getTextSnippet()));
                bookModel.setSearchInfo(searchInfo);
            } else {
                Log.w("BookUtils", "SearchInfoEntity is null in BookWithDetails");
            }

            // Map VolumeInfoEntity
            if (volumeInfoEntity != null) {
                VolumeInfoModel volumeInfo = new VolumeInfoModel();
                volumeInfo.setAuthors(parseStringToList(safeString(volumeInfoEntity.getAuthors())));
                volumeInfo.setCategories(parseStringToList(safeString(volumeInfoEntity.getCategories())));
                volumeInfo.setDescription(safeString(volumeInfoEntity.getDescription()));
                volumeInfo.setPageCount(volumeInfoEntity.getPageCount());
                volumeInfo.setPreviewLink(safeString(volumeInfoEntity.getPreviewLink()));
                volumeInfo.setPublishedDate(safeString(volumeInfoEntity.getPublishedDate()));
                volumeInfo.setTitle(safeString(volumeInfoEntity.getTitle()));
                volumeInfo.setPublisher(safeString(volumeInfoEntity.getPublisher()));
                bookModel.setVolumeInfo(volumeInfo);

                String imageLinksJson = volumeInfoEntity.getImageLinks();
                if (imageLinksJson != null && !imageLinksJson.isEmpty()) {
                    try {
                        ImageLinksModel imageLinks = parseImageLinks(imageLinksJson);
                        volumeInfo.setImageLinks(imageLinks);
                    } catch (Exception e) {
                        Log.e("BookUtils", "Failed to parse image links", e);
                    }
                }

            } else {
                Log.w("BookUtils", "VolumeInfoEntity is null in BookWithDetails");
            }

        } catch (Exception e) {
            Log.e("BookUtils", "Error mapping BookWithDetails to BookModel", e);
        }

        return bookModel;
    }

    private static String safeString(String value) {
        return value != null ? value : "";
    }

    private static ImageLinksModel parseImageLinks(String json) {
        // Assuming you are using Gson or Jackson for JSON parsing
        Gson gson = new Gson();
        return gson.fromJson(json, ImageLinksModel.class);
    }

    private static List<String> parseStringToList(String data) {
        if (data == null || data.isEmpty()) {
            Log.w("BookUtils", "String is null or empty, returning an empty list");
            return new ArrayList<>();
        }
        try {
            return Arrays.asList(data.split(","));
        } catch (Exception e) {
            Log.e("BookUtils", "Error parsing string to list: " + data, e);
            return new ArrayList<>();
        }
    }


    // Convert VolumeInfoEntity to VolumeInfoModel
    public static VolumeInfoModel convertToVolumeInfoModel(VolumeInfoEntity volumeInfoEntity) {
        if (volumeInfoEntity == null) {
            return new VolumeInfoModel(); // Return an empty model if entity is null
        }

        VolumeInfoModel volumeInfoModel = new VolumeInfoModel();
        volumeInfoModel.setAuthors(parseStringToList(volumeInfoEntity.getAuthors()));
        volumeInfoModel.setCategories(parseStringToList(volumeInfoEntity.getCategories()));
        volumeInfoModel.setDescription(volumeInfoEntity.getDescription());
        volumeInfoModel.setPageCount(volumeInfoEntity.getPageCount());
        volumeInfoModel.setPreviewLink(volumeInfoEntity.getPreviewLink());
        volumeInfoModel.setPublishedDate(volumeInfoEntity.getPublishedDate());
        volumeInfoModel.setTitle(volumeInfoEntity.getTitle());
        volumeInfoModel.setPublisher(volumeInfoEntity.getPublisher());

        // Get the image links as a String from the entity
        String imageLinksJson = volumeInfoEntity.getImageLinks();
        if (imageLinksJson != null && !imageLinksJson.isEmpty()) {
            try {
                // Extract thumbnail and smallThumbnail manually
                String thumbnail = extractImageLink(imageLinksJson, "thumbnail");
                String smallThumbnail = extractImageLink(imageLinksJson, "smallThumbnail");

                // Create an ImageLinksModel and set the extracted values
                ImageLinksModel imageLinks = new ImageLinksModel();
                imageLinks.setThumbnail(thumbnail);
                imageLinks.setSmallThumbnail(smallThumbnail);

                // Set the parsed image links in the VolumeInfoModel
                volumeInfoModel.setImageLinks(imageLinks);
            } catch (Exception e) {
                Log.e("BookUtils", "Failed to parse image links", e);
            }
        }

        return volumeInfoModel;
    }

    private static String extractImageLink(String imageLinksJson, String key) {
        // Search for the key in the string and extract the value between single quotes
        String pattern = key + "='(.*?)'";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(imageLinksJson);

        if (m.find()) {
            return m.group(1); // Return the URL
        }

        return null; // Return null if the key was not found
    }


    // Convert SaleInfoEntity to SaleInfoModel
    public static SaleInfoModel convertToSaleInfoModel(SaleInfoEntity saleInfoEntity) {
        if (saleInfoEntity == null) {
            return new SaleInfoModel(); // Return an empty model if entity is null
        }

        SaleInfoModel saleInfoModel = new SaleInfoModel();
        saleInfoModel.setEbook(saleInfoEntity.isEbook());
        saleInfoModel.setSaleability(saleInfoEntity.getSaleability());
        return saleInfoModel;
    }

    // Convert SearchInfoEntity to SearchInfoModel
    public static SearchInfoModel convertToSearchInfoModel(SearchInfoEntity searchInfoEntity) {
        if (searchInfoEntity == null) {
            return new SearchInfoModel(); // Return an empty model if entity is null
        }

        SearchInfoModel searchInfoModel = new SearchInfoModel();
        searchInfoModel.setTextSnippet(searchInfoEntity.getTextSnippet());
        return searchInfoModel;
    }

}


