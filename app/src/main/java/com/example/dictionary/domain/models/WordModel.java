package com.example.dictionary.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WordModel implements Serializable {

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("phonetic")
    @Expose
    private String phonetic;

    @SerializedName("phonetics")
    @Expose
    private List<PhoneticModel> phonetics;

    @SerializedName("meanings")
    @Expose
    private List<MeaningModel> meanings;

    // Getters and Setters

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public List<PhoneticModel> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<PhoneticModel> phonetics) {
        this.phonetics = phonetics;
    }

    public List<MeaningModel> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<MeaningModel> meanings) {
        this.meanings = meanings;
    }

 }
