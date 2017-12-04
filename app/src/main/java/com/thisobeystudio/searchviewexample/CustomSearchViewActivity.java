package com.thisobeystudio.searchviewexample;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.thisobeystudio.searchviewexample.adapters.SearchRecyclerViewAdapter;
import com.thisobeystudio.searchviewexample.async.SearchDataAsync;
import com.thisobeystudio.searchviewexample.async.SearchDataAsyncResponse;
import com.thisobeystudio.searchviewexample.custom.CustomSearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomSearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchDataAsyncResponse,
        CustomSearchView.SearchItemCallbacks {

    // This TextView is a 'fake' SearchView
    // Handle clicks to show Custom SearchView
    // And shows Search query text
    private TextView mCustomSearchTextView;
    // Data container RecyclerView
    private RecyclerView mRecyclerView;

    // RecyclerView Adapter
    private SearchRecyclerViewAdapter mAdapter;

    // Parent ConstraintLayout will be used as Custom SearchView parent
    private ConstraintLayout mParent;

    // Demo data
    private ArrayList<String> mData;
    // Search results data
    private ArrayList<String> mSearchResults;

    // Custom SearchView
    private final CustomSearchView mCustomSearchView = new CustomSearchView();

    // Search query
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);

        // Find parent
        mParent = findViewById(R.id.custom_search_view_parent);

        // Custom SearchView selection can be set as follows
        mCustomSearchView.setSelection(CustomSearchView.selectionCountries);

        // Set Data
        setData();

        // Setup views
        setupCustomSearchTextView();
        setupSearchRecyclerView();
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
        // Execute Search query async
        SearchDataAsync searchDataAsync = new SearchDataAsync();
        searchDataAsync.delegate = this;
        searchDataAsync.execute(query);

        // Check is Custom SearchView is present
        if (mCustomSearchView.isVisible() && mCustomSearchView.isCancelable()) {
            mCustomSearchView.removeCustomSearchView();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Execute Search newText async
        SearchDataAsync searchDataAsync = new SearchDataAsync();
        searchDataAsync.delegate = this;
        searchDataAsync.execute(newText);
        return false;
    }

    @Override
    public void onSearchItemCallbacks(int selection) {
        // Update Custom searchView selection
        mCustomSearchView.setSelection(selection);

        // Update query
        setSearchResultsByQuery(getQuery());

        // swap RecyclerView data
        if (mAdapter != null) mAdapter.swapData(getSearchResults());
    }

    @Override
    public void searchDataAsyncDoInBackground(String params) {
        // Update query
        setQuery(params);
        // Update results data
        setSearchResultsByQuery(getQuery());
    }

    @Override
    public void searchDataAsyncOnPostExecute() {
        // Update RecyclerView data
        if (mAdapter != null) mAdapter.swapData(getSearchResults());
        // Update 'fake' SearchView text
        updateCustomSearchTextView();
    }

    /**
     * Setup 'fake' SearchView
     */
    private void setupCustomSearchTextView() {

        if (mCustomSearchTextView == null)
            mCustomSearchTextView = findViewById(R.id.custom_search_view_text_view);

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

        if (mRecyclerView == null)
            mRecyclerView = findViewById(R.id.custom_search_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // set layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // this makes scroll smoothly
        mRecyclerView.setNestedScrollingEnabled(false);

        // specify an adapter
        mAdapter = new SearchRecyclerViewAdapter(this, getData());

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
                CustomSearchViewActivity.this,
                CustomSearchViewActivity.this,
                mParent,
                getQuery());

        // set callbacks
        mCustomSearchView.setSearchItemCallbacks(CustomSearchViewActivity.this);
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
     * Sets data based on Custom SearchView selection
     */
    private void setData() {

        String[] stringArray;

        switch (mCustomSearchView.getSelection()) {
            case CustomSearchView.selectionDays:
                stringArray = getResources().getStringArray(R.array.days);
                break;
            case CustomSearchView.selectionMonths:
                stringArray = getResources().getStringArray(R.array.months);
                break;
            case CustomSearchView.selectionCountries:
            default:
                stringArray = getResources().getStringArray(R.array.countries);
                break;
        }

        setData(new ArrayList<>(Arrays.asList(stringArray)));
    }

    /**
     * @param data current selected data
     */
    private void setData(ArrayList<String> data) {
        this.mData = data;
    }

    /**
     * @return current selected data
     */
    private ArrayList<String> getData() {
        return mData;
    }

    /**
     * @param mSearchResults current search results data
     */
    private void setSearchResults(ArrayList<String> mSearchResults) {
        this.mSearchResults = mSearchResults;
    }

    /**
     * @return current search results data
     */
    private ArrayList<String> getSearchResults() {
        return mSearchResults;
    }

    /**
     * @param query current search query
     */
    private void setSearchResultsByQuery(String query) {

        setData();

        if (getData() != null && getData().size() > 0) {

            // reset results data
            setSearchResults(new ArrayList<String>());

            if (!TextUtils.isEmpty(query)) {

                for (int i = 0; i < getData().size(); i++) {
                    // notice that is checking contains and toLowerCased
                    if (getData().get(i).toLowerCase().contains(query.toLowerCase())) {
                        String data = getData().get(i);
                        getSearchResults().add(data);
                    }
                }

            } else {
                // set all data since query is empty
                setSearchResults(getData());
            }
        } else {
            Toast.makeText(CustomSearchViewActivity.this, R.string.no_data,
                    Toast.LENGTH_SHORT).show();
        }

    }

}
