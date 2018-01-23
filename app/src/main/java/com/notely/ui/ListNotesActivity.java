package com.notely.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.model.Note;
import com.notely.utility.RecyclerItemTouchHelper;
import com.notely.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ListNotesActivity extends AppCompatActivity implements ListNotesAdapter.ListNotesAdapterActionListener {
    RecyclerView rvNotes;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<Note> noteArrayList = new ArrayList<>();
    private ListNotesAdapter listNotesAdapter;
    private View coordinatorLayout;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        rvNotes = findViewById(R.id.rvNotes);
        coordinatorLayout = findViewById(R.id.coordinateLayout);
        ((NoteApplication) getApplication()).getAppComponent().inject(this);
        listNotesAdapter = new ListNotesAdapter(noteArrayList);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listNotesAdapter.setAdapterActionListener(this);
        rvNotes.setAdapter(listNotesAdapter);
        ItemTouchHelper.Callback callback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, listNotesAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvNotes);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);

        mViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (notes != null && notes.size() > 0) {
                    Log.d("ListNoteActivity", "onChanged: " + notes.size());
                    listNotesAdapter.addItems(notes);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:
                break;
            case R.id.action_add:
                Intent intent = new Intent(ListNotesActivity.this, AddNotectivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onListItemClicked() {

    }

    @Override
    public void onItemSwipe(Note note, int position) {

        mDisposable.add(mViewModel.deleteNote(note.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .subscribe(() -> {
                    // remove the item from recycler view
                    listNotesAdapter.removeItem(position);
                }));

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, note.getTitle() + " removed from cart!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisposable.add(mViewModel.insertNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(Timber::e)
                        .subscribe(() -> {
                            // undo is selected, restore the deleted item
                            listNotesAdapter.restoreItem(note, position);
                        }));


            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
