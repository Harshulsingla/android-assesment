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

    public void toggleBookSaveState(boolean isCurrentlySaved, BookModel book) {
        executorService.execute(() -> {
            if (isCurrentlySaved) {
                BookUtils.unSaveBook(dbService, book);
            } else {
                BookUtils.saveBookToDb(dbService, book);
            }
            isBookSaved.postValue(!isCurrentlySaved);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}