package com.example.dictionary.domain.entity;

import java.io.Serializable;
import java.util.List;

public class WordDetailEntity implements Serializable {

    private WordEntity wordEntity;
    private List<PartOfSpeechEntity> partOfSpeechEntities;
    private SynonymAntonymEntity synonymAntonymEntity;

    // Constructor
    public WordDetailEntity(WordEntity wordEntity, List<PartOfSpeechEntity> partOfSpeechEntities, SynonymAntonymEntity synonymAntonymEntity) {
        this.wordEntity = wordEntity;
        this.partOfSpeechEntities = partOfSpeechEntities;
        this.synonymAntonymEntity = synonymAntonymEntity;
    }

    // Getters and Setters
    public WordEntity getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(WordEntity wordEntity) {
        this.wordEntity = wordEntity;
    }

    public List<PartOfSpeechEntity> getPartOfSpeechEntities() {
        return partOfSpeechEntities;
    }

    public void setPartOfSpeechEntities(List<PartOfSpeechEntity> partOfSpeechEntities) {
        this.partOfSpeechEntities = partOfSpeechEntities;
    }

    public SynonymAntonymEntity getSynonymAntonymEntity() {
        return synonymAntonymEntity;
    }

    public void setSynonymAntonymEntity(SynonymAntonymEntity synonymAntonymEntity) {
        this.synonymAntonymEntity = synonymAntonymEntity;
    }

    @Override
    public String toString() {
        return "WordDetailEntity{" +
                "wordEntity=" + wordEntity +
                ", partOfSpeechEntities=" + partOfSpeechEntities +
                ", synonymAntonymEntities=" + synonymAntonymEntity +
                '}';
    }
}

