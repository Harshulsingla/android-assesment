package com.example.booksviewer.ui.views.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.domain.relation.BookWithDetails;
import com.example.booksviewer.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavouriteViewModel extends ViewModel {

    private final DbService dbService;
    private final MutableLiveData<List<BookModel>> booksLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Flag to check if data has been fetched already
    private boolean isDataFetched = false;

    @Inject
    public FavouriteViewModel(DbService dbService) {
        this.dbService = dbService;
    }

    public LiveData<List<BookModel>> getBooksLiveData() {
        return booksLiveData;
    }

    // Add getter and setter for isDataFetched
    public boolean isDataFetched() {
        return isDataFetched;
    }

    public void setDataFetched(boolean isDataFetched) {
        this.isDataFetched = isDataFetched;
    }

    // Fetch saved books from the database
    public void fetchSavedBooks() {
        if (!isDataFetched) {
            executorService.execute(() -> {
                try {
                    List<BookWithDetails> bookWithDetailsList = dbService.getAllBooksWithDetails();
                    List<BookModel> bookModels = convertToBookModels(bookWithDetailsList);

                    if (!bookModels.isEmpty()) {
                        booksLiveData.postValue(bookModels); // Post the data to LiveData
                        isDataFetched = true; // Mark data as fetched
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Convert BookWithDetails entities to BookModel objects
    private List<BookModel> convertToBookModels(List<BookWithDetails> bookWithDetailsList) {
        List<BookModel> bookModels = new ArrayList<>();
        for (BookWithDetails details : bookWithDetailsList) {
            BookModel bookModel = new BookModel();
            bookModel.setBookId(details.bookEntity.getBookId());
            bookModel.setVolumeInfo(BookUtils.convertToVolumeInfoModel(details.volumeInfoEntity)); // Convert VolumeInfo
            bookModel.setSaleInfo(BookUtils.convertToSaleInfoModel(details.saleInfoEntity)); // Convert SaleInfo
            bookModel.setSearchInfo(BookUtils.convertToSearchInfoModel(details.searchInfoEntity)); // Convert SearchInfo
            bookModel.setSaved(true);
            bookModels.add(bookModel);
        }
        return bookModels;
    }

    // Delete book from the database
    public void deleteBook(BookModel book) {
        executorService.execute(() -> {
            try {
                // Assuming you have a method in DbService that deletes a book by its ID
                dbService.deleteBookById(book.getBookId());

                // Optionally, you can update the LiveData here to reflect the change in the UI
                List<BookModel> currentBooks = booksLiveData.getValue();
                if (currentBooks != null) {
                    currentBooks.remove(book); // Remove the book from the list
                    booksLiveData.postValue(currentBooks); // Post updated list to LiveData
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
