package ru.ionlabs.easymovies.ui.detail;

import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.ui.base.MvpView;

public interface DetailActivityMvpView extends MvpView {

    void showMovie(Movie movie);
    void showFavorite(boolean isFavorite);
}
