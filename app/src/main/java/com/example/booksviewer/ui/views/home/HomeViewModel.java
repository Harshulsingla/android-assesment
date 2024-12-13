package com.example.booksviewer.ui.views.home;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.data.repository.Repository;
import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.domain.models.BookResponseModel;
import com.example.booksviewer.domain.models.SaleInfoModel;
import com.example.booksviewer.domain.models.SearchInfoModel;
import com.example.booksviewer.domain.models.VolumeInfoModel;
import com.example.booksviewer.domain.relation.BookWithDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Response;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final Repository repository;
    private final DbService dbService;
    private final MutableLiveData<List<BookModel>> books = new MutableLiveData<>();
    private final MutableLiveData<List<String>> searchHistory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String currentQuery;
    private int currentPage = 0;

    @Inject
    public HomeViewModel(Repository repository, DbService dbService) {
        this.repository = repository;
        this.dbService = dbService;
        searchHistory.setValue(repository.getSearchHistory());
    }

    public void prepareApiCall(String query, int startIndex, int maxResults) {
        fetchBooks(query, startIndex, maxResults);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private void fetchBooks(String query, int startIndex, int maxResults) {
        if (!query.equals(currentQuery)) {
            currentQuery = query;
            books.setValue(new ArrayList<>()); // Clear the list for a new query
        }

        isLoading.setValue(true);

        executorService.submit(() -> {
            try {
                updateSearchHistory(query);
                List<BookModel> newBooks = fetchBooksFromApi(query, startIndex, maxResults);

                if (newBooks != null && !newBooks.isEmpty()) {
                    books.postValue(newBooks);
                } else {
                    Log.e("HomeViewModel", "Error: No books found");
                    books.postValue(null);
                }
            } catch (IOException e) {
                Log.e("HomeViewModel", "Error fetching book details", e);
                books.postValue(null);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    private void updateSearchHistory(String query) {
        List<String> currentHistory = repository.getSearchHistory();
        if (!currentHistory.contains(query)) {
            repository.saveSearchHistory(query);
            searchHistory.postValue(repository.getSearchHistory());
        }
    }

    private List<BookModel> fetchBooksFromApi(String query, int startIndex, int maxResults) throws IOException {
        List<BookModel> books = new ArrayList<>();
        Response<BookResponseModel> response = repository.getBookDetails(query, startIndex, maxResults);

        if (response.isSuccessful() && response.body() != null) {
            List<BookModel> newBooks = response.body().getItems();
            List<String> existingBookIds = getExistingBookIds();
            for (BookModel book : newBooks) {
                book.setSaved(existingBookIds.contains(book.getBookId()));
            }
            books.addAll(newBooks);
        } else {
            Log.e("HomeViewModel", "Error: " + response.code() + " " + response.message());
        }
        return books;
    }

    private List<String> getExistingBookIds() {
        List<BookWithDetails> existingBooks = dbService.getAllBooksWithDetails();
        List<String> existingBookIds = new ArrayList<>();
        for (BookWithDetails bookWithDetails : existingBooks) {
            existingBookIds.add(bookWithDetails.bookEntity.getBookId());
        }
        return existingBookIds;
    }

    public LiveData<List<BookModel>> getBooks() {
        return books;
    }

    public LiveData<List<String>> getSearchHistory() {
        return searchHistory;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }

    public void saveBookToDb(BookModel book) {
        if (book == null || book.getBookId() == null) {
            Log.e("saveBookToDb", "Invalid book");
            return;
        }

        BookWithDetails bookWithDetails = mapBookToEntity(book);
        executorService.submit(() -> {
            try {
                dbService.insertBookWithDetails(bookWithDetails);
            } catch (Exception e) {
                Log.e("saveBookToDb", "Error saving book to DB", e);
            }
        });
    }

    private BookWithDetails mapBookToEntity(BookModel book) {
        BookWithDetails bookWithDetails = new BookWithDetails();
        bookWithDetails.bookEntity = mapBookEntity(book);
        bookWithDetails.saleInfoEntity = mapSaleInfo(book.getSaleInfo());
        bookWithDetails.searchInfoEntity = mapSearchInfo(book.getSearchInfo());
        bookWithDetails.volumeInfoEntity = mapVolumeInfo(book.getVolumeInfo());
        return bookWithDetails;
    }

    private BookEntity mapBookEntity(BookModel book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookId(book.getBookId());
        bookEntity.setSelfLink(book.getSelfLink());
        bookEntity.setEtag(book.getEtag());
        return bookEntity;
    }

    private SaleInfoEntity mapSaleInfo(SaleInfoModel saleInfo) {
        if (saleInfo == null) return new SaleInfoEntity();
        SaleInfoEntity saleInfoEntity = new SaleInfoEntity();
        saleInfoEntity.setEbook(saleInfo.isEbook());
        saleInfoEntity.setSaleability(saleInfo.getSaleability());
        return saleInfoEntity;
    }

    private SearchInfoEntity mapSearchInfo(SearchInfoModel searchInfo) {
        if (searchInfo == null || searchInfo.getTextSnippet() == null) return new SearchInfoEntity();
        SearchInfoEntity searchInfoEntity = new SearchInfoEntity();
        searchInfoEntity.setTextSnippet(searchInfo.getTextSnippet());
        return searchInfoEntity;
    }

    private VolumeInfoEntity mapVolumeInfo(VolumeInfoModel volumeInfo) {
        if (volumeInfo == null) return new VolumeInfoEntity();
        VolumeInfoEntity volumeInfoEntity = new VolumeInfoEntity();
        volumeInfoEntity.setAuthors(volumeInfo.getAuthors().toString());
        volumeInfoEntity.setCategories(volumeInfo.getCategories().toString());
        volumeInfoEntity.setDescription(volumeInfo.getDescription());
        volumeInfoEntity.setPageCount(volumeInfo.getPageCount());
        if(volumeInfo.getImageLinks() != null){
            volumeInfoEntity.setImageLinks(volumeInfo.getImageLinks().toString());
        }
        volumeInfoEntity.setPreviewLink(volumeInfo.getPreviewLink());
        volumeInfoEntity.setPublishedDate(volumeInfo.getPublishedDate());
        volumeInfoEntity.setTitle(volumeInfo.getTitle());
        volumeInfoEntity.setPublisher(volumeInfo.getPublisher());
        return volumeInfoEntity;
    }

    public void unSaveBook(BookModel book) {
        if (book == null || book.getBookId() == null) {
            Log.e("unSaveBook", "Invalid book or bookId");
            return;
        }

        executorService.submit(() -> {
            try {
                long result = dbService.deleteBookById(book.getBookId());
                if (result > 0) {
                    Log.d("unSaveBook", "Book deleted successfully with ID: " + book.getBookId());
                } else {
                    Log.e("unSaveBook", "Failed to delete book with ID: " + book.getBookId());
                }
            } catch (Exception e) {
                Log.e("unSaveBook", "Error deleting book from DB", e);
            }
        });
    }
}
