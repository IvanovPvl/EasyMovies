package ru.ionlabs.easymovies.ui.movies.toprated;

import java.util.List;

import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.ui.base.MvpView;

public interface TopRatedMvpView extends MvpView {

    void showMovies(List<Movie> movies);
    void updateMovies(List<Movie> movies);
    void hideLoader();
}
