package com.example.dictionary.db;

import com.example.dictionary.db.dao.PartOfSpeechDao;
import com.example.dictionary.db.dao.SynonymAntonymDao;
import com.example.dictionary.db.dao.WordDao;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;

import java.util.List;

public class DbService {

    private final WordDao wordDao;
    private final PartOfSpeechDao partOfSpeechDao;
    private final SynonymAntonymDao synonymAntonymDao;

    public DbService(WordDao wordDao, PartOfSpeechDao partOfSpeechDao, SynonymAntonymDao synonymAntonymDao) {
        this.wordDao = wordDao;
        this.partOfSpeechDao = partOfSpeechDao;
        this.synonymAntonymDao = synonymAntonymDao;
    }

    // Word Methods
    public List<WordEntity> getAllWords() {
        return wordDao.getAllWords();
    }

    public WordEntity getWordById(int wordId) {
        return wordDao.getWordById(wordId);
    }

    public long deleteAllWords(){
        return wordDao.deleteAllWords();
    }

    public List<WordEntity> getWordsByTitle(String title){
        return wordDao.getWordsByTitle(title);
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

    public String getSynonymsByWordId(int wordId) {
        return synonymAntonymDao.getSynonymsByWordId(wordId);
    }

    public String getAntonymsByWordId(int wordId) {
        return synonymAntonymDao.getAntonymsByWordId(wordId);
    }

    public long insertSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity) {
        return synonymAntonymDao.insertSynonymAntonym(synonymAntonymEntity);
    }

    public int updateSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity) {
        return synonymAntonymDao.updateSynonymAntonym(synonymAntonymEntity);
    }

    public int deleteSynonymAntonym(SynonymAntonymEntity synonymAntonymEntity) {
        return synonymAntonymDao.deleteSynonymAntonym(synonymAntonymEntity);
    }

    // Transactional methods to handle Word, PartOfSpeech, and SynonymAntonym in one transaction
    public long insertWordWithDetails(WordEntity wordEntity, List<PartOfSpeechEntity> partOfSpeechEntities, SynonymAntonymEntity synonymAntonymEntity) {
        return wordDao.insertWordWithDetails(wordEntity, partOfSpeechEntities, synonymAntonymEntity);
    }



    public int deleteWordWithDetails(WordDetailEntity wordDetailEntity) {
        int wordId = wordDetailEntity.getWordEntity().getClmId();
        return wordDao.deleteWordWithDetails(wordId);
    }

    public List<WordDetailEntity> getAllWordDetails() {
       return wordDao.getAllWordDetails();
    }
}
