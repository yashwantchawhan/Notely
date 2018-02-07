package com.notely.di;

import com.notely.ui.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yashwant on 21/01/18.
 */

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
}
