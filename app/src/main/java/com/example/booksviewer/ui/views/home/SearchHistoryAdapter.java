package com.example.booksviewer.ui.views.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksviewer.R;

import java.util.List;

/**
 * Adapter to display the search history in a RecyclerView.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<String> searchHistory;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        /**
         * Called when an item in the search history is clicked.
         *
         * @param query The search query clicked by the user.
         */
        void onItemClick(String query);
    }

    /**
     * Constructor to initialize the adapter with search history data and a click listener.
     *
     * @param searchHistory The list of search history items.
     * @param listener      The listener to handle item click events.
     */
    public SearchHistoryAdapter(List<String> searchHistory, OnItemClickListener listener) {
        this.searchHistory = searchHistory;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for search history entry
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the view holder
        String query = searchHistory.get(position);
        holder.bind(query, listener);
    }

    @Override
    public int getItemCount() {
        // Return the size of the search history list
        return searchHistory != null ? searchHistory.size() : 0;
    }

    /**
     * Updates the search history list.
     *
     * @param newSearchHistory The new list of search history items.
     */
    public void updateSearchHistory(List<String> newSearchHistory) {
        this.searchHistory = newSearchHistory;
        notifyDataSetChanged();  // Notify adapter that data has changed
    }

    /**
     * ViewHolder class to hold and bind individual search history items.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView queryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            queryText = itemView.findViewById(R.id.query_text);
        }

        /**
         * Binds the search query to the view holder.
         *
         * @param query   The search query to display.
         * @param listener The click listener to handle item clicks.
         */
        public void bind(final String query, final OnItemClickListener listener) {
            queryText.setText(query);  // Set the query text in the view

            // Set a click listener to handle item clicks
            itemView.setOnClickListener(v -> listener.onItemClick(query));
        }
    }
}
