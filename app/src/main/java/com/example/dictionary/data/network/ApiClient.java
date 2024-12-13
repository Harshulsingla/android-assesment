//package com.example.dictionary.data.network;
//
//import android.os.StrictMode;
//import android.util.Log;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class ApiClient {
//
//    private static final String TAG = "ApiClient";
//
//    private static final String BASE_URL = "https://api.dictionaryapi.dev/";
//
//    public Retrofit createRetrofitClient() {
//
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////        StrictMode.setThreadPolicy(policy);
//
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
//                message -> Log.d(TAG, "ApiInterceptor() " + message)
//        );
//
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        // Build OkHttpClient with interceptor
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .hostnameVerifier((hostname, session) -> true) // Disable hostname verification
//                .build();
//
//        // Create Retrofit instance with OkHttpClient
//        return new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }
//
//    public ApiService createApiService() {
//        return createRetrofitClient().create(ApiService.class);
//    }
//}
