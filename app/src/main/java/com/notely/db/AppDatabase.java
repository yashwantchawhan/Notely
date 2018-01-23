package com.notely.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.notely.model.Note;

import javax.inject.Singleton;

/**
 * Created by yashwant on 21/01/18.
 */

@Singleton
@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
