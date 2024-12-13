package com.example.booksviewer.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PreferenceHelper {

    private static final String PREF_NAME = "books_viewer_prefs";
    private static final String SEARCH_HISTORY_KEY = "search_history";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public PreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Save search history as a list
    public void saveSearchHistory(String searchQuery) {
        List<String> currentHistory = getSearchHistory();

        // Check if the search query already exists in the history
        if (!currentHistory.contains(searchQuery)) {
            // Add the new query at the beginning of the list
            currentHistory.add(0, searchQuery);

            // Limit to the latest 5 queries
            if (currentHistory.size() > 5) {
                currentHistory.remove(currentHistory.size() - 1);
            }

            // Save the updated list to SharedPreferences
            sharedPreferences.edit()
                    .putString(SEARCH_HISTORY_KEY, gson.toJson(currentHistory))
                    .apply();
        }
    }

    // Get search history as a list
    public List<String> getSearchHistory() {
        String json = sharedPreferences.getString(SEARCH_HISTORY_KEY, "[]");
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Clear search history
    public void clearSearchHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply();
    }
}
