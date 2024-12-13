package com.example.dictionary.ui.views.word_detail;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.mappers.WordMapper;
import com.example.dictionary.domain.models.DefinitionModel;
import com.example.dictionary.domain.models.MeaningModel;
import com.example.dictionary.domain.models.PhoneticModel;
import com.example.dictionary.utils.CommonServices;

import java.util.List;

public class WordDetailFragment extends Fragment implements View.OnClickListener{


    private FragmentWordDetailBinding binding;

    private static final String TAG = "WordDetailFragment";

    private WordDetailEntity wordDetailEntity;

    private Activity activity;

    public WordDetailFragment() {

    }

    public static WordDetailFragment newInstance(WordDetailEntity wordDetailEntity){
        WordDetailFragment fragment = new WordDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("word_detail_entity", wordDetailEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWordDetailBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            wordDetailEntity = (WordDetailEntity) getArguments().getSerializable("word_detail_entity");
        }

        WordEntity wordEntity = wordDetailEntity.getWordEntity();
        List<PartOfSpeechEntity> partOfSpeechEntities = wordDetailEntity.getPartOfSpeechEntities();
        SynonymAntonymEntity synonymAntonymEntity = wordDetailEntity.getSynonymAntonymEntity();


        if (wordEntity != null && wordEntity.getClmTitle() != null) {
            binding.wordTitle.setText(wordEntity.getClmTitle());
            binding.wordTitle.setVisibility(View.VISIBLE);
        } else {
            binding.wordTitle.setVisibility(View.GONE);
        }


        if (wordEntity != null && wordEntity.getClmPhonetic() != null) {
            binding.phoneticPronunciation.setText(wordEntity.getClmPhonetic());
            binding.phoneticPronunciation.setVisibility(View.VISIBLE);
        } else {
            binding.phoneticPronunciation.setVisibility(View.GONE);
        }


        if (wordEntity != null && wordEntity.getClmPronunciationAudioUrl() != null) {
            binding.playIcon.setOnClickListener(this);
            binding.playIcon.setVisibility(View.VISIBLE);
        } else {
            binding.playIcon.setVisibility(View.GONE);
        }


        if (synonymAntonymEntity != null && synonymAntonymEntity.getClmSynonyms() != null) {
            binding.synonymsList.setText(synonymAntonymEntity.getClmSynonyms());
            binding.synonymsList.setVisibility(View.VISIBLE);
        } else {
            binding.synonymsHeading.setVisibility(View.GONE);
            binding.synonymsList.setVisibility(View.GONE);
        }


        if (synonymAntonymEntity != null && synonymAntonymEntity.getClmAntonyms() != null) {
            binding.antonymsList.setText(synonymAntonymEntity.getClmAntonyms());
            binding.antonymsList.setVisibility(View.VISIBLE);
        } else {
            binding.antonymsHeading.setVisibility(View.GONE);
            binding.antonymsList.setVisibility(View.GONE);
        }


        for (PartOfSpeechEntity partOfSpeech : partOfSpeechEntities) {
            if (partOfSpeech.getClmPartOfSpeech() != null && !partOfSpeech.getClmPartOfSpeech().isEmpty()) {
                // Create a new LinearLayout to hold this part of speech block
                LinearLayout partOfSpeechBlock = new LinearLayout(getContext());
                partOfSpeechBlock.setOrientation(LinearLayout.VERTICAL);

                // Set margin for the entire part of speech block
                LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                blockParams.setMargins(0, 16, 0, 16); // 16 dp top and bottom margin for spacing between blocks
                partOfSpeechBlock.setLayoutParams(blockParams);

                // Create a TextView for the part of speech heading
                TextView partOfSpeechText = new TextView(getContext());
                partOfSpeechText.setText(partOfSpeech.getClmPartOfSpeech());
                partOfSpeechText.setTextColor(getResources().getColor(android.R.color.black));
                partOfSpeechText.setTextSize(21f);
                partOfSpeechText.setTypeface(Typeface.DEFAULT_BOLD);

                // Add the part of speech TextView to the block
                partOfSpeechBlock.addView(partOfSpeechText);

                // Check if definition is available
                if (partOfSpeech.getClmDefinition().length() > 0) {
                    // Add definition heading
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

                    // Create a TextView for the definitions
                    TextView definitionText = new TextView(getContext());
                    definitionText.setText(partOfSpeech.getClmDefinition().toString());
                    definitionText.setTextColor(getResources().getColor(android.R.color.black));
                    definitionText.setTextSize(19f);
                    definitionText.setVisibility(View.VISIBLE);
                    partOfSpeechBlock.addView(definitionText);
                }

                // Check if example is available
                if (partOfSpeech.getClmExample() != null && !partOfSpeech.getClmExample().isEmpty()) {
                    // Add example heading
                    TextView exampleHeading = new TextView(getContext());
                    exampleHeading.setText("Example:");
                    exampleHeading.setTextColor(getResources().getColor(android.R.color.black));
                    exampleHeading.setTextSize(19f);

                    LinearLayout.LayoutParams exampleHeadingParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    exampleHeadingParams.setMargins(0, 8, 0, 0); // 8 dp margin top for spacing before example heading

                    exampleHeading.setLayoutParams(exampleHeadingParams);

                    // Add the example heading to the block
                    partOfSpeechBlock.addView(exampleHeading);

                    // Create a TextView for the example text
                    TextView exampleText = new TextView(getContext());
                    exampleText.setText(partOfSpeech.getClmExample().toString()); // Assuming this is how you get example text
                    exampleText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    exampleText.setTextSize(17f);
                    exampleText.setVisibility(View.VISIBLE);
                    partOfSpeechBlock.addView(exampleText);
                }

                // Add the part of speech block to the container
                binding.partOfSpeechContainer.addView(partOfSpeechBlock);
            }
        }




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            activity = getActivity();
        }

        View parentView = activity.findViewById(R.id.searchIcon);
        parentView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Prevents memory leak by nullifying binding reference
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.playIcon){
            CommonServices.playAudio(wordDetailEntity.getWordEntity().getClmPronunciationAudioUrl(), getContext(), binding.playIcon);
        }
    }
}
