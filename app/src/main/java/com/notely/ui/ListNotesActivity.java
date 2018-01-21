package com.notely.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.model.Note;
import com.notely.utility.NoteType;
import com.notely.viewmodel.NoteViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ListNotesActivity extends AppCompatActivity {
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        ((NoteApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);

        findViewById(R.id.addRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note note = new Note();
                note.setType(NoteType.POEM.toString());
                note.setTitle("My first title");
                note.setGist("My first description");
                note.setFavourite(true);
                note.setStar(false);

                mDisposable.add(mViewModel.insertNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {

                                Log.d("ListNotesActivity", "run: " + "Record Added");
                            }
                        }));

            }
        });
        mViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (notes != null) {
                    Log.d("ListNoteActivity", "onChanged: " + notes.size());

                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }
}
