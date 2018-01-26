package com.notely.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import com.notely.model.Note;

import java.util.List;

/**
 * Created by yashwant on 21/01/18.
 */

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getNotes();

    @RawQuery(observedEntities = Note.class)
    LiveData<List<Note>> filteredNotes(String query);

    @Query("SELECT * FROM Note WHERE id= :id")
    LiveData<Note> loadById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Query("DELETE FROM Note WHERE id= :id" )
    int delete(int id);
}
