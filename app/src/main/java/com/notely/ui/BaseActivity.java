package com.notely.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.notely.R;
import com.notely.app.NoteApplication;
import com.notely.viewmodel.NoteViewModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    protected NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NoteApplication) getApplication()).getAppComponent().inject(this);

        viewModel = ViewModelProviders.of(this, mViewModelFactory).get(NoteViewModel.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();

    }

    protected void finishWithTransition() {
        overridePendingTransition(R.anim.slide_in_left_medium, R.anim.slide_out_right_medium);
    }
}
