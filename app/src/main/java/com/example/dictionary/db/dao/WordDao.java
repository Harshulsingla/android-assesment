package com.example.dictionary.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import androidx.room.Transaction;

import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WordDao {

    // Insert a new word and return the row ID of the inserted word
    @Insert
    long insertWord(WordEntity wordEntity);

    // Insert a new part of speech and return the row ID of the inserted part of speech
    @Insert
    long insertPartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Insert a new synonym/antonym and return the row ID
    @Insert
    long insertSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

    // Update an existing word and return the number of rows affected
    @Update
    int updateWord(WordEntity wordEntity);

    // Update an existing part of speech and return the number of rows affected
    @Update
    int updatePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Update an existing synonym/antonym and return the number of rows affected
    @Update
    int updateSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

    // Delete a word by its ID and return the number of rows affected
    @Delete
    int deleteWord(WordEntity wordEntity);

    // Delete a part of speech by its ID and return the number of rows affected
    @Delete
    int deletePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Delete a synonym/antonym by its ID and return the number of rows affected
    @Delete
    int deleteSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

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

    // Get all synonyms/antonyms for a specific word
    @Query("SELECT clm_id, clm_word_id, clm_synonyms, clm_antonyms FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    SynonymAntonymEntity getSynonymsAntonymsByWordId(int wordId);

    // Get all synonyms for a specific word
    @Query("SELECT clm_synonyms FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    String getSynonymsByWordId(int wordId); // Now returns concatenated synonyms as a string

    // Get all antonyms for a specific word
    @Query("SELECT clm_antonyms FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    String getAntonymsByWordId(int wordId); // Now returns concatenated antonyms as a string

    // Get all words that match a specific title
    @Query("SELECT * FROM tbl_words WHERE clm_title LIKE :title")
    List<WordEntity> getWordsByTitle(String title);

    // Delete all parts of speech for a specific word and return the number of rows affected
    @Query("DELETE FROM tbl_parts_of_speech WHERE clm_word_id = :wordId")
    int deletePartsOfSpeechByWordId(int wordId);

    // Delete all synonyms/antonyms for a specific word and return the number of rows affected
    @Query("DELETE FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    int deleteSynonymsAntonymsByWordId(int wordId);

    @Query("SELECT * FROM tbl_words WHERE clm_title = :title")
    List<WordEntity> getWordByTitle(String title);

    @Query("SELECT * FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    SynonymAntonymEntity getSynonymAntonymByWordId(int wordId);

    // Delete all words
    @Query("DELETE FROM tbl_words")
    int deleteAllWords();

    @Transaction
    default List<WordDetailEntity> getAllWordDetails() {
        // Fetch all WordEntity records
        List<WordEntity> wordEntities = getAllWords();
        List<WordDetailEntity> wordDetailEntities = new ArrayList<>();

        // Iterate through all words and fetch their corresponding details
        for (WordEntity wordEntity : wordEntities) {
            int wordId = wordEntity.getClmId();

            // Fetch parts of speech for the current word
            List<PartOfSpeechEntity> partOfSpeechEntities = getPartsOfSpeechByWordId(wordId);

            // Fetch synonyms and antonyms for the current word
            SynonymAntonymEntity synonymAntonymEntity = getSynonymAntonymByWordId(wordId);

            // Construct the WordDetailEntity
            WordDetailEntity wordDetailEntity = new WordDetailEntity(
                    wordEntity,
                    partOfSpeechEntities,
                    synonymAntonymEntity
            );

            // Add to the result list
            wordDetailEntities.add(wordDetailEntity);
        }

        return wordDetailEntities; // Return the list of WordDetailEntity
    }



    // Transactional method to insert a word with its parts of speech and synonyms/antonyms in one go
    @Transaction
    default long insertWordWithDetails(
            WordEntity wordEntity,
            List<PartOfSpeechEntity> partOfSpeechEntities,
            SynonymAntonymEntity synonymAntonymEntity) {

        if (wordEntity == null) {
            throw new IllegalArgumentException("WordEntity cannot be null");
        }

        long wordId = insertWord(wordEntity); // Insert word and get the word ID

        // Check and insert parts of speech
        if (partOfSpeechEntities != null && !partOfSpeechEntities.isEmpty()) {
            for (PartOfSpeechEntity partOfSpeechEntity : partOfSpeechEntities) {
                if (partOfSpeechEntity != null) { // Null-check for individual entities
                    partOfSpeechEntity.setClmWordId((int) wordId);
                    insertPartOfSpeech(partOfSpeechEntity);
                }
            }
        }

        // Check and insert synonyms and antonyms
        if (synonymAntonymEntity != null) {
            synonymAntonymEntity.setClmWordId((int) wordId);
            insertSynonymAntonym(synonymAntonymEntity);
        }

        return wordId; // Return the word ID
    }


    // Transactional method to delete a word and its related entities
    @Transaction
    default int deleteWordWithDetails(int wordId) {
        int rowsDeleted = deletePartsOfSpeechByWordId(wordId); // Delete parts of speech
        rowsDeleted += deleteSynonymsAntonymsByWordId(wordId); // Delete synonyms and antonyms
        WordEntity wordEntity = getWordById(wordId); // Fetch the word entity
        if (wordEntity != null) {
            rowsDeleted += deleteWord(wordEntity); // Delete the word
        }
        return rowsDeleted; // Return the total number of rows deleted
    }
}
