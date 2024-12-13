package com.example.dictionary.ui.views.search;


import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class HistoryManager {

    private final Context context;
    private final ListView historyListView;
    private ArrayAdapter<String> historyAdapter;
    private ArrayList<String> historyList;

    public HistoryManager(Context context, ListView historyListView) {
        this.context = context;
        this.historyListView = historyListView;
    }

    public interface OnHistoryClickListener {
        void onHistoryItemClicked(String word);
    }

    public void initializeHistory(Set<String> searchHistory, OnHistoryClickListener listener) {
        if (searchHistory != null && !searchHistory.isEmpty()) {
            historyList = new ArrayList<>(searchHistory);
            historyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, historyList);
            historyListView.setAdapter(historyAdapter);
            historyListView.setVisibility(View.VISIBLE);

            historyListView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedWord = historyList.get(position);
                listener.onHistoryItemClicked(selectedWord);
            });
        } else {
            historyListView.setVisibility(View.GONE); // Hide if no history
        }
    }


}
