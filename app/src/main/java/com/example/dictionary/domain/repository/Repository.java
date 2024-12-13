package com.example.dictionary.domain.repository;

import com.example.dictionary.data.network.ApiService;
import com.example.dictionary.domain.models.WordModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class Repository {

    private final ApiService apiService;

    public Repository(ApiService apiService) {
        this.apiService = apiService;
    }

//    public Response<List<UserModel>> fetchUserList() throws IOException {
//        return apiService.fetchUserList().execute();
//    }

    public Response<List<WordModel>> fetchWordDetail(String word) throws IOException {
        return apiService.fetchWordDetail(word).execute();
    }

}
