package com.example.dictionary.ui.views.search;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.domain.models.DefinitionModel;
import com.example.dictionary.domain.models.MeaningModel;
import com.example.dictionary.domain.models.PhoneticModel;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.R;
import com.example.dictionary.utils.CommonServices;

import java.io.IOException;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<WordModel> wordList;
    private MediaPlayer mediaPlayer;

    private final OnClickListener listener;


    public interface OnClickListener{
        void onClickListener(String action, WordModel word);
    }

    public WordAdapter(List<WordModel> wordList, OnClickListener listener) {
        this.wordList = wordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_card, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel word = wordList.get(position);

        // Set word title and phonetic pronunciation
        holder.wordTitle.setText(word.getWord());
        //holder.phoneticPronunciation.setText(word.getPhonetic());

        // Set part of speech, definition, and example (from the first meaning and its first definition)
        if (word.getMeanings() != null && !word.getMeanings().isEmpty()) {
            MeaningModel firstMeaning = word.getMeanings().get(0); // Get the first meaning
            holder.partOfSpeech.setText(firstMeaning.getPartOfSpeech());

            if (firstMeaning.getDefinitions() != null && !firstMeaning.getDefinitions().isEmpty()) {
                DefinitionModel firstDefinition = firstMeaning.getDefinitions().get(0); // Get the first definition
                holder.definition.setText("Definition: " + firstDefinition.getDefinition());

                if (firstDefinition.getSynonyms() != null && !firstDefinition.getSynonyms().isEmpty()) {
                    holder.synonyms.setText("Synonyms: " + String.join(", ", firstDefinition.getSynonyms()));
                    holder.synonyms.setVisibility(View.VISIBLE);
                } else {
                    holder.synonyms.setVisibility(View.GONE);
                }
            } else {
                holder.definition.setText("Definition: N/A");
                holder.synonyms.setVisibility(View.GONE);
            }
        } else {
            holder.partOfSpeech.setText("N/A");
            holder.definition.setText("Definition: N/A");
            holder.synonyms.setVisibility(View.GONE);
        }

        if (word.getPhonetics() != null && !word.getPhonetics().isEmpty()) {
            boolean audioSet = false;

            for (PhoneticModel phonetic : word.getPhonetics()) {
                String audioUrl = phonetic.getAudio();
                String phoneticText = phonetic.getText(); // Get the phonetic text (pronunciation)

                if (audioUrl != null && audioUrl.length() > 1 && phoneticText != null && phoneticText.length() > 0) {
                    // Found the first valid audio URL and phonetic text
                    holder.playIcon.setOnClickListener(v -> CommonServices.playAudio(audioUrl, holder.itemView.getContext(), holder.playIcon));
                    holder.phoneticPronunciation.setText(phoneticText); // Set phonetic text
                    holder.playIcon.setVisibility(View.VISIBLE); // Show the play icon
                    audioSet = true;
                    break; // Exit the loop after the first valid audio URL is found
                }
            }

            // If no valid audio URL was found, handle fallback cases
            if (!audioSet) {
                if (word.getPhonetic() != null && word.getPhonetic().length() > 0) {
                    // If word.getPhonetic() is available and not empty, set it to the phoneticPronunciation
                    holder.phoneticPronunciation.setText(word.getPhonetic());
                } else if (!word.getPhonetics().isEmpty()) {
                    // If no audio URL and phonetic text, use the first available phonetic text
                    holder.phoneticPronunciation.setText(word.getPhonetics().get(0).getText());
                }
                holder.playIcon.setOnClickListener(null); // Remove click listener if no audio URL is available
                holder.playIcon.setVisibility(View.GONE); // Hide the play icon
            }
        } else {
            // If phonetics list is empty or null, check word.getPhonetic()
            if (word.getPhonetic() != null && word.getPhonetic().length() > 0) {
                holder.phoneticPronunciation.setText(word.getPhonetic());
                holder.playIcon.setOnClickListener(null); // Remove click listener
                holder.playIcon.setVisibility(View.GONE);
            } else {
                holder.phoneticPronunciation.setVisibility(View.GONE);
                holder.playIcon.setOnClickListener(null); // Remove click listener
                holder.playIcon.setVisibility(View.GONE);
            }
             // Hide the play icon
        }

        holder.seeMoreButton.setOnClickListener(v-> listener.onClickListener("SeeMore", word));

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void updateSearchResult(List<WordModel> wordList){
        this.wordList = wordList;
        notifyDataSetChanged();
    }




    // Method to release MediaPlayer when adapter is no longer needed
    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTitle, phoneticPronunciation, partOfSpeech, definition, synonyms;
        ImageView playIcon, seeMoreButton;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordTitle = itemView.findViewById(R.id.wordTitle);
            phoneticPronunciation = itemView.findViewById(R.id.phoneticPronunciation);
            playIcon = itemView.findViewById(R.id.playIcon);
            partOfSpeech = itemView.findViewById(R.id.partOfSpeech);
            definition = itemView.findViewById(R.id.definition);
            synonyms = itemView.findViewById(R.id.synonyms);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
        }
    }
}

