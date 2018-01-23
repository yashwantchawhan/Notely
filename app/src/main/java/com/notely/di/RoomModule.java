package com.notely.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.notely.db.AppDatabase;
import com.notely.db.NoteDao;
import com.notely.repository.NoteDataSource;
import com.notely.repository.NoteRepository;
import com.notely.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yashwant on 21/01/18.
 */

@Module
public class RoomModule {

    private AppDatabase demoDatabase;

    public RoomModule(Application mApplication) {
        demoDatabase = Room.databaseBuilder(mApplication, AppDatabase.class, "note-database").build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return demoDatabase;
    }

    @Singleton
    @Provides
    NoteDao providesProductDao(AppDatabase demoDatabase) {
        return demoDatabase.noteDao();
    }

    @Singleton
    @Provides
    NoteRepository productRepository(NoteDao productDao) {
        return new NoteDataSource(productDao);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideListIssuesViewModelFactory(ViewModelFactory factory) {
        return factory;
    }
}
