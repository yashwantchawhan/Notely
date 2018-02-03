package com.notely.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.notely.model.Note;
import com.notely.repository.NoteRepository;
import com.notely.utility.NoteType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yashwant on 27/01/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class ViewModelTest {
    private NoteViewModel viewModel;


    private static final Note NOTE = new Note(NoteType.POEM.toString(), "title", "description", true, false);

    NoteRepository mockNoteRepository = new NoteRepository() {
        private HashMap<Integer, Note> store = new HashMap<Integer, Note>();

        @Override
        public LiveData<Note> loadById(int id) {
            MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
            noteLiveData.setValue(store.get(id));
            return noteLiveData;
        }

        @Override
        public LiveData<List<Note>> getNotes() {
            MutableLiveData<List<Note>> noteLiveData = new MutableLiveData<>();
            noteLiveData.setValue(new ArrayList<>(store.values()));
            return noteLiveData;
        }

        @Override
        public long insert(Note note) {
            store.put(store.size() + 1, note);
            return store.size();
        }

        @Override
        public int delete(int id) {
            store.remove(id);
            return store.size();
        }

        @Override
        public LiveData<List<Note>> filteredNotes(String query) {
            return null;
        }

        @Override
        public void updateNote(Note note) {

        }
    };

    @Before
    public void setUp() {
        viewModel = new NoteViewModel(mockNoteRepository);
    }

    @Test
    public void filterTestMethod() throws InterruptedException {
        viewModel.insertNote(NOTE);
        List<Note> list = viewModel.getNotes().getValue();
        Assert.assertEquals(NOTE, list.get(0));
    }
}
