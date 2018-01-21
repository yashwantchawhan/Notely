package com.notely.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.notely.model.Note;

import java.util.List;

/**
 * Created by yashwant on 21/01/18.
 */

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM Note WHERE id= :id")
    LiveData<Note> loadById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Query("DELETE FROM Note WHERE id= :id" )
    int delete(int id);
}
