package com.example.booksviewer.di;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.booksviewer.data.db.DbService;
import com.example.booksviewer.data.db.RoomDbHelper;
import com.example.booksviewer.data.network.ApiService;
import com.example.booksviewer.data.preference.PreferenceHelper;
import com.example.booksviewer.data.repository.Repository;

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

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";

    @Provides
    @Singleton
    public Retrofit createRetrofitClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.d("DataModule", "ApiInterceptor -> " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public ApiService provideMainApiService() {
        return createRetrofitClient().create(ApiService.class);
    }

    @Provides
    @Singleton
    public Repository providesRepository(ApiService apiService, PreferenceHelper preferenceHelper) {
        return new Repository(apiService,preferenceHelper);
    }

    @Provides
    @Singleton
    public PreferenceHelper providePreferenceHelper(@ApplicationContext Context context) {
        return new PreferenceHelper(context);
    }

    @Provides
    @Singleton
    public RoomDbHelper provideDatabase(@ApplicationContext  Context context) {
        synchronized (RoomDbHelper.class){
            return Room.databaseBuilder(
                            context,
                            RoomDbHelper.class,
                            "books_database"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
    }

    @Provides
    @Singleton
    public DbService provideDbService(RoomDbHelper db) {
        return new DbService(db);
    }
}
