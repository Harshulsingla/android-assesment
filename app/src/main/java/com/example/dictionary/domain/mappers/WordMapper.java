package com.example.dictionary.domain.mappers;


import com.example.dictionary.domain.models.DefinitionModel;
import com.example.dictionary.domain.models.PhoneticModel;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.domain.models.MeaningModel;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordMapper {

    public static List<WordDetailEntity> mapModelListToWordDetailEntity(List<WordModel> wordModelList){
        List<WordDetailEntity> wordDetailEntities = new ArrayList<>();
        for(WordModel wordModel : wordModelList){
            WordDetailEntity wordDetailEntity = mapToWordDetailEntity(wordModel);
            wordDetailEntities.add(wordDetailEntity);
        }
        return wordDetailEntities;
    }

    public static WordDetailEntity mapToWordDetailEntity(WordModel wordModel) {
        if (wordModel == null) {
            return null;
        }

        // Map WordEntity (basic details)
        WordEntity wordEntity = mapToWordEntity(wordModel);

        // Prepare lists for PartOfSpeech and SynonymAntonym entities
        List<PartOfSpeechEntity> partOfSpeechEntities = mapToPartOfSpeechEntities(wordModel);
        SynonymAntonymEntity synonymAntonymEntity = mapToSynonymAntonymEntity(wordModel);

        // Return WordDetailEntity with all mapped details
        return new WordDetailEntity(wordEntity, partOfSpeechEntities, synonymAntonymEntity);
    }

    private static WordEntity mapToWordEntity(WordModel wordModel) {
        if (wordModel == null) {
            return null;
        }

        WordEntity wordEntity = new WordEntity();

        // Map title (word)
        wordEntity.setClmTitle(wordModel.getWord() != null ? wordModel.getWord() : "");

        // Phonetics: Extract text and audio
        String phoneticText = getPhoneticText(wordModel);
        String audioUrl = getAudioUrl(wordModel);

        wordEntity.setClmPhonetic(phoneticText);
        wordEntity.setClmPronunciationAudioUrl(audioUrl);

        return wordEntity;
    }

    private static String getPhoneticText(WordModel wordModel) {
        String phoneticText = null;
        if (wordModel.getPhonetics() != null) {
            for (PhoneticModel phonetic : wordModel.getPhonetics()) {
                if (phonetic != null) {
                    if (phonetic.getText() != null && !phonetic.getText().isEmpty()) {
                        phoneticText = phonetic.getText();
                        break;
                    }
                }
            }
        }
        // Fallback to `phonetic` key if no valid text found
        if (phoneticText == null && wordModel.getPhonetic() != null) {
            phoneticText = wordModel.getPhonetic();
        }
        return phoneticText;
    }

    private static String getAudioUrl(WordModel wordModel) {
        String audioUrl = null;
        if (wordModel.getPhonetics() != null) {
            for (PhoneticModel phonetic : wordModel.getPhonetics()) {
                if (phonetic != null && phonetic.getAudio() != null && !phonetic.getAudio().isEmpty()) {
                    audioUrl = phonetic.getAudio();
                    break;
                }
            }
        }
        return audioUrl;
    }

    private static List<PartOfSpeechEntity> mapToPartOfSpeechEntities(WordModel wordModel) {
        List<PartOfSpeechEntity> partOfSpeechEntities = new ArrayList<>();
        if (wordModel.getMeanings() != null) {
            for (MeaningModel meaning : wordModel.getMeanings()) {
                if (meaning != null) {
                    // Create and map PartOfSpeechEntity
                    PartOfSpeechEntity partOfSpeechEntity = new PartOfSpeechEntity();
                    partOfSpeechEntity.setClmPartOfSpeech(meaning.getPartOfSpeech());

                    // Concatenate all definitions into a single string
                    String definitions = "";
                    if (meaning.getDefinitions() != null && !meaning.getDefinitions().isEmpty()) {
                        // Concatenate each definition, separating with commas and spaces
                        definitions = meaning.getDefinitions().stream()
                                .map(def -> def.getDefinition())  // Extract the definition string
                                .reduce((def1, def2) -> def1 + ", " + def2)  // Concatenate them
                                .orElse("");  // If no definitions, leave it empty
                    }
                    partOfSpeechEntity.setClmDefinition(definitions);

                    // Get the first example if available, or leave it empty
                    String example = "";
                    if (meaning.getDefinitions() != null) {
                        for (DefinitionModel definition : meaning.getDefinitions()) {
                            if (definition.getExample() != null && !definition.getExample().isEmpty()) {
                                example = definition.getExample();  // Assign the first example
                                break;
                            }
                        }
                    }
                    partOfSpeechEntity.setClmExample(example);

                    // Add the PartOfSpeechEntity to the list
                    partOfSpeechEntities.add(partOfSpeechEntity);
                }
            }
        }
        return partOfSpeechEntities;
    }


    private static SynonymAntonymEntity mapToSynonymAntonymEntity(WordModel wordModel) {
        SynonymAntonymEntity synonymAntonymEntity = null;
        if (wordModel.getMeanings() != null) {
            for (MeaningModel meaning : wordModel.getMeanings()) {
                if (meaning != null) {
                    String synonymsStr = meaning.getSynonyms() != null
                            ? String.join(", ", meaning.getSynonyms())
                            : "";
                    String antonymsStr = meaning.getAntonyms() != null
                            ? String.join(", ", meaning.getAntonyms())
                            : "";

                    if (!synonymsStr.isEmpty() || !antonymsStr.isEmpty()) {
                        synonymAntonymEntity = new SynonymAntonymEntity();
                        synonymAntonymEntity.setClmSynonyms(synonymsStr);
                        synonymAntonymEntity.setClmAntonyms(antonymsStr);
                        break; // Only use the first set of synonyms/antonyms
                    }
                }
            }
        }
        return synonymAntonymEntity;
    }
}
