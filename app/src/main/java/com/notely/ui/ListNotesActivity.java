package com.notely.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.notely.app.NoteApplication;
import com.notely.model.Filter;
import com.notely.model.Note;
import com.notely.utility.DataManager;
import com.notely.utility.NoteType;
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
    public static final String NOTE_ITEM = "note_item";
    private static final String FILTER_QUERY = "SELECT * FROM Note WHERE ";
    private static final String SELECT_QUERY_ALL = "SELECT * FROM Note ORDER BY time_created DESC";
    private static final String ORDER_BY = "ORDER BY time_created DESC";

    RecyclerView rvNotes;
    TextView tvNoRecord;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private NoteViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<Note> noteArrayList = new ArrayList<>();
    private ListNotesAdapter listNotesAdapter;
    private View coordinatorLayout;
    private ItemTouchHelper mItemTouchHelper;
    private AlertDialog alertDialog;
    private boolean isFilterApplied=false;
    Observer<List<Note>> noteObserver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        getSupportActionBar().setElevation(0);

        rvNotes = findViewById(R.id.rvNotes);
        tvNoRecord = findViewById(R.id.tvNoRecord);
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


        // Create the observer which updates the UI.
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

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getNotes().observe(this, noteObserver);
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
                if(isFilterApplied){
                    mViewModel.getNotes().observe(this,noteObserver);
                    isFilterApplied=false;
                }
                View view = getLayoutInflater().inflate(R.layout.filter_list, null);
                findviews(view);

               alertDialog= new AlertDialog.Builder(this)
                        .setView(view)
                        .show();


                break;
            case R.id.action_add:
                Intent intent = new Intent(ListNotesActivity.this, AddNotectivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void findviews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvFilterList);
        Button buttonApply = view.findViewById(R.id.apply);
        FilterAdapter filterAdapter = new FilterAdapter(DataManager.getInstance().getFilters());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(filterAdapter);

        buttonApply.setOnClickListener(v -> {
            StringBuilder queryBuilder = new StringBuilder();
            for (Filter filter : filterAdapter.getFilterList()) {
                if (filter.isChecked()) {
                    switch (filter.getFilterType()) {
                        case Poem:
                            queryBuilder.append("type='" + NoteType.POEM.toString()+"'");
                            break;
                        case Star:
                            if (queryBuilder.toString().isEmpty()) {
                                queryBuilder.append("star='" + 1 + "'");
                            } else {
                                queryBuilder.append("AND star='" + 1 + "'");
                            }
                            break;
                        case Favourite:
                            if (queryBuilder.toString().isEmpty()) {
                                queryBuilder.append("favourite='" + 1 + "'");
                            } else {
                                queryBuilder.append("AND favourite='" + 1 + "'");
                            }
                            break;
                        case Story:
                            if (queryBuilder.toString().isEmpty()) {
                                queryBuilder.append("type='" + NoteType.STORY.toString()+"'");
                            } else {
                                if(queryBuilder.toString().contains(NoteType.POEM.toString())){
                                    queryBuilder.append(" OR type='" + NoteType.STORY.toString()+"'");

                                }else {
                                    queryBuilder.append(" AND type='" + NoteType.STORY.toString()+"'");

                                }
                            }
                            break;




                    }
                }
            }
            mViewModel.filteredNotes(queryBuilder.toString().isEmpty()?SELECT_QUERY_ALL: FILTER_QUERY +queryBuilder.toString()+ORDER_BY).observe(ListNotesActivity.this, new Observer<List<Note>>() {
                @Override
                public void onChanged(@Nullable List<Note> notes) {
                    Log.d("", "onChanged: " + notes.size());
                    if (notes != null && notes.size() > 0) {
                        alertDialog.dismiss();
                        isFilterApplied=true;
                        Log.d("ListNoteActivity", "onChanged: " + notes.size());
                        listNotesAdapter.filterList(notes);
                        tvNoRecord.setVisibility(View.GONE);
                        rvNotes.setVisibility(View.VISIBLE);

                    } else {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        rvNotes.setVisibility(View.GONE);
                    }

                }
            });

        });
    }

    @Override
    public void onListItemClicked(Note note) {
        Intent intent = new Intent(this, DetailsNoteActivity.class);
        intent.putExtra(NOTE_ITEM, note);
        startActivity(intent);


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

    @Override
    public void onStarOrFavClicked(Note note,int position) {
        mDisposable.add(mViewModel.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(
                        AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .subscribe(() -> {
                }));
    }

}
