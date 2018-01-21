package com.notely.repository;

import android.arch.lifecycle.LiveData;

import com.notely.model.Note;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by yashwant on 21/01/18.
 */

@Singleton
public interface NoteRepository {
    LiveData<Note> loadById(int id);

    LiveData<List<Note>> getNotes();

    long insert(Note note);

    int delete(Note note);
}

