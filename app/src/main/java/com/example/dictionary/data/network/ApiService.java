package com.example.dictionary.data.network;

import com.example.dictionary.domain.models.WordModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

//    @GET("api/v2/entries/en/Good")
//    Call<List<WordModel>> fetchUserList();

    @GET("api/v2/entries/en/{word}")
    Call<List<WordModel>> fetchWordDetail(@Path("word") String word);

}
