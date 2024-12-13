package com.example.dictionary.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefinitionModel {

    @SerializedName("definition")
    @Expose
    private String definition;

    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms;

    @SerializedName("antonyms")
    @Expose
    private List<String> antonyms;

    @SerializedName("example")  // Added the 'example' field with Gson annotation
    @Expose
    private String example;  // This will hold the example sentence

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
