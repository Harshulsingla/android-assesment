package com.example.dictionary.di.module;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.dictionary.data.network.ApiService;
import com.example.dictionary.db.DbService;
import com.example.dictionary.db.RoomDBHelper;
import com.example.dictionary.db.dao.PartOfSpeechDao;
import com.example.dictionary.db.dao.SynonymAntonymDao;
import com.example.dictionary.db.dao.WordDao;
import com.example.dictionary.domain.repository.Repository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    private static final String BASE_URL = "https://api.dictionaryapi.dev/";

    private static final String TAG = "DataModule";

    @Provides
    @Singleton
    public Repository provideRepository(ApiService apiService){
        return new Repository(apiService);
    }

    public Retrofit createRetrofitClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.d(TAG, "ApiInterceptor() " + message)
        );

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build OkHttpClient with interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .hostnameVerifier((hostname, session) -> true) // Disable hostname verification
                .build();

        // Create Retrofit instance with OkHttpClient
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public ApiService createApiService() {
        return createRetrofitClient().create(ApiService.class);
    }

    @Provides
    @Singleton
    public RoomDBHelper provideRoomDbHelper(@ApplicationContext Context context){
        synchronized (RoomDBHelper.class){
            return Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomDBHelper.class,
                            "app_database"
                    ).addMigrations()
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    @Provides
    @Singleton
    public WordDao provideWordDao(RoomDBHelper db) {
        return db.wordDao();
    }

    @Provides
    @Singleton
    public PartOfSpeechDao providePartOfSpeechDao(RoomDBHelper db) {
        return db.partOfSpeechDao();
    }

    @Provides
    @Singleton
    public SynonymAntonymDao provideSynonymAntonymDao(RoomDBHelper db) {
        return db.synonymAntonymDao();
    }

    @Provides
    @Singleton
    public DbService provideDbService(WordDao wordDao, PartOfSpeechDao partOfSpeechDao, SynonymAntonymDao synonymAntonymDao) {
        return new DbService(wordDao, partOfSpeechDao, synonymAntonymDao);
    }


}
