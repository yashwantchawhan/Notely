package com.notely.ui.details;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.notely.R;
import com.notely.model.Note;
import com.notely.ui.BaseActivity;
import com.notely.ui.list.ListNotesActivity;
import com.notely.utility.Helper;
import com.notely.utility.TextViewUndoRedo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DetailsNoteActivity extends BaseActivity {

    private Menu menu;
    private EditText editTitle;
    private EditText editGist;
    private TextView tvDateUpdated;
    private Note note;
    private TextViewUndoRedo helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);

        setUpToolBar();

        findView();

        getIntentDataAndBind();
    }

    private void getIntentDataAndBind() {
        if (getIntent() != null) {
            note = getIntent().getParcelableExtra(ListNotesActivity.NOTE_ITEM);
            editTitle.setText(note.getTitle());
            editGist.setText(note.getGist());
            tvDateUpdated.setText(getString(R.string.last_updated) + " " + Helper.getDate(this, note.getTime_created()));

        }
    }

    private void findView() {
        editTitle = findViewById(R.id.etTitle);
        editGist = findViewById(R.id.etGist);
        tvDateUpdated = findViewById(R.id.tvDateUpdated);
        helper = new TextViewUndoRedo(editGist);
    }

    private void setUpToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.details_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_undo:
                // add undo while editing
                helper.undo();
                break;
            case R.id.action_save:
                // Save edited story/poem
                if (!TextUtils.isEmpty(editTitle.getText()) && !TextUtils.isEmpty(editGist.getText())) {
                    note.setTitle(editTitle.getText().toString());
                    note.setGist(editGist.getText().toString());
                    compositeDisposable.add(viewModel.insertNote(note)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(Timber::e)
                            .subscribe(() -> {
                                setFocusable(false);
                            }));
                } else {
                    Toast.makeText(this, getString(R.string.aler_info), Toast.LENGTH_SHORT).show();
                }
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
        finishWithTransition();
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        note = null;
    }

    /*
    * Method to manage visibility of menu item and edit fields
    * */
    void setFocusable(boolean focusable) {
        if (focusable) {
            editTitle.setFocusable(true);
            editTitle.setFocusableInTouchMode(true);
            editGist.setFocusable(true);
            editGist.setFocusableInTouchMode(true);
            menu.findItem(R.id.action_undo).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(false);
        } else {
            editTitle.setFocusable(false);
            editTitle.setFocusableInTouchMode(false);
            editGist.setFocusable(false);
            editGist.setFocusableInTouchMode(false);
            menu.findItem(R.id.action_undo).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition();
    }
}
