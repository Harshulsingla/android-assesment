package com.example.dictionary.db.dao;

import androidx.room.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import androidx.room.Transaction;

import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.WordEntity;

import java.util.List;

@Dao
public interface WordDao {

    // Insert a new word and return the row ID of the inserted word
    @Insert
    long insertWord(WordEntity wordEntity);

    // Insert a new part of speech and return the row ID of the inserted part of speech
    @Insert
    long insertPartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Update an existing word and return the number of rows affected
    @Update
    int updateWord(WordEntity wordEntity);

    // Update an existing part of speech and return the number of rows affected
    @Update
    int updatePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Delete a word by its ID and return the number of rows affected
    @Delete
    int deleteWord(WordEntity wordEntity);

    // Delete a part of speech by its ID and return the number of rows affected
    @Delete
    int deletePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Get all words
    @Query("SELECT * FROM tbl_words")
    List<WordEntity> getAllWords();

    // Get a word by its ID
    @Query("SELECT * FROM tbl_words WHERE clm_id = :wordId")
    WordEntity getWordById(int wordId);

    // Get all parts of speech for a specific word (based on word ID)
    @Query("SELECT * FROM tbl_parts_of_speech WHERE clm_word_id = :wordId")
    List<PartOfSpeechEntity> getPartsOfSpeechByWordId(int wordId);

    // Get a specific part of speech by its ID
    @Query("SELECT * FROM tbl_parts_of_speech WHERE clm_id = :partOfSpeechId")
    PartOfSpeechEntity getPartOfSpeechById(int partOfSpeechId);

    // Get all words that match a specific title
    @Query("SELECT * FROM tbl_words WHERE clm_title LIKE :title")
    List<WordEntity> getWordsByTitle(String title);

    // Transactional method to insert a word with its parts of speech in one go and return the row IDs
    @Transaction
    default long insertWordWithPartsOfSpeech(WordEntity wordEntity, List<PartOfSpeechEntity> partOfSpeechEntities) {
        long wordId = insertWord(wordEntity); // Insert word and get the word ID
        for (PartOfSpeechEntity partOfSpeechEntity : partOfSpeechEntities) {
            partOfSpeechEntity.setClmWordId((int) wordId); // Set the word ID for part of speech
            insertPartOfSpeech(partOfSpeechEntity); // Insert each part of speech
        }
        return wordId; // Return the word ID
    }

    // Delete all parts of speech for a specific word and return the number of rows affected
    @Query("DELETE FROM tbl_parts_of_speech WHERE clm_word_id = :wordId")
    int deletePartsOfSpeechByWordId(int wordId);
}

