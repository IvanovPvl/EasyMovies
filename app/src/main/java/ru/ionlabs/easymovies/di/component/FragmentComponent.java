package ru.ionlabs.easymovies.di.component;

import dagger.Subcomponent;
import ru.ionlabs.easymovies.di.FragmentScope;
import ru.ionlabs.easymovies.di.module.FragmentModule;
import ru.ionlabs.easymovies.ui.movies.favorites.FavoritesFragment;
import ru.ionlabs.easymovies.ui.movies.toprated.TopRatedFragment;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(TopRatedFragment fragment);
    void inject(FavoritesFragment fragment);
}
