package com.example.dictionary.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import androidx.room.Transaction;

import com.example.dictionary.domain.entity.SynonymAntonymEntity;

import java.util.List;

@Dao
public interface SynonymAntonymDao {

    // Insert a new synonym/antonym and return the row ID
    @Insert
    long insertSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

    // Update an existing synonym/antonym and return the number of rows affected
    @Update
    int updateSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

    // Delete a synonym/antonym and return the number of rows affected
    @Delete
    int deleteSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity);

    // Get all synonyms and antonyms for a specific word
    @Query("SELECT * FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    List<SynonymAntonymEntity> getSynonymsAntonymsByWordId(int wordId);

    // Get all synonyms for a specific word
    @Query("SELECT clm_synonyms FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    String getSynonymsByWordId(int wordId); // Now returns concatenated synonyms as a string

    // Get all antonyms for a specific word
    @Query("SELECT clm_antonyms FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    String getAntonymsByWordId(int wordId); // Now returns concatenated antonyms as a string

    // Delete all synonyms/antonyms for a specific word and return the number of rows affected
    @Query("DELETE FROM tbl_synonyms_antonyms WHERE clm_word_id = :wordId")
    int deleteSynonymsAntonymsByWordId(int wordId);

    // Transactional method to insert synonyms and antonyms in one go
    @Transaction
    default void insertSynonymsAntonymsForWord(int wordId, List<SynonymAntonymEntity> synonymAntonymEntities) {
        for (SynonymAntonymEntity entity : synonymAntonymEntities) {
            entity.setClmWordId(wordId); // Set the word ID
            insertSynonymAntonym(entity); // Insert each synonym/antonym
        }
    }
}
