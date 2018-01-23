package com.notely.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.notely.model.Note;
import com.notely.repository.NoteRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * Created by yashwant on 21/01/18.
 */

public class NoteViewModel extends ViewModel {

    private final NoteRepository repository;

    @Inject
    NoteViewModel(NoteRepository noteRepository) {
        this.repository = noteRepository;
    }

    public LiveData<List<Note>> getNotes() {
        return repository.getNotes();
    }

    public Completable insertNote(final Note note) {
        return Completable.fromAction(() -> repository.insert(note));
    }

    public Completable deleteNote(final int id) {
        return Completable.fromAction(() -> repository.delete(id));
    }
}
