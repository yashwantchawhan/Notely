package com.notely.di;

import android.app.Application;

import com.notely.db.AppDatabase;
import com.notely.db.NoteDao;
import com.notely.repository.NoteRepository;
import com.notely.ui.AddNotectivity;
import com.notely.ui.ListNotesActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yashwant on 21/01/18.
 */

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    NoteDao noteDao();

    AppDatabase appDatabase();

    NoteRepository noteRepository();

    Application application();

    void inject(ListNotesActivity listNotesActivity);

    void inject(AddNotectivity addNotectivity);
}
