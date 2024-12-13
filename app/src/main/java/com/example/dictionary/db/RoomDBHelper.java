package com.example.dictionary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dictionary.db.dao.PartOfSpeechDao;
import com.example.dictionary.db.dao.WordDao;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.WordEntity;

@Database(
        entities = {
                WordEntity.class,
                PartOfSpeechEntity.class
        },
        version=1,
        exportSchema = false
)
public abstract class RoomDBHelper extends RoomDatabase {


    public abstract WordDao wordDao();

    public abstract PartOfSpeechDao partOfSpeechDao();



}
