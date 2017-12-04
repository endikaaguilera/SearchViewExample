package com.thisobeystudio.searchviewexample.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by thisobeystudio on 4/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class DemoDataProvider extends ContentProvider {

    private DemoDBHelper mDbHelper;

    /*
    * These constant will be used to match URIs with the data they are looking for. We will take
    * advantage of the UriMatcher class to make that matching MUCH easier than doing something
    * ourselves, such as using regular expressions.
    */
    private static final int CODE_DAYS = 100;
    private static final int CODE_MONTHS = 200;
    private static final int CODE_COUNTRIES = 300;

    private final UriMatcher sUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DemoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DemoContract.PATH_DAYS, CODE_DAYS);
        matcher.addURI(authority, DemoContract.PATH_MONTHS, CODE_MONTHS);
        matcher.addURI(authority, DemoContract.PATH_COUNTRIES, CODE_COUNTRIES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DemoDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        switch (sUriMatcher.match(uri)) {

            case CODE_DAYS:

                return myInsert(values, DemoContract.DataEntry.TABLE_DAYS, uri);

            case CODE_MONTHS:

                return myInsert(values, DemoContract.DataEntry.TABLE_MONTHS, uri);

            case CODE_COUNTRIES:

                return myInsert(values, DemoContract.DataEntry.TABLE_COUNTRIES, uri);
            
            default:
                return super.bulkInsert(uri, values);

        }

    }

    /**
     * @param values insert values
     * @param table  target table
     * @param uri    query Uri
     * @return rowsInserted
     */
    private int myInsert(ContentValues[] values, String table, Uri uri) {

        SQLiteDatabase db;

        db = mDbHelper.getWritableDatabase();
        db.beginTransaction();

        int rowsInserted = 0;

        try {
            for (ContentValues value : values) {

                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        ContentResolver contentResolver = mGetContentResolver(getContext());
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }

        return rowsInserted;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_DAYS: {
                cursor = mDbHelper.getReadableDatabase().query(
                        DemoContract.DataEntry.TABLE_DAYS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_MONTHS: {
                cursor = mDbHelper.getReadableDatabase().query(
                        DemoContract.DataEntry.TABLE_MONTHS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_COUNTRIES: {
                cursor = mDbHelper.getReadableDatabase().query(
                        DemoContract.DataEntry.TABLE_COUNTRIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        ContentResolver contentResolver = mGetContentResolver(getContext());
        if (contentResolver != null) {
            cursor.setNotificationUri(contentResolver, uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException(
                "Not implemented, using bulkInsert instead");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_DAYS:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        DemoContract.DataEntry.TABLE_DAYS,
                        selection,
                        selectionArgs);
                break;

            case CODE_MONTHS:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        DemoContract.DataEntry.TABLE_MONTHS,
                        selection,
                        selectionArgs);
                break;

            case CODE_COUNTRIES:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        DemoContract.DataEntry.TABLE_COUNTRIES,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues contentValues,
            @Nullable String s,
            @Nullable String[] strings) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @param context context
     * @return contentResolver
     */
    private ContentResolver mGetContentResolver(Context context) {
        try {
            if (context == null) return null;
            else return context.getContentResolver();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
