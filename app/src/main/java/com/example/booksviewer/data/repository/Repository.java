package com.example.booksviewer.data.repository;

import com.example.booksviewer.data.network.ApiService;
import com.example.booksviewer.data.preference.PreferenceHelper;
import com.example.booksviewer.domain.models.BookResponseModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

import javax.inject.Inject;

public class Repository {

    private final ApiService apiService;
    private final PreferenceHelper preferenceHelper;

    @Inject
    public Repository(ApiService apiService, PreferenceHelper preferenceHelper) {
        this.apiService = apiService;
        this.preferenceHelper = preferenceHelper;
    }

    public Response<BookResponseModel> getBookDetails(String query, int randomStartIndex, int maxResults) throws IOException {
        // Call the API to fetch book details
        return apiService.getBookDetails(query, randomStartIndex, maxResults).execute();
    }

    // Save search history to shared preferences
    public void saveSearchHistory(String searchQuery) {
        preferenceHelper.saveSearchHistory(searchQuery);
    }

    // Get the search history from PreferenceHelper
    public List<String> getSearchHistory() {
        return preferenceHelper.getSearchHistory();
    }

    // Clear the search history
    public void clearSearchHistory() {
        preferenceHelper.clearSearchHistory();
    }
}
