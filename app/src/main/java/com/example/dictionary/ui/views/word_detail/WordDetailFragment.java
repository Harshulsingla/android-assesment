package com.example.dictionary.ui.views.word_detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dictionary.R;
import com.example.dictionary.databinding.FragmentSearchBinding;
import com.example.dictionary.databinding.FragmentWordDetailBinding;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.models.DefinitionModel;
import com.example.dictionary.domain.models.MeaningModel;
import com.example.dictionary.domain.models.PhoneticModel;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.utils.CommonServices;

public class WordDetailFragment extends Fragment {

    private WordModel wordModel;

    private FragmentWordDetailBinding binding;

    public WordDetailFragment() {

    }

    public static WordDetailFragment newInstance(WordModel wordModel){
        WordDetailFragment fragment = new WordDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("word_model", wordModel); // Assuming WordModel implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWordDetailBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            wordModel = (WordModel) getArguments().getSerializable("word_model");
        }
        binding.wordTitle.setText(wordModel.getWord());

        if (wordModel.getPhonetics() != null && !wordModel.getPhonetics().isEmpty()) {
            boolean audioSet = false;
            for (PhoneticModel phonetic : wordModel.getPhonetics()) {
                String audioUrl = phonetic.getAudio();
                String phoneticText = phonetic.getText();

                if (audioUrl != null && audioUrl.length() > 1 && phoneticText != null && phoneticText.length() > 0) {
                    binding.playIcon.setOnClickListener(v -> CommonServices.playAudio(audioUrl, getContext(), binding.playIcon));
                    binding.phoneticPronunciation.setText(phoneticText);
                    binding.playIcon.setVisibility(View.VISIBLE);
                    audioSet = true;
                    break;
                }
            }

            if (!audioSet) {
                if (wordModel.getPhonetic() != null && wordModel.getPhonetic().length() > 0) {
                    binding.phoneticPronunciation.setText(wordModel.getPhonetic());
                } else if (!wordModel.getPhonetics().isEmpty()) {
                    binding.phoneticPronunciation.setText(wordModel.getPhonetics().get(0).getText());
                }
                binding.playIcon.setOnClickListener(null);
                binding.playIcon.setVisibility(View.GONE);
            }
        } else {
            if (wordModel.getPhonetic() != null && wordModel.getPhonetic().length() > 0) {
                binding.phoneticPronunciation.setText(wordModel.getPhonetic());
            } else {
                binding.phoneticPronunciation.setVisibility(View.GONE);
                binding.playIcon.setVisibility(View.GONE);
            }
        }

        LinearLayout partOfSpeechContainer = binding.partOfSpeechContainer;

// Assuming you have a list of MeaningModel objects, loop through them

        for (MeaningModel meaningModel : wordModel.getMeanings()) {
            if (meaningModel.getPartOfSpeech() != null && !meaningModel.getPartOfSpeech().isEmpty()) {
                // Create a new LinearLayout for each part of speech block to group related views
                LinearLayout partOfSpeechBlock = new LinearLayout(getContext());
                partOfSpeechBlock.setOrientation(LinearLayout.VERTICAL);

                // Set margin for the entire part of speech block
                LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                blockParams.setMargins(0, 16, 0, 16); // 16 dp top and bottom margin for spacing between blocks
                partOfSpeechBlock.setLayoutParams(blockParams);

                // Create a TextView for the part of speech
                TextView partOfSpeechText = new TextView(getContext());
                partOfSpeechText.setText(meaningModel.getPartOfSpeech());
                partOfSpeechText.setTextColor(getResources().getColor(android.R.color.black));
                partOfSpeechText.setTextSize(21f);
                partOfSpeechText.setTypeface(Typeface.DEFAULT_BOLD);

                // Add the part of speech TextView to the block
                partOfSpeechBlock.addView(partOfSpeechText);

                // Add a definition heading
                TextView definitionHeading = new TextView(getContext());
                definitionHeading.setText("Definition:");
                definitionHeading.setTextColor(getResources().getColor(android.R.color.black));
                definitionHeading.setTextSize(19f);

                // Set margin between part of speech and definition heading
                LinearLayout.LayoutParams definitionHeadingParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                definitionHeadingParams.setMargins(0, 8, 0, 8); // 8 dp margin
                definitionHeading.setLayoutParams(definitionHeadingParams);

                // Add the definition heading to the block
                partOfSpeechBlock.addView(definitionHeading);

                // Combine all definitions into a single comma-separated string
                StringBuilder definitionsText = new StringBuilder();
                for (DefinitionModel definition : meaningModel.getDefinitions()) {
                    if (definition.getDefinition() != null && !definition.getDefinition().isEmpty()) {
                        if (definitionsText.length() > 0) {
                            definitionsText.append(", ");
                        }
                        definitionsText.append(definition.getDefinition());
                    }
                }

                // Create a TextView for the definitions
                TextView definitionText = new TextView(getContext());
                if (definitionsText.length() > 0) {
                    definitionText.setText(definitionsText.toString());
                    definitionText.setTextColor(getResources().getColor(android.R.color.black));
                    definitionText.setTextSize(19f);
                    definitionText.setVisibility(View.VISIBLE);
                } else {
                    definitionText.setVisibility(View.GONE);
                }
                partOfSpeechBlock.addView(definitionText);

                // Add an example heading
                TextView exampleHeading = new TextView(getContext());
                exampleHeading.setText("Example:");
                exampleHeading.setTextColor(getResources().getColor(android.R.color.black));
                exampleHeading.setTextSize(19f);

                // Add the example heading to the block
                partOfSpeechBlock.addView(exampleHeading);

                // Create a TextView for the example text
                TextView exampleText = new TextView(getContext());
                String example = meaningModel.getSynonyms().toString(); // Assuming you have this method in the model
                if (example != null && !example.isEmpty()) {
                    exampleText.setText(example);
                    exampleText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    exampleText.setTextSize(17f);
                    exampleText.setVisibility(View.VISIBLE);
                } else {
                    exampleText.setVisibility(View.GONE);
                }
                partOfSpeechBlock.addView(exampleText);

                // Add the part of speech block to the main container
                partOfSpeechContainer.addView(partOfSpeechBlock);
            }
        }





//        int partOfSpeechCount = 0;
//        if (wordModel.getMeanings() != null && !wordModel.getMeanings().isEmpty()) {
//            for (MeaningModel meaning : wordModel.getMeanings()) {
//                if (partOfSpeechCount >= 3) break; // Stop after 3 parts of speech
//
//                binding.partOfSpeech.setText(meaning.getPartOfSpeech());
//
//                if (meaning.getDefinitions() != null && !meaning.getDefinitions().isEmpty()) {
//                    DefinitionModel firstDefinition = meaning.getDefinitions().get(0);
//                    binding.definition.setText("Definition: " + firstDefinition.getDefinition());
//
//                    if (firstDefinition.getExample() != null && !firstDefinition.getExample().isEmpty()) {
//                        binding.example.setText("Example: " + firstDefinition.getExample());
//                        binding.example.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.example.setVisibility(View.GONE);
//                    }
//
//                    if (firstDefinition.getSynonyms() != null && !firstDefinition.getSynonyms().isEmpty()) {
//                        binding.synonyms.setText("Synonyms: " + String.join(", ", firstDefinition.getSynonyms()));
//                        binding.synonyms.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.synonyms.setVisibility(View.GONE);
//                    }
//                } else {
//                    binding.definition.setText("Definition: N/A");
//                    binding.example.setVisibility(View.GONE);
//                    binding.synonyms.setVisibility(View.GONE);
//                }
//                partOfSpeechCount++;
//            }
//        }
//
//        if (partOfSpeechCount == 0) {
//            binding.partOfSpeech.setText("N/A");
//            binding.definition.setText("Definition: N/A");
//            binding.synonyms.setVisibility(View.GONE);
//            binding.example.setVisibility(View.GONE);
//        }





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Prevents memory leak by nullifying binding reference
    }


    public void WrodModelToEntityMappper(WordModel wordModel){
        WordEntity wordEntity = new WordEntity();

    }



}
