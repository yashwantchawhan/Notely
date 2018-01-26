package com.notely.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.notely.model.Filter;
import com.notely.model.Note;
import com.notely.repository.NoteRepository;
import com.notely.utility.NoteType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by yashwant on 21/01/18.
 */

public class NoteViewModel extends ViewModel {

    private final NoteRepository repository;
    private static final String FILTER_QUERY = "SELECT * FROM Note WHERE ";
    private static final String SELECT_QUERY_ALL = "SELECT * FROM Note ORDER BY time_created DESC";
    private static final String ORDER_BY = "ORDER BY time_created DESC";

    @Inject
    NoteViewModel(NoteRepository noteRepository) {
        this.repository = noteRepository;
    }

    public LiveData<List<Note>> getNotes() {
        return repository.getNotes();
    }

    public LiveData<List<Note>> filteredNotes(String query) {
        return repository.filteredNotes(query);
    }

    public Completable insertNote(final Note note) {
        return Completable.fromAction(() -> repository.insert(note));
    }

    public Completable deleteNote(final int id) {
        return Completable.fromAction(() -> repository.delete(id));
    }

    public Completable updateNote(Note note) {
        return Completable.fromAction(() -> repository.updateNote(note));
    }

    public LiveData<List<Note>> filteredNotes(ArrayList<Filter> filterList) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Filter filter : filterList) {
            if (filter.isChecked()) {
                switch (filter.getFilterType()) {
                    case Poem:
                        queryBuilder.append("type='" + NoteType.POEM.toString() + "'");
                        break;
                    case Star:
                        if (queryBuilder.toString().isEmpty()) {
                            queryBuilder.append("star='" + 1 + "'");
                        } else {
                            queryBuilder.append("AND star='" + 1 + "'");
                        }
                        break;
                    case Favourite:
                        if (queryBuilder.toString().isEmpty()) {
                            queryBuilder.append("favourite='" + 1 + "'");
                        } else {
                            queryBuilder.append("AND favourite='" + 1 + "'");
                        }
                        break;
                    case Story:
                        if (queryBuilder.toString().isEmpty()) {
                            queryBuilder.append("type='" + NoteType.STORY.toString() + "'");
                        } else {
                            if (queryBuilder.toString().contains(NoteType.POEM.toString())) {
                                queryBuilder.append(" OR type='" + NoteType.STORY.toString() + "'");

                            } else {
                                queryBuilder.append(" AND type='" + NoteType.STORY.toString() + "'");

                            }
                        }
                        break;


                }
            }
        }
        return repository.filteredNotes(queryBuilder.toString().isEmpty() ? SELECT_QUERY_ALL : FILTER_QUERY + queryBuilder.toString() + ORDER_BY);

    }
}
