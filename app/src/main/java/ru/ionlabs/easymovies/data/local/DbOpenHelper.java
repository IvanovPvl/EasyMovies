package ru.ionlabs.easymovies.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.ionlabs.easymovies.di.ApplicationContext;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "easymovies.db";
    private static final int DATABASE_VERSION = 1;

    @Inject
    DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(DbContract.MovieEntry.CREATE);
            db.execSQL(DbContract.FavoritesEntry.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
