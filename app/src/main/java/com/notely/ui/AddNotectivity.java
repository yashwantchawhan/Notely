package com.notely.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.model.Note;
import com.notely.utility.NoteType;
import com.notely.viewmodel.NoteViewModel;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class AddNotectivity extends AppCompatActivity {

    private Menu mMenu;
    EditText etTitle;
    EditText etGist;
    RadioGroup rgNoteType;
    String noteType = "";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notectivity);
        ((NoteApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);


        etTitle = findViewById(R.id.etTitle);
        etGist = findViewById(R.id.etGist);
        rgNoteType = findViewById(R.id.rgNoteType);

        rgNoteType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbStory:
                        noteType = NoteType.STORY.toString();
                        break;
                    case R.id.rbPoem:
                        noteType = NoteType.POEM.toString();
                        break;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.add_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_undo:
                break;
            case R.id.action_add:

                Note note = new Note.NoteBuilder()
                        .setTitle(etTitle.getText().toString())
                        .setGist(etGist.getText().toString())
                        .setType(noteType)
                        .setTimeCreated(new Date().getTime())
                        .isStar(false)
                        .isFavourite(false)
                        .build();

                mDisposable.add(mViewModel.insertNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {

                                Log.d("ListNotesActivity", "run: " + "Record Added");
                                finish();
                            }
                        }));


                break;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }


}
