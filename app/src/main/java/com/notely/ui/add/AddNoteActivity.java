package com.notely.ui.add;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.model.Note;
import com.notely.utility.NoteType;
import com.notely.utility.TextViewUndoRedo;
import com.notely.viewmodel.NoteViewModel;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddNoteActivity extends AppCompatActivity {

    private static final long DELAY_IN_FINISH = 1000;
    private Menu mMenu;
    private EditText etTitle;
    private EditText etGist;
    private RadioGroup rgNoteType;
    private ProgressBar progressBar;
    private LinearLayout parentLinearLayout;
    private String noteType = "";
    private TextViewUndoRedo helper;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notectivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        ((NoteApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);
        parentLinearLayout = findViewById(R.id.parentLinearLayout);
        progressBar = findViewById(R.id.progressBar);
        etTitle = findViewById(R.id.etTitle);
        etGist = findViewById(R.id.etGist);
        helper = new TextViewUndoRedo(etGist);
        rgNoteType = findViewById(R.id.rgNoteType);
        rgNoteType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbStory:
                    noteType = NoteType.STORY.toString();
                    break;
                case R.id.rbPoem:
                    noteType = NoteType.POEM.toString();
                    break;
            }
        });

        etGist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mMenu.findItem(R.id.action_undo).setVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        parentLinearLayout.setVisibility(View.VISIBLE);

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
                helper.undo();
                break;
            case R.id.action_add:

                if (!TextUtils.isEmpty(etTitle.getText()) && !TextUtils.isEmpty(etGist.getText())) {
                    mMenu.findItem(R.id.action_undo).setVisible(false);
                    showLoading();
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
                            .doOnError(Timber::e)
                            .subscribe(() -> {
                                final Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    finish();
                                    hideLoading();

                                }, DELAY_IN_FINISH);


                            }));
                } else {
                    Toast.makeText(this, getString(R.string.aler_info), Toast.LENGTH_SHORT).show();

                }


                break;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }

    public void showLoading() {
        parentLinearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
