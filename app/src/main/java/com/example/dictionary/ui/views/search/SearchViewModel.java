package com.example.dictionary.ui.views.search;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dictionary.db.DbService;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.mappers.WordMapper;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.domain.repository.Repository;
import com.example.dictionary.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Response;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    private MutableLiveData<List<WordDetailEntity>> wordList = new MutableLiveData<>(new ArrayList<>());

    private final SingleLiveEvent<Long> savedWordResult = new SingleLiveEvent<>();
    public Boolean initialRenderFlag = true;
    Repository repository;

    DbService dbService;

    @Inject
    public SearchViewModel(Repository repository, DbService dbService){
        super();
        this.repository = repository;
        this.dbService = dbService;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public MutableLiveData<List<WordDetailEntity>> getWordList() {
        return wordList;
    }

    public SingleLiveEvent<Long> getSavedWordResult() {
        return savedWordResult;
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

                        List<WordDetailEntity> responseWordDetailEntities = WordMapper.mapModelListToWordDetailEntity(response.body());
                        wordList.setValue(responseWordDetailEntities);

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


    public void saveWordDetails(WordDetailEntity wordDetailEntity) {
        executorService.submit(() -> {
            try {
                // Extract entities
                WordEntity wordEntity = wordDetailEntity.getWordEntity();
                List<PartOfSpeechEntity> partOfSpeechEntities = wordDetailEntity.getPartOfSpeechEntities();
                SynonymAntonymEntity synonymAntonymEntity = wordDetailEntity.getSynonymAntonymEntity();

                // Check if a word with the same title already exists
                List<WordEntity> existingWords = dbService.getWordsByTitle(wordEntity.getClmTitle());
                boolean shouldSave = true;

                if (existingWords != null && !existingWords.isEmpty()) {
                    for (WordEntity existingWord : existingWords) {
                        List<PartOfSpeechEntity> existingPartsOfSpeech = dbService.getPartsOfSpeechByWordId(existingWord.getClmId());

                        // If parts of speech count matches and content matches, skip saving
                        if (existingPartsOfSpeech.size() == partOfSpeechEntities.size()) {
                            shouldSave = false;
                            break;
                        }
                    }
                }

                if (shouldSave) {
                    // Save the word along with its parts of speech and synonyms/antonyms
                    long savedWordId = dbService.insertWordWithDetails(wordEntity, partOfSpeechEntities, synonymAntonymEntity);
                    savedWordResult.postValue(savedWordId); // Use postValue for LiveData updates in a background thread
                    Log.d(TAG, "Word saved: " + wordEntity.getClmTitle());
                } else {
                    // Skip saving
                    savedWordResult.postValue(-1L); // Use postValue to indicate no save
                    Log.d(TAG, "Word with same title and parts of speech exists, skipping save: " + wordEntity.getClmTitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
                savedWordResult.postValue(-1L); // Use postValue to indicate failure
                Log.e(TAG, "Error saving word details", e);
            }
        });
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shutdown the executor service to release resources
    }



}
