package ru.ionlabs.easymovies;

import android.app.Application;
import android.content.Context;

import ru.ionlabs.easymovies.di.component.ApplicationComponent;
import ru.ionlabs.easymovies.di.component.DaggerApplicationComponent;
import ru.ionlabs.easymovies.di.module.ApplicationModule;
import timber.log.Timber;

public class EasyMoviesApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    public static EasyMoviesApplication get(Context context) {
        return (EasyMoviesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public ApplicationComponent getComponent() {
        initDagger();
        return mApplicationComponent;
    }

    private void initDagger() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
    }
}
