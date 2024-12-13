package com.example.dictionary.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tbl_parts_of_speech",
        foreignKeys = @ForeignKey(
                entity = WordEntity.class,
                parentColumns = "clm_id",
                childColumns = "clm_word_id",
                onDelete = ForeignKey.CASCADE // Automatically delete parts of speech when the corresponding word is deleted
        )
)
public class PartOfSpeechEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "clm_id")
    private int clmId;

    @ColumnInfo(name = "clm_part_of_speech")
    private String clmPartOfSpeech;

    @ColumnInfo(name = "clm_definition")
    private String clmDefinition;

    @ColumnInfo(name = "clm_example")
    private String clmExample;

    @ColumnInfo(name = "clm_word_id")
    private int clmWordId; // Foreign key to tbl_words

    // Getters and Setters
    public int getClmId() {
        return clmId;
    }

    public void setClmId(int clmId) {
        this.clmId = clmId;
    }

    public String getClmPartOfSpeech() {
        return clmPartOfSpeech;
    }

    public void setClmPartOfSpeech(String clmPartOfSpeech) {
        this.clmPartOfSpeech = clmPartOfSpeech;
    }

    public String getClmDefinition() {
        return clmDefinition;
    }

    public void setClmDefinition(String clmDefinition) {
        this.clmDefinition = clmDefinition;
    }

    public String getClmExample() {
        return clmExample;
    }

    public void setClmExample(String clmExample) {
        this.clmExample = clmExample;
    }

    public int getClmWordId() {
        return clmWordId;
    }

    public void setClmWordId(int clmWordId) {
        this.clmWordId = clmWordId;
    }
}

