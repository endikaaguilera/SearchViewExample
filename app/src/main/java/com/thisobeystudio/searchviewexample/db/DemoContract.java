package com.thisobeystudio.searchviewexample.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.thisobeystudio.searchviewexample.R;

/**
 * Created by thisobeystudio on 4/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class DemoContract {

    static final String CONTENT_AUTHORITY = "com.thisobeystudio.searchviewexample";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int PROJECTION_INDEX = 0;

    public static final String[] DAYS_PROJECTION = new String[]{
            DataEntry.DAYS_COLUMN_DAY_DATA
    };

    public static final String[] MONTHS_PROJECTION = new String[]{
            DataEntry.MONTHS_COLUMN_MONTH_DATA
    };

    public static final String[] COUNTRIES_PROJECTION = new String[]{
            DataEntry.COUNTRIES_COLUMN_COUNTRY_DATA
    };

    static final String PATH_DAYS = "days";
    static final String PATH_MONTHS = "months";
    static final String PATH_COUNTRIES = "countries";

    /* Inner class that defines the table contents of the table */
    public static final class DataEntry implements BaseColumns {

        /* The base CONTENT_URIS used to query the table from the content provider */
        public static final Uri CONTENT_URI_DAYS = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_DAYS)
                .build();
        public static final Uri CONTENT_URI_MONTHS = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MONTHS)
                .build();
        public static final Uri CONTENT_URI_COUNTRIES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COUNTRIES)
                .build();

        /* Used internally as the name of our tables. */
        public static final String TABLE_DAYS = "days";
        public static final String TABLE_MONTHS = "months";
        public static final String TABLE_COUNTRIES = "countries";

        /* Used internally as the name of our columns. */
        public static final String DAYS_COLUMN_DAY_DATA = "day_data";
        public static final String MONTHS_COLUMN_MONTH_DATA = "month_data";
        public static final String COUNTRIES_COLUMN_COUNTRY_DATA = "country_data";

    }

    public static void setDBData(Context context) {
        DemoContract.setDaysData(context);
        DemoContract.setMonthsData(context);
        DemoContract.setCountriesData(context);
    }

    private static void setDaysData(Context context) {

        if (context == null) return;

        String[] daysArray = context.getResources().getStringArray(R.array.days);

        ContentValues[] contentValuesArr = new ContentValues[daysArray.length];

        for (int i = 0; i < daysArray.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataEntry.DAYS_COLUMN_DAY_DATA, daysArray[i]);
            contentValuesArr[i] = contentValues;
        }

        /* Get a handle on the ContentResolver to delete and insert data */
        ContentResolver contentResolver = context.getContentResolver();
            /* Delete old data */
        contentResolver.delete(
                DataEntry.CONTENT_URI_DAYS,
                null,
                null);

        /* Insert new data */
        contentResolver.bulkInsert(DataEntry.CONTENT_URI_DAYS, contentValuesArr);
    }

    private static void setMonthsData(Context context) {

        if (context == null) return;

        String[] monthsArray = context.getResources().getStringArray(R.array.months);

        ContentValues[] contentValuesArr = new ContentValues[monthsArray.length];

        for (int i = 0; i < monthsArray.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataEntry.MONTHS_COLUMN_MONTH_DATA, monthsArray[i]);
            contentValuesArr[i] = contentValues;
        }

        /* Get a handle on the ContentResolver to delete and insert data */
        ContentResolver contentResolver = context.getContentResolver();
            /* Delete old data */
        contentResolver.delete(
                DataEntry.CONTENT_URI_MONTHS,
                null,
                null);

        /* Insert new data */
        contentResolver.bulkInsert(DataEntry.CONTENT_URI_MONTHS, contentValuesArr);
    }

    private static void setCountriesData(Context context) {

        if (context == null) return;

        String[] countriesArray = context.getResources().getStringArray(R.array.countries);

        ContentValues[] contentValuesArr = new ContentValues[countriesArray.length];

        for (int i = 0; i < countriesArray.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataEntry.COUNTRIES_COLUMN_COUNTRY_DATA, countriesArray[i]);
            contentValuesArr[i] = contentValues;
        }

        /* Get a handle on the ContentResolver to delete and insert data */
        ContentResolver contentResolver = context.getContentResolver();
            /* Delete old data */
        contentResolver.delete(
                DataEntry.CONTENT_URI_COUNTRIES,
                null,
                null);

        /* Insert new data */
        contentResolver.bulkInsert(DataEntry.CONTENT_URI_COUNTRIES, contentValuesArr);
    }

}
