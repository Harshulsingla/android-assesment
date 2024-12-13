package com.example.dictionary.domain.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_words")
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "clm_id")
    private int clmId;

    @ColumnInfo(name = "clm_title")
    private String clmTitle;

    @ColumnInfo(name = "clm_phonetic")
    private String clmPhonetic;

    @ColumnInfo(name = "clm_pronunciation_audio_url")
    private String clmPronunciationAudioUrl;

    @ColumnInfo(name = "clm_synonyms")
    private String clmSynonyms; // Comma-separated list of synonyms

    @ColumnInfo(name = "clm_antonyms")
    private String clmAntonyms; // Comma-separated list of antonyms

    // Getters and Setters
    public int getClmId() {
        return clmId;
    }

    public void setClmId(int clmId) {
        this.clmId = clmId;
    }

    public String getClmTitle() {
        return clmTitle;
    }

    public void setClmTitle(String clmTitle) {
        this.clmTitle = clmTitle;
    }

    public String getClmPhonetic() {
        return clmPhonetic;
    }

    public void setClmPhonetic(String clmPhonetic) {
        this.clmPhonetic = clmPhonetic;
    }

    public String getClmPronunciationAudioUrl() {
        return clmPronunciationAudioUrl;
    }

    public void setClmPronunciationAudioUrl(String clmPronunciationAudioUrl) {
        this.clmPronunciationAudioUrl = clmPronunciationAudioUrl;
    }

    public String getClmSynonyms() {
        return clmSynonyms;
    }

    public void setClmSynonyms(String clmSynonyms) {
        this.clmSynonyms = clmSynonyms;
    }

    public String getClmAntonyms() {
        return clmAntonyms;
    }

    public void setClmAntonyms(String clmAntonyms) {
        this.clmAntonyms = clmAntonyms;
    }
}

