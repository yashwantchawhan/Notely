package com.notely;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.notely.db.AppDatabase;
import com.notely.model.Note;
import com.notely.repository.NoteDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

/**
 * Created by yashwant on 21/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class NoteDaoTest {
    private NoteDao mNoteDao;
    private AppDatabase mDb;
    Context context;

    private static final Note NOTE = new Note("type", "title", "description", true, false);


    @Before
    public void createDb() {
        context = InstrumentationRegistry.getTargetContext();
        //allowMainThreadQueries just for testing purpose
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        mNoteDao = mDb.noteDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void insertNoteAndReadInList() throws Exception {
        mNoteDao.insert(NOTE);
        List<Note> list = LiveDataTestUtil.getValue(mNoteDao.getNotes());
        assertThat(list.get(0).getTitle(), equalTo(NOTE.getTitle()));

    }

    @Test
    public void loadByIdTest() throws InterruptedException {
        mNoteDao.insert(NOTE);
        Note note = LiveDataTestUtil.getValue(mNoteDao.loadById(1));
        assertThat(note.getTitle(), equalTo(NOTE.getTitle()));
    }

    @Test
    public void deleteAndGetUser() throws InterruptedException {
        // Given that we have a user in the data source
        mNoteDao.insert(NOTE);
        //When we are deleting all users
        mNoteDao.delete(1);
        Note note = LiveDataTestUtil.getValue(mNoteDao.loadById(1));
        assertNull(note);

    }
}
