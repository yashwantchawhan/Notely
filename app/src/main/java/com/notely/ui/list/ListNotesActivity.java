package com.notely.ui.list;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.notely.R;
import com.notely.model.Note;
import com.notely.ui.BaseActivity;
import com.notely.ui.add.AddNoteActivity;
import com.notely.ui.details.DetailsNoteActivity;
import com.notely.utility.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ListNotesActivity extends BaseActivity implements ListNotesAdapter.ListNotesAdapterActionListener {
    public static final String NOTE_ITEM = "note_item";

    RecyclerView rvNotes;
    TextView tvNoRecord;
    private List<Note> noteArrayList = new ArrayList<>();
    private ListNotesAdapter listNotesAdapter;
    private View coordinatorLayout;
    private ItemTouchHelper itemTouchHelper;
    private AlertDialog alertDialog;
    private boolean isFilterApplied = false;
    private Observer<List<Note>> noteObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        setUpToolBar();

        setUpViews();

        // Create the observer which updates the UI.
        createObserver();

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        observeLiveData();
    }

    private void observeLiveData() {
        viewModel.getNotes().observe(this, noteObserver);
    }

    private void createObserver() {
        noteObserver = notes -> {
            // Update the UI, in this case, a TextView.
            if (notes != null && notes.size() > 0) {
                Log.d("ListNoteActivity", "onChanged: " + notes.size());
                listNotesAdapter.addItems(notes);
                tvNoRecord.setVisibility(View.GONE);
                rvNotes.setVisibility(View.VISIBLE);

            } else {
                tvNoRecord.setVisibility(View.VISIBLE);
                rvNotes.setVisibility(View.GONE);
            }
        };
    }

    private void setUpViews() {
        rvNotes = findViewById(R.id.rvNotes);
        tvNoRecord = findViewById(R.id.tvNoRecord);
        coordinatorLayout = findViewById(R.id.coordinateLayout);
        listNotesAdapter = new ListNotesAdapter(noteArrayList);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listNotesAdapter.setAdapterActionListener(this);
        rvNotes.setAdapter(listNotesAdapter);
        ItemTouchHelper.Callback callback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, listNotesAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvNotes);
    }

    private void setUpToolBar() {
        getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                openFilterDialog();
                break;
            case R.id.action_add:
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivity(intent,
                        ActivityOptions.makeCustomAnimation(this,
                                R.anim.slide_in_right_medium,
                                R.anim.slide_out_left_medium).toBundle()
                );
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void openFilterDialog() {
        if (isFilterApplied) {
            viewModel.getNotes().observe(this, noteObserver);
            isFilterApplied = false;
        }
        View view = getLayoutInflater().inflate(R.layout.filter_list, null);
        findViews(view);

        alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
    }

    private void findViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvFilterList);
        Button buttonApply = view.findViewById(R.id.apply);
        FilterAdapter filterAdapter = new FilterAdapter(dataManager.getFilters());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(filterAdapter);
        buttonApply.setOnClickListener(v -> {
            alertDialog.dismiss();
            isFilterApplied = true;
            viewModel.filteredNotes(filterAdapter.getFilterList()).observe(ListNotesActivity.this, noteObserver);
        });
    }

    @Override
    public void onListItemClicked(Note note) {
        Intent intent = new Intent(this, DetailsNoteActivity.class);
        intent.putExtra(NOTE_ITEM, note);
        startActivity(intent,
                ActivityOptions.makeCustomAnimation(this,
                        R.anim.slide_in_right_medium,
                        R.anim.slide_out_left_medium).toBundle()
        );

    }

    @Override
    public void onItemSwipe(Note note, int position) {

        //Delete story/poem
        compositeDisposable.add(viewModel.deleteNote(note.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .subscribe(() -> {
                    // remove the item from recycler view
                    listNotesAdapter.removeItem(position);
                }));

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, note.getTitle() + " " + getString(R.string.delete_message), Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.undo), view -> compositeDisposable.add(viewModel.insertNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .subscribe(() -> {
                    // undo is selected, restore the deleted item
                    listNotesAdapter.restoreItem(note, position);
                })));
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onStarOrFavClicked(Note note, int position) {
        //observe click on fav or star
        compositeDisposable.add(viewModel.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .subscribe(() -> {
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
