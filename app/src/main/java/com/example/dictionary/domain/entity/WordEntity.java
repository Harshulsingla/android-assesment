package com.example.dictionary.domain.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        // Check if the same object reference
        if (this == obj) {
            return true;
        }

        // Check if obj is null or of a different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast and compare fields
        WordEntity other = (WordEntity) obj;

        return Objects.equals(clmId, other.clmId) &&
                Objects.equals(clmTitle, other.clmTitle);
    }


}
