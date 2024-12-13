package com.example.dictionary.domain.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class PreferenceHelper {

    private static SharedPreferences preferences = null;

    // Singleton instance to access SharedPreferences
    public static PreferenceHelper getInstance(Context context, String preferenceKey) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        }
        return new PreferenceHelper();
    }

    // Save search history
    public static void saveSearchHistory(Set<String> history) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("search_history", history);
        editor.apply();
    }

    // Get search history
    public static Set<String> getSearchHistory() {
        return preferences.getStringSet("search_history", null);
    }

    // Add new search word to history
    public static void addToSearchHistory(String word) {
        Set<String> history = getSearchHistory();

        // Initialize history if it is null
        if (history == null) {
            history = new LinkedHashSet<>();
        }

        // Add new word to the history
        history.add(word);

        // Remove the oldest item if size exceeds 5
        if (history.size() > 5) {
            Iterator<String> iterator = history.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // Move to the first item
                iterator.remove(); // Remove the oldest item
            }
        }

        // Save updated history
        saveSearchHistory(history);
    }

}
