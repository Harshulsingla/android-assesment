package com.example.booksviewer.ui.views.bookDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.utils.BookUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BookDetailsViewModel extends ViewModel {
    private final DbService dbService;
    private final MutableLiveData<Boolean> isBookSaved = new MutableLiveData<>();
    private final ExecutorService executorService;

    @Inject
    public BookDetailsViewModel(DbService dbService) {
        this.dbService = dbService;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Boolean> getIsBookSaved() {
        return isBookSaved;
    }

    public void checkIfBookIsSaved(String bookId) {
        executorService.execute(() -> {
            boolean exists = dbService.getBookWithDetailsById(bookId) != null;
            isBookSaved.postValue(exists);
        });
    }

    public void toggleBookSaveState(boolean isCurrentlySaved, BookModel book) {
        executorService.execute(() -> {
            if (isCurrentlySaved) {
                // Unsaving the book
                BookUtils.unSaveBook(dbService, book);
            } else {
                // Saving the book
                BookUtils.saveBookToDb(dbService, book);
            }
            // After the operation, update the UI to reflect the new state
            isBookSaved.postValue(!isCurrentlySaved); // Toggle the state
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
