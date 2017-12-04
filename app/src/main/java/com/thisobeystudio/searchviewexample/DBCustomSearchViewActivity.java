package com.thisobeystudio.searchviewexample;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.thisobeystudio.searchviewexample.adapters.DBSearchRecyclerViewAdapter;
import com.thisobeystudio.searchviewexample.custom.CustomSearchView;
import com.thisobeystudio.searchviewexample.db.DemoContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thisobeystudio.searchviewexample.db.DemoContract.DataEntry.COUNTRIES_COLUMN_COUNTRY_DATA;
import static com.thisobeystudio.searchviewexample.db.DemoContract.DataEntry.DAYS_COLUMN_DAY_DATA;
import static com.thisobeystudio.searchviewexample.db.DemoContract.DataEntry.MONTHS_COLUMN_MONTH_DATA;

/**
 * Created by thisobeystudio on 4/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class DBCustomSearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        CustomSearchView.SearchItemCallbacks,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private final int DAYS_LOADER_ID = 1111;
    private final int MONTHS_LOADER_ID = 2222;
    private final int COUNTRIES_LOADER_ID = 3333;

    // This TextView is a 'fake' SearchView
    // Handle clicks to show Custom SearchView
    // And shows Search query text
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.custom_search_view_text_view)
    TextView mCustomSearchTextView;
    // Data container RecyclerView
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.custom_search_recycler_view)
    RecyclerView mRecyclerView;

    // RecyclerView Adapter
    private DBSearchRecyclerViewAdapter mAdapter;

    // Parent ConstraintLayout will be used as Custom SearchView parent
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.custom_search_view_parent)
    ConstraintLayout mParent;

    // Search results data
    private Cursor mData;

    // Custom SearchView
    private final CustomSearchView mCustomSearchView = new CustomSearchView();

    // Search query
    private String mQuery;

    private int mLoaderID = COUNTRIES_LOADER_ID;

    private final String BUNDLE_DATA_QUERY = "data_query";
    private final String BUNDLE_DATA_SEARCH_SELECTION = "data_search_selection";
    private final String BUNDLE_DATA_SEARCH_VIEW_VISIBLE = "data_search_view_visible";
    private final String BUNDLE_DATA_LOADER_ID = "data_loader_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);

        ButterKnife.bind(this);

        setTitle(getClass().getSimpleName());

        // Setup views
        setupCustomSearchTextView();
        setupSearchRecyclerView();

        switch (mCustomSearchView.getSelection()) {
            case CustomSearchView.selectionDays:
                mLoaderID = DAYS_LOADER_ID;
                break;
            case CustomSearchView.selectionMonths:
                mLoaderID = MONTHS_LOADER_ID;
                break;
            case CustomSearchView.selectionCountries:
                mLoaderID = COUNTRIES_LOADER_ID;
                break;
        }

        if (savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_DATA_QUERY)
                && savedInstanceState.containsKey(BUNDLE_DATA_SEARCH_SELECTION)
                && savedInstanceState.containsKey(BUNDLE_DATA_SEARCH_VIEW_VISIBLE)
                && savedInstanceState.containsKey(BUNDLE_DATA_LOADER_ID)) {

            setQuery(savedInstanceState.getString(BUNDLE_DATA_QUERY));

            mCustomSearchView.setSelection(
                    savedInstanceState.getInt(BUNDLE_DATA_SEARCH_SELECTION,
                            CustomSearchView.selectionCountries));

            mLoaderID = savedInstanceState.getInt(BUNDLE_DATA_LOADER_ID, COUNTRIES_LOADER_ID);

            boolean isVisible = savedInstanceState.getBoolean(BUNDLE_DATA_SEARCH_VIEW_VISIBLE);

            if (isVisible) {
                showCustomSearchView();
            }

            mCustomSearchTextView.setText(getQuery());

        }

        getSupportLoaderManager().initLoader(mLoaderID,
                null, DBCustomSearchViewActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(BUNDLE_DATA_QUERY, getQuery());
        outState.putInt(BUNDLE_DATA_SEARCH_SELECTION, mCustomSearchView.getSelection());
        outState.putBoolean(BUNDLE_DATA_SEARCH_VIEW_VISIBLE, mCustomSearchView.isVisible());
        outState.putInt(BUNDLE_DATA_LOADER_ID, mLoaderID);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // Check is Custom SearchView is present
        if (mCustomSearchView.isVisible() && mCustomSearchView.isCancelable()) {
            mCustomSearchView.removeCustomSearchView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setQuery(query);
        restartLoaderBySelection();
        // Check is Custom SearchView is present
        if (mCustomSearchView.isVisible() && mCustomSearchView.isCancelable()) {
            mCustomSearchView.removeCustomSearchView();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        setQuery(newText);
        restartLoaderBySelection();
        return false;
    }

    @Override
    public void onSearchItemCallbacks(int selection) {
        restartLoaderBySelection();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        Uri uri;
        String[] projection;
        String selection;

        if (loaderId == DAYS_LOADER_ID) {
            uri = DemoContract.DataEntry.CONTENT_URI_DAYS;
            projection = DemoContract.DAYS_PROJECTION;
            selection = DAYS_COLUMN_DAY_DATA + " like '%" + getQuery() + "%'";
        } else if (loaderId == MONTHS_LOADER_ID) {
            uri = DemoContract.DataEntry.CONTENT_URI_MONTHS;
            projection = DemoContract.MONTHS_PROJECTION;
            selection = MONTHS_COLUMN_MONTH_DATA + " like '%" + getQuery() + "%'";
        } else if (loaderId == COUNTRIES_LOADER_ID) {
            uri = DemoContract.DataEntry.CONTENT_URI_COUNTRIES;
            projection = DemoContract.COUNTRIES_PROJECTION;
            selection = COUNTRIES_COLUMN_COUNTRY_DATA + " like '%" + getQuery() + "%'";
        } else {
            throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

        if (TextUtils.isEmpty(getQuery())) {
            selection = null;
        }

        return new CursorLoader(this, uri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            if (mAdapter != null) mAdapter.swapData(null);
            setData(null);
            return;
        }

        setData(data);
        // Update RecyclerView data
        if (mAdapter != null) mAdapter.swapData(getData());
        // Update 'fake' SearchView text
        updateCustomSearchTextView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) mAdapter.swapData(null);
        setData(null);
    }

    private void restartLoaderBySelection() {

        switch (mCustomSearchView.getSelection()) {
            case CustomSearchView.selectionDays:
                mLoaderID = DAYS_LOADER_ID;
                break;
            case CustomSearchView.selectionMonths:
                mLoaderID = MONTHS_LOADER_ID;
                break;
            case CustomSearchView.selectionCountries:
                mLoaderID = COUNTRIES_LOADER_ID;
                break;
        }

        getSupportLoaderManager().restartLoader(mLoaderID,
                null, DBCustomSearchViewActivity.this);
    }

    /**
     * Setup 'fake' SearchView
     */
    private void setupCustomSearchTextView() {

        mCustomSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if present to prevent duplicates
                if (mCustomSearchView.isVisible()) {
                    mCustomSearchView.removeCustomSearchView();
                } else {
                    showCustomSearchView();
                }

            }
        });

    }

    /**
     * Update 'fake' SearchView text
     */
    private void updateCustomSearchTextView() {
        if (!TextUtils.isEmpty(getQuery())) {
            mCustomSearchTextView.setText(getQuery());
        } else {
            mCustomSearchTextView.setText(getString(R.string.query_hint));
        }
    }

    /**
     * Setup RecyclerView
     */
    private void setupSearchRecyclerView() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // set layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // this makes scroll smoothly
        mRecyclerView.setNestedScrollingEnabled(false);

        // specify an adapter
        mAdapter = new DBSearchRecyclerViewAdapter(this, getData());

        // set recyclerView adapter
        mRecyclerView.setAdapter(mAdapter);

        // set recyclerView divider decoration
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // set recyclerView VISIBLE
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    /**
     * Show Custom SearchView
     */
    private void showCustomSearchView() {
        // Show Custom SearchView
        mCustomSearchView.showCustomSearchView(
                DBCustomSearchViewActivity.this,
                DBCustomSearchViewActivity.this,
                mParent,
                getQuery());

        // set callbacks
        mCustomSearchView.setSearchItemCallbacks(DBCustomSearchViewActivity.this);
    }

    /**
     * @param query current search query
     */
    private void setQuery(String query) {
        this.mQuery = query;
    }

    /**
     * @return current search query
     */
    private String getQuery() {
        return mQuery;
    }

    /**
     * @param data current selected data
     */
    private void setData(Cursor data) {
        this.mData = data;
    }

    /**
     * @return current selected data
     */
    private Cursor getData() {
        return mData;
    }

}
