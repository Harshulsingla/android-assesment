package com.example.dictionary.ui.views.offlineWords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;
import com.example.dictionary.domain.entity.PartOfSpeechEntity;
import com.example.dictionary.domain.entity.SynonymAntonymEntity;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.entity.WordEntity;
import com.example.dictionary.utils.CommonServices;

import java.util.ArrayList;
import java.util.List;

public class WordDetailAdapter extends RecyclerView.Adapter<WordDetailAdapter.OfflineWordViewHolder>{

    private List<WordDetailEntity> wordDetailEntityList = new ArrayList<>();

    private final OnClickListener listener;

    public interface OnClickListener{
        void onClickListener(String action, WordDetailEntity wordDetailEntity);
    }

    public WordDetailAdapter(List<WordDetailEntity> wordList, WordDetailAdapter.OnClickListener listener) {
        this.wordDetailEntityList = wordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordDetailAdapter.OfflineWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_card, parent, false);
        return new WordDetailAdapter.OfflineWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineWordViewHolder holder, int position) {
        WordDetailEntity wordDetailEntity = wordDetailEntityList.get(position);

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



    public void loadOfflineWords(List<WordDetailEntity> offlineWordsList){
        if(wordDetailEntityList != null && !offlineWordsList.isEmpty() && wordDetailEntityList.size() < offlineWordsList.size()){
            this.wordDetailEntityList = offlineWordsList;
            notifyItemInserted(0);
        }
    }

    public void deleteOfflineWord(int position){
        if(position >=0 && position < wordDetailEntityList.size()){
            wordDetailEntityList.remove(position);
          //  notifyItemRemoved(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return wordDetailEntityList.size();
    }

    public static class OfflineWordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTitle, phoneticPronunciation, partOfSpeech, definition, synonyms, synonymsHeading;
        ImageView playIcon, saveButton, deleteButton;

        public OfflineWordViewHolder(View itemView) {
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
