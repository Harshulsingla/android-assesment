package com.example.dictionary.db;

import com.example.dictionary.db.dao.PartOfSpeechDao;
import com.example.dictionary.db.dao.WordDao;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.WordEntity;
import java.util.List;

public class DbService {

    private final WordDao wordDao;
    private final PartOfSpeechDao partOfSpeechDao;

    public DbService(WordDao wordDao, PartOfSpeechDao partOfSpeechDao) {
        this.wordDao = wordDao;
        this.partOfSpeechDao = partOfSpeechDao;
    }

    // Word Methods
    public List<WordEntity> getAllWords() {
        return wordDao.getAllWords();
    }

    public WordEntity getWordById(int wordId) {
        return wordDao.getWordById(wordId);
    }

    public long insertWord(WordEntity wordEntity) {
        return wordDao.insertWord(wordEntity);
    }

    public int updateWord(WordEntity wordEntity) {
        return wordDao.updateWord(wordEntity);
    }

    public int deleteWord(WordEntity wordEntity) {
        return wordDao.deleteWord(wordEntity);
    }

    // Part of Speech Methods
    public List<PartOfSpeechEntity> getAllPartsOfSpeech() {
        return partOfSpeechDao.getAllPartsOfSpeech();
    }

    public PartOfSpeechEntity getPartOfSpeechById(int partOfSpeechId) {
        return partOfSpeechDao.getPartOfSpeechById(partOfSpeechId);
    }

    public List<PartOfSpeechEntity> getPartsOfSpeechByWordId(int wordId) {
        return partOfSpeechDao.getPartsOfSpeechByWordId(wordId);
    }

    public List<PartOfSpeechEntity> getPartsOfSpeechByType(String partOfSpeechType) {
        return partOfSpeechDao.getPartsOfSpeechByType(partOfSpeechType);
    }

    public long insertPartOfSpeech(PartOfSpeechEntity partOfSpeechEntity) {
        return partOfSpeechDao.insertPartOfSpeech(partOfSpeechEntity);
    }

    public int updatePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity) {
        return partOfSpeechDao.updatePartOfSpeech(partOfSpeechEntity);
    }

    public int deletePartOfSpeech(PartOfSpeechEntity partOfSpeechEntity) {
        return partOfSpeechDao.deletePartOfSpeech(partOfSpeechEntity);
    }

    public int deletePartsOfSpeechByWordId(int wordId) {
        return partOfSpeechDao.deletePartsOfSpeechByWordId(wordId);
    }

    public int deletePartsOfSpeechByType(String partOfSpeechType) {
        return partOfSpeechDao.deletePartsOfSpeechByType(partOfSpeechType);
    }

    public long insertPartOfSpeechWithWordId(PartOfSpeechEntity partOfSpeechEntity, int wordId) {
        return partOfSpeechDao.insertPartOfSpeechWithWordId(partOfSpeechEntity, wordId);
    }
}
