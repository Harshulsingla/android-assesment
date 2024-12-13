package com.example.booksviewer.data.db;

import android.content.IntentFilter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.booksviewer.data.db.dao.BookDao;
import com.example.booksviewer.domain.entity.BookEntity;
import com.example.booksviewer.domain.entity.SaleInfoEntity;
import com.example.booksviewer.domain.entity.SearchInfoEntity;
import com.example.booksviewer.domain.entity.VolumeInfoEntity;

@Database(entities = {BookEntity.class, SaleInfoEntity.class, SearchInfoEntity.class, VolumeInfoEntity.class}, version = 6,exportSchema = false)
public abstract class RoomDbHelper extends RoomDatabase {

    public abstract BookDao bookDao();
}
