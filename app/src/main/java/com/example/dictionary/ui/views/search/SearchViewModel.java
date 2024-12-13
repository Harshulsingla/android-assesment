package com.example.dictionary.ui.views.search;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.domain.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Response;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<WordModel>> wordList = new MutableLiveData<>(new ArrayList<>());
    public Boolean initialRenderFlag = true;

//    ApiClient apiClient = new ApiClient();
    Repository repository;

    @Inject
    public SearchViewModel(Repository repository){
        super();
        this.repository = repository;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public MutableLiveData<List<WordModel>> getWordList() {
        return wordList;
    }

    public Boolean getInitialRenderFlag() {
        return initialRenderFlag;
    }


    public void getWordDetails(String word) {

        executorService.submit(() -> {
            try {
                Response<List<WordModel>> response = repository.fetchWordDetail(word);

                // Update the initial render flag
                initialRenderFlag = false;

                // Post results back to the main thread
                mainHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        wordList.setValue(response.body());
                    } else {
                        wordList.setValue(null);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions gracefully
                mainHandler.post(() -> wordList.setValue(null));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shutdown the executor service to release resources
    }


    }
