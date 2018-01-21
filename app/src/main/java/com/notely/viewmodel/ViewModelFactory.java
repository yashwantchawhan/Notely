package com.notely.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

/**
 * Created by yashwant on 21/01/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private NoteViewModel mViewModel;

    @Inject
    public ViewModelFactory(NoteViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
