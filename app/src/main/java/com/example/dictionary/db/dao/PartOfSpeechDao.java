package com.example.dictionary.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import androidx.room.Transaction;

import com.example.dictionary.domain.entity.PartOfSpeechEntity;

import java.util.List;

@Dao
public interface PartOfSpeechDao {

    // Insert a new part of speech and return the row ID of the inserted part of speech
    @Insert
    long insertPartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Update an existing part of speech and return the number of rows affected
    @Update
    int updatePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Delete a part of speech by its ID and return the number of rows affected
    @Delete
    int deletePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity);

    // Get all parts of speech
    @Query("SELECT * FROM tbl_parts_of_speech")
    List<PartOfSpeechEntity> getAllPartsOfSpeech();

    // Get a part of speech by its ID
    @Query("SELECT * FROM tbl_parts_of_speech WHERE clm_id = :partOfSpeechId")
    PartOfSpeechEntity getPartOfSpeechById(int partOfSpeechId);

    // Get all parts of speech for a specific word (based on word ID)
    @Query("SELECT * FROM tbl_parts_of_speech WHERE clm_word_id = :wordId")
    List<PartOfSpeechEntity> getPartsOfSpeechByWordId(int wordId);

    // Get parts of speech by a specific part of speech type
    @Query("SELECT * FROM tbl_parts_of_speech WHERE clm_part_of_speech = :partOfSpeechType")
    List<PartOfSpeechEntity> getPartsOfSpeechByType(String partOfSpeechType);

    // Transactional method to insert a part of speech with the word ID and return the row ID
    @Transaction
    default long insertPartOfSpeechWithWordId(PartOfSpeechEntity partOfSpeechEntity, int wordId) {
        partOfSpeechEntity.setClmWordId(wordId); // Set the word ID for the part of speech
        return insertPartOfSpeech(partOfSpeechEntity); // Insert part of speech and return the row ID
    }

    // Delete all parts of speech for a specific word and return the number of rows affected
    @Query("DELETE FROM tbl_parts_of_speech WHERE clm_word_id = :wordId")
    int deletePartsOfSpeechByWordId(int wordId);

    // Delete all parts of speech of a specific type and return the number of rows affected
    @Query("DELETE FROM tbl_parts_of_speech WHERE clm_part_of_speech = :partOfSpeechType")
    int deletePartsOfSpeechByType(String partOfSpeechType);
}
