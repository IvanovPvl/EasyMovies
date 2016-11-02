package ru.ionlabs.easymovies.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.data.local.DatabaseHelper;
import ru.ionlabs.easymovies.di.ApplicationContext;
import ru.ionlabs.easymovies.di.module.ApiServiceModule;
import ru.ionlabs.easymovies.di.module.ApplicationModule;
import ru.ionlabs.easymovies.di.module.FragmentModule;

@Singleton
@Component(modules = { ApplicationModule.class, ApiServiceModule.class })
public interface ApplicationComponent {

    @ApplicationContext Context context();

    ConfigPersistentComponent configPersistentComponent();
    FragmentComponent fragmentComponent(FragmentModule fragmentModule);
}
