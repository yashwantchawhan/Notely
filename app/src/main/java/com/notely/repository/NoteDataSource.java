package com.notely.repository;

import android.arch.lifecycle.LiveData;

import com.notely.db.NoteDao;
import com.notely.model.Note;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yashwant on 21/01/18.
 */

public class NoteDataSource implements NoteRepository {

    private NoteDao noteDao;

    @Inject
    public NoteDataSource(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public LiveData<Note> loadById(int id) {
        return noteDao.loadById(id);
    }

    @Override
    public LiveData<List<Note>> getNotes() {
        return noteDao.getNotes();
    }

    @Override
    public long insert(final Note note) {
        return noteDao.insert(note);
    }


    @Override
    public int delete(int id) {
        return noteDao.delete(id);
    }

    @Override
    public LiveData<List<Note>> filteredNotes(String query) {
        return noteDao.filteredNotes(query);
    }

    @Override
    public void updateNote(Note note) {
        noteDao.updateNote(note);
    }
}
