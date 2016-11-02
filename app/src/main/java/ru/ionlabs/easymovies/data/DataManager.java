package ru.ionlabs.easymovies.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.ionlabs.easymovies.BuildConfig;
import ru.ionlabs.easymovies.data.local.DatabaseHelper;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.data.remote.MoviesService;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class DataManager {

    public static final int PER_PAGE = 20;

    private final DatabaseHelper mDb;
    private final MoviesService.Api mApi;

    @Inject
    public DataManager(DatabaseHelper db, MoviesService.Api api) {
        mDb = db;
        mApi = api;
    }

    public Observable<List<Movie>> getMovies(int page, boolean needRefresh) {
        if (needRefresh) {
            return syncMovies(1);
        }

        return mDb.getMovies(PER_PAGE * (page - 1), PER_PAGE)
                .flatMap(movies ->
                        movies.size() == 0 ? syncMovies(page) : Observable.just(movies));
    }

    public Observable<Movie> getMovieById(int movieId) {
        return mDb.getMovieById(movieId);
    }

    public Observable<List<Movie>> getFavorites() {
        return mDb.getFavorites();
    }

    public Single<Boolean> updateFavorite(int movieId, boolean isFavorite) {
        return mDb.updateFavorite(movieId, isFavorite);
    }

    private Observable<List<Movie>> syncMovies(int page) {
        Timber.d("Sync movies from Api.");
        return mApi.getTopRatedMovies(BuildConfig.API_KEY, page)
                .flatMap(movies -> {
                    boolean needDelete = page == 1;
                    return mDb.setMovies(movies, needDelete);
                });
    }
}
