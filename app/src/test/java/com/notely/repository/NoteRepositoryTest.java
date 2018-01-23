package com.notely.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.notely.db.NoteDao;
import com.notely.model.Note;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.mock;

/**
 * Created by yashwant on 22/01/18.
 */

@RunWith(JUnit4.class)
public class NoteRepositoryTest {

    private NoteDao noteDao;
    private NoteRepository repo;
    private static final Note NOTE = new Note("type", "title", "description", true, false);


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        noteDao = mock(NoteDao.class);
        repo = mock(NoteRepository.class);
    }

    @Test
    public void insertNoteTest() {
        repo.insert(NOTE);
        //TODO add assertion condition

    }
}
