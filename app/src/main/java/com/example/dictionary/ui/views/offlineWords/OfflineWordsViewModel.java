package com.example.dictionary.ui.views.offlineWords;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dictionary.db.DbService;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OfflineWordsViewModel extends ViewModel {

    private static final String TAG = "OfflineWordsViewModel";

    private final DbService dbService;

    private final MutableLiveData<List<WordDetailEntity>> offlineWordList = new MutableLiveData<>(new ArrayList<>());

    private final SingleLiveEvent<Pair<Boolean, Integer>> deletedWordResult = new SingleLiveEvent<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Inject
    public OfflineWordsViewModel(DbService dbService) {
        this.dbService = dbService;
    }

    public MutableLiveData<List<WordDetailEntity>> getOfflineWordsList() {
        return offlineWordList;
    }

    public SingleLiveEvent<Pair<Boolean, Integer>> getDeletedWordResult() {
        return deletedWordResult;
    }

    public void loadOfflineWords() {
        executorService.submit(() -> {
            try {
                // Perform the database operation in the background
                List<WordDetailEntity> wordDetailEntitiesList = dbService.getAllWordDetails();

                // Update the LiveData on the main thread
                mainHandler.post(() -> {
                    if (wordDetailEntitiesList != null && !wordDetailEntitiesList.isEmpty()) {
                        offlineWordList.setValue(wordDetailEntitiesList);
                    } else {
                        offlineWordList.setValue(new ArrayList<>());
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading offline words", e);
                // In case of error, update the LiveData on the main thread
                mainHandler.post(() -> offlineWordList.setValue(new ArrayList<>()));
            }
        });
    }



    public void deleteWord(WordDetailEntity wordDetailEntity) {
        executorService.submit(() -> {
            try {
                // Perform the deletion operation directly on the main thread
                int deleteWordResult = dbService.deleteWordWithDetails(wordDetailEntity);

                // Update the LiveData and result on the main thread
                if (deleteWordResult > 0) {
                    // Update the list if the word was deleted successfully
                    if (offlineWordList.getValue() != null) {
                        List<WordDetailEntity> currentList = new ArrayList<>(offlineWordList.getValue()); // Create a new list
                        int position = currentList.indexOf(wordDetailEntity);
                        if (position != -1) {
                            currentList.remove(position);
                            offlineWordList.postValue(currentList);
                            //offlineWordList.getValue().remove(position); // Update the LiveData
                            deletedWordResult.postValue(new Pair<>(true, position));
                        }
                    }
                } else {
                    // Notify that the deletion failed
                    deletedWordResult.postValue(new Pair<>(false, -1));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting word", e);
                // In case of error, post failure result to the main thread
                deletedWordResult.postValue(new Pair<>(false, -1));
            }
        });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shutdown executor to release resources
    }
}

