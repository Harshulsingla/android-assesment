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

import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.domain.mappers.WordMapper;
import com.example.dictionary.domain.models.DefinitionModel;
import com.example.dictionary.domain.models.MeaningModel;
import com.example.dictionary.domain.models.PhoneticModel;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.R;
import com.example.dictionary.ui.views.offlineWords.OfflineWordsFragment;
import com.example.dictionary.utils.CommonServices;

import java.io.IOException;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<WordDetailEntity> wordList;

    private final OnClickListener listener;


    public interface OnClickListener{
        void onClickListener(String action, WordDetailEntity wordDetailEntity);
    }

    public WordAdapter(List<WordDetailEntity> wordList, OnClickListener listener) {
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
        WordDetailEntity wordDetailEntity = wordList.get(position);

        WordEntity wordEntity = wordDetailEntity.getWordEntity();
        List<PartOfSpeechEntity> partOfSpeechEntities = wordDetailEntity.getPartOfSpeechEntities();
        SynonymAntonymEntity synonymAntonymEntity = wordDetailEntity.getSynonymAntonymEntity();

        if (wordEntity != null && wordEntity.getClmTitle() != null) {
            holder.wordTitle.setText(wordEntity.getClmTitle());
            holder.wordTitle.setVisibility(View.VISIBLE);
        } else {
            holder.wordTitle.setVisibility(View.GONE);
        }


        if (wordEntity != null && wordEntity.getClmPhonetic() != null) {
            holder.phoneticPronunciation.setText(wordEntity.getClmPhonetic());
            holder.phoneticPronunciation.setVisibility(View.VISIBLE);
        } else {
            holder.phoneticPronunciation.setVisibility(View.GONE);
        }


        if (wordEntity != null && wordEntity.getClmPronunciationAudioUrl() != null) {
            holder.playIcon.setOnClickListener(v-> CommonServices.playAudio(wordEntity.getClmPronunciationAudioUrl(), holder.itemView.getContext(), holder.playIcon));
            holder.playIcon.setVisibility(View.VISIBLE);
        } else {
            holder.playIcon.setVisibility(View.GONE);
        }

        for(PartOfSpeechEntity partOfSpeechEntity : partOfSpeechEntities){
            holder.partOfSpeech.setText(partOfSpeechEntity.getClmPartOfSpeech());
            holder.definition.setText(partOfSpeechEntity.getClmDefinition());
            if(partOfSpeechEntity.getClmExample() != null && partOfSpeechEntity.getClmExample().length()>0){
                holder.synonyms.setText(partOfSpeechEntity.getClmExample());
            }else{
                holder.synonymsHeading.setVisibility(View.GONE);
            }
            break;
        }

        if(listener instanceof OfflineWordsFragment){
            holder.saveButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v-> listener.onClickListener("DeleteWord", wordDetailEntity));
        }else {
            holder.saveButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.saveButton.setOnClickListener(v-> listener.onClickListener("SaveWord", wordDetailEntity));
        }

        holder.itemView.setOnClickListener(v-> listener.onClickListener("SeeMore", wordDetailEntity));


    }

    public void updateSearchResult(List<WordDetailEntity> wordList){
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTitle, phoneticPronunciation, partOfSpeech, definition, synonyms, synonymsHeading;
        ImageView playIcon, saveButton, deleteButton;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordTitle = itemView.findViewById(R.id.wordTitle);
            phoneticPronunciation = itemView.findViewById(R.id.phoneticPronunciation);
            playIcon = itemView.findViewById(R.id.playIcon);
            partOfSpeech = itemView.findViewById(R.id.partOfSpeech);
            definition = itemView.findViewById(R.id.definition);
            synonyms = itemView.findViewById(R.id.synonyms);
            saveButton = itemView.findViewById(R.id.saveButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            synonymsHeading = itemView.findViewById(R.id.synonymsHeading);
        }
    }
}

