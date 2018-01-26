package com.notely.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.model.Note;
import com.notely.utility.TextViewUndoRedo;
import com.notely.viewmodel.NoteViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DetailsNoteActivity extends AppCompatActivity {

    private Menu mMenu;
    private EditText editTitle;
    private EditText editGist;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Note note;
    TextViewUndoRedo helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NoteApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_details_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);
        editTitle = findViewById(R.id.etTitle);
        editGist = findViewById(R.id.etGist);
        // pass edittext object to TextViewUndoRedo class
         helper= new TextViewUndoRedo(editGist);


        if (getIntent() != null) {
            note = getIntent().getParcelableExtra(ListNotesActivity.NOTE_ITEM);
            editTitle.setText(note.getTitle());
            editGist.setText(note.getGist());

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.details_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_undo:
                helper.undo(); // perform undo
                break;
            case R.id.action_save:
                note.setTitle(editTitle.getText().toString());
                note.setGist(editGist.getText().toString());
                mDisposable.add(mViewModel.insertNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(Timber::e)
                        .subscribe(() -> {
                            setFocusable(false);
                        }));
                break;
            case R.id.action_edit:
                setFocusable(true);

                break;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        note=null;
        mDisposable.dispose();
    }

    void setFocusable(boolean focusable) {
        if (focusable) {
            editTitle.setFocusable(true);
            editTitle.setFocusableInTouchMode(true);
            editGist.setFocusable(true);
            editGist.setFocusableInTouchMode(true);
            mMenu.findItem(R.id.action_undo).setVisible(true);
            mMenu.findItem(R.id.action_save).setVisible(true);
            mMenu.findItem(R.id.action_edit).setVisible(false);
        } else {
            editTitle.setFocusable(false);
            editTitle.setFocusableInTouchMode(false);
            editGist.setFocusable(false);
            editGist.setFocusableInTouchMode(false);
            mMenu.findItem(R.id.action_undo).setVisible(false);
            mMenu.findItem(R.id.action_save).setVisible(false);
            mMenu.findItem(R.id.action_edit).setVisible(true);
        }
    }
}
