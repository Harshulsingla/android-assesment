package com.example.booksviewer.data.network;

import com.example.booksviewer.domain.models.BookResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("volumes")
    Call<BookResponseModel> getBookDetails(
            @Query("q") String query,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults
    );
}
