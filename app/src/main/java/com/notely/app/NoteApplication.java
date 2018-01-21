package com.notely.app;

import android.app.Application;

import com.notely.di.AppComponent;
import com.notely.di.AppModule;
import com.notely.di.DaggerAppComponent;
import com.notely.di.RoomModule;

/**
 * Created by yashwant on 21/01/18.
 */

public class NoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initDagger(NoteApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .roomModule(new RoomModule(application))
                .build();
    }
}
