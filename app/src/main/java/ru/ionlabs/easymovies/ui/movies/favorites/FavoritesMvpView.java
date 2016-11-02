package ru.ionlabs.easymovies.ui.movies.favorites;

import java.util.List;

import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.ui.base.MvpView;

public interface FavoritesMvpView extends MvpView {

    void showFavorites(List<Movie> movies);
}
