package com.notely.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.notely.LiveDataTestUtil;
import com.notely.model.Note;
import com.notely.repository.NoteRepository;
import com.notely.utility.DataManager;
import com.notely.utility.NoteType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by yashwant on 27/01/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class ViewModelTest {

    private NoteViewModel viewModel;
    private static final Note NOTE = new Note(NoteType.POEM.toString(), "title", "description", true, false);

    @Mock
    ViewModelProvider.Factory viewModelProviderFactory;
    @Mock
    DataManager dataManager;
    @Mock
    NoteRepository noteRepository;

    @Before
    public void setUp() {
        viewModel = new NoteViewModel(noteRepository);
    }

    @Test
    public void filterTestMethod() throws InterruptedException {
        viewModel.insertNote(NOTE);
        List<Note> list = LiveDataTestUtil.getValue(viewModel.getNotes());
        Assert.assertEquals(NOTE, list.get(0));


    }
}
