package ru.ionlabs.easymovies.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import ru.ionlabs.easymovies.data.model.Movie;

class DbContract {

    static final class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "movies";

        static final String COLUMN_TITLE = "title";
        static final String COLUMN_POSTER_PATH = "poster_path";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_RELEASE_DATE = "release_date";

        static final int INDEX_ID = 0;
        static final int INDEX_TITLE = 1;
        static final int INDEX_POSTER_PATH = 2;
        static final int INDEX_BACKDROP_PATH = 3;
        static final int INDEX_OVERVIEW = 4;
        static final int INDEX_RELEASE_DATE = 5;
        static final int INDEX_FAVORITE = 6;

        static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_POSTER_PATH + " TEXT, " +
                        COLUMN_BACKDROP_PATH + " TEXT, " +
                        COLUMN_OVERVIEW + " TEXT, " +
                        COLUMN_RELEASE_DATE + " TEXT)";

        static ContentValues toContentValues(Movie movie) {
            ContentValues values = new ContentValues();
            values.put(_ID, movie.id());
            values.put(COLUMN_TITLE, movie.title());
            values.put(COLUMN_POSTER_PATH, movie.posterPath());
            values.put(COLUMN_BACKDROP_PATH, movie.backdropPath());
            values.put(COLUMN_OVERVIEW, movie.overview());
            values.put(COLUMN_RELEASE_DATE, movie.releaseDate());
            return values;
        }

        static Movie parseCursor(Cursor cursor) {
            int id = cursor.getInt(INDEX_ID);
            String title = cursor.getString(INDEX_TITLE);
            String posterPath = cursor.getString(INDEX_POSTER_PATH);
            String backdropPath = cursor.getString(INDEX_BACKDROP_PATH);
            String overview = cursor.getString(INDEX_OVERVIEW);
            String releaseDate = cursor.getString(INDEX_RELEASE_DATE);
            int favorite = cursor.getInt(INDEX_FAVORITE) > 0 ? 1 : 0;
            return Movie.create(id, title, posterPath, backdropPath, overview, releaseDate, favorite);
        }
    }

    static final class FavoritesEntry {
        static final String TABLE_NAME = "favorites";

        static final String COLUMN_MOVIE_ID = "movie_id";

        static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                        "FOREIGN KEY (" + COLUMN_MOVIE_ID + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + "(" + BaseColumns._ID + ")) ";
    }
}
