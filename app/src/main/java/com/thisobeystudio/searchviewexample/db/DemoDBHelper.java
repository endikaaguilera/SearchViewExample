package com.thisobeystudio.searchviewexample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thisobeystudio on 4/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

class DemoDBHelper extends SQLiteOpenHelper {

    // the DataBase name
    private static final String DATABASE_NAME = "demo.db";

    // the DataBase version
    private static final int DATABASE_VERSION = 1;

    DemoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_DAYS_TABLE =
                "CREATE TABLE " + DemoContract.DataEntry.TABLE_DAYS + " (" +
                        DemoContract.DataEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " +
                        DemoContract.DataEntry.DAYS_COLUMN_DAY_DATA + " REAL NOT NULL);";

        final String SQL_CREATE_MONTHS_TABLE =
                "CREATE TABLE " + DemoContract.DataEntry.TABLE_MONTHS + " (" +
                        DemoContract.DataEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " +
                        DemoContract.DataEntry.MONTHS_COLUMN_MONTH_DATA + " REAL NOT NULL);";

        final String SQL_CREATE_COUNTRIES_TABLE =
                "CREATE TABLE " + DemoContract.DataEntry.TABLE_COUNTRIES + " (" +
                        DemoContract.DataEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " +
                        DemoContract.DataEntry.COUNTRIES_COLUMN_COUNTRY_DATA + " REAL NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_DAYS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MONTHS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Nothing to do here since this is an example project
    }

}