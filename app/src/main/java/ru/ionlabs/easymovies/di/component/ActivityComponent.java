package ru.ionlabs.easymovies.di.component;

import dagger.Subcomponent;
import ru.ionlabs.easymovies.di.ActivityScope;
import ru.ionlabs.easymovies.di.module.ActivityModule;
import ru.ionlabs.easymovies.ui.detail.DetailActivity;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(DetailActivity activity);
}
