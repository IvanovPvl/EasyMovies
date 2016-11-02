package ru.ionlabs.easymovies.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.ionlabs.easymovies.di.ApplicationContext;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }
}
