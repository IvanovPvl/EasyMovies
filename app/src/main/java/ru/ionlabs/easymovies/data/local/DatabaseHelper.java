package ru.ionlabs.easymovies.data.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.ionlabs.easymovies.data.model.Movie;
import static ru.ionlabs.easymovies.data.local.DbContract.MovieEntry;
import static ru.ionlabs.easymovies.data.local.DbContract.FavoritesEntry;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    private static String sSelect = "SELECT t1.*, t2.* ";
    private static String sTables =
            "FROM " + MovieEntry.TABLE_NAME + " t1 " +
                    "LEFT JOIN " + FavoritesEntry.TABLE_NAME + " t2 " +
                    "ON t1." + MovieEntry._ID + " = t2." + FavoritesEntry.COLUMN_MOVIE_ID;

    @Inject
    DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public Observable<List<Movie>> setMovies(final Collection<Movie> movies, boolean needDelete) {
        Timber.d("Store movies to database.");
        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) return;
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                if (needDelete) {
                    mDb.delete(MovieEntry.TABLE_NAME, null);
                }

                List<Movie> movieList = new ArrayList<>();
                for (Movie movie : movies) {
                    long result = mDb.insert(MovieEntry.TABLE_NAME, MovieEntry.toContentValues(movie),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result > 0) {
                        movieList.add(movie);
                    }
                }

                if (movieList.size() > 0) {
                    subscriber.onNext(movieList);
                }
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<List<Movie>> getMovies(int offset, int limit) {
        Timber.d("Get top movies from database.");
        return mDb.createQuery(MovieEntry.TABLE_NAME,
                sSelect + sTables + " LIMIT " + limit + " OFFSET " + offset)
                .mapToList(MovieEntry::parseCursor);
    }

    public Observable<List<Movie>> getMovies() {
        Timber.d("Get top movies from database.");
        return mDb.createQuery(MovieEntry.TABLE_NAME,
                sSelect + sTables)
                .mapToList(MovieEntry::parseCursor);
    }

    public Observable<Movie> getMovieById(int movieId) {
        return mDb.createQuery(MovieEntry.TABLE_NAME,
                sSelect + sTables + " WHERE " + MovieEntry._ID + " = ?", "" + movieId)
                .mapToOne(MovieEntry::parseCursor);
    }

    public Observable<List<Movie>> getFavorites() {
        Timber.d("Get favorite movies from database.");
        List<String> tables = Arrays.asList(MovieEntry.TABLE_NAME, FavoritesEntry.TABLE_NAME);
        return mDb.createQuery(tables,
                sSelect + sTables + " WHERE movie_id != 0")
                .mapToList(MovieEntry::parseCursor);
    }

    public Single<Boolean> updateFavorite(int movieId, boolean isFavorite) {
        return Single.create(subscriber -> {
            if (subscriber.isUnsubscribed()) return;

            long result;
            if (isFavorite) {
                ContentValues values = new ContentValues();
                values.put(FavoritesEntry.COLUMN_MOVIE_ID, movieId);
                result = mDb.insert(FavoritesEntry.TABLE_NAME, values);
            } else {
                String whereClause = FavoritesEntry.COLUMN_MOVIE_ID + " = ?";
                String whereArg = "" + movieId;
                result = mDb.delete(FavoritesEntry.TABLE_NAME, whereClause, whereArg);
            }

            if (result > 0) {
                subscriber.onSuccess(isFavorite);
            } else {
                subscriber.onError(new UnsupportedOperationException("No movies updated."));
            }
        });
    }
}
