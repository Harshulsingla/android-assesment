package com.example.dictionary.domain.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tbl_synonyms_antonyms",
        foreignKeys = @ForeignKey(
                entity = WordEntity.class, // Parent entity
                parentColumns = "clm_id", // Column in WordEntity
                childColumns = "clm_word_id", // Column in this entity
                onDelete = ForeignKey.CASCADE // Automatically delete when the parent is deleted
        )
)
public class SynonymAntonymEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "clm_id")
    private int clmId;

    @ColumnInfo(name = "clm_word_id")
    private int clmWordId;

    @ColumnInfo(name = "clm_synonyms")
    private String clmSynonyms;

    @ColumnInfo(name = "clm_antonyms")
    private String clmAntonyms;

    // Getters and Setters
    public int getClmId() {
        return clmId;
    }

    public void setClmId(int clmId) {
        this.clmId = clmId;
    }

    public int getClmWordId() {
        return clmWordId;
    }

    public void setClmWordId(int clmWordId) {
        this.clmWordId = clmWordId;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
