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

import com.thisobeystudio.searchviewexample.adapters.SearchRecyclerViewAdapter;
import com.thisobeystudio.searchviewexample.async.SearchDataAsync;
import com.thisobeystudio.searchviewexample.async.SearchDataAsyncResponse;
import com.thisobeystudio.searchviewexample.custom.CustomSearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomSearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchDataAsyncResponse,
        CustomSearchView.SearchItemCallbacks {

    private TextView mCustomSearchTextView;
    private RecyclerView mRecyclerView;

    private SearchRecyclerViewAdapter mAdapter;

    private ConstraintLayout mParent;

    private ArrayList<String> mData;
    private ArrayList<String> mSearchResults;

    private final CustomSearchView mCustomSearchView = new CustomSearchView();

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);

        mParent = findViewById(R.id.custom_search_view_parent);

        setData();

        setupCustomSearchTextView();
        setupSearchRecyclerView();

    }

    @Override
    public void onBackPressed() {
        if (mCustomSearchView.isVisible() && mCustomSearchView.isCancelable()) {
            mCustomSearchView.removeCustomSearchView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SearchDataAsync searchDataAsync = new SearchDataAsync();
        searchDataAsync.delegate = this;
        searchDataAsync.execute(query);

        if (mCustomSearchView.isVisible() && mCustomSearchView.isCancelable()) {
            mCustomSearchView.removeCustomSearchView();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        SearchDataAsync searchDataAsync = new SearchDataAsync();
        searchDataAsync.delegate = this;
        searchDataAsync.execute(newText);
        return false;
    }

    @Override
    public void onSearchItemCallbacks(int selection) {
        mCustomSearchView.setSelection(selection);
        setSearchResultsByQuery(getQuery());
        if (mAdapter != null) mAdapter.swapData(getSearchResults());
        mCustomSearchView.getSearchView().setQuery(getQuery(), false);  // set true if wants it to submit on item click
    }

    @Override
    public void searchDataAsyncDoInBackground(String params) {
        setQuery(params);
        setSearchResultsByQuery(getQuery());
    }

    @Override
    public void searchDataAsyncOnPostExecute() {
        if (mAdapter != null) mAdapter.swapData(getSearchResults());
    }

    private void setQuery(String query) {
        this.mQuery = query;
    }

    private String getQuery() {
        return mQuery;
    }

    private ArrayList<String> getData() {
        return mData;
    }

    private void setData(ArrayList<String> data) {
        this.mData = data;
    }

    private ArrayList<String> getSearchResults() {
        return mSearchResults;
    }

    private void setSearchResults(ArrayList<String> mSearchResults) {
        this.mSearchResults = mSearchResults;
    }

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

    private void setupCustomSearchTextView() {

        if (mCustomSearchTextView == null)
            mCustomSearchTextView = findViewById(R.id.custom_search_view_text_view);

        mCustomSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCustomSearchView.isVisible()) {
                    mCustomSearchView.removeCustomSearchView();
                } else {
                    showCustomSearchView();
                }

            }
        });

    }

    private void showCustomSearchView() {

        mCustomSearchView.showCustomSearchView(
                CustomSearchViewActivity.this,
                CustomSearchViewActivity.this,
                mParent,
                getQuery());

        // set callbacks
        mCustomSearchView.setSearchItemCallbacks(CustomSearchViewActivity.this);

    }

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

    private void setSearchResultsByQuery(String query) {

        setData();

        updateCustomSearchTextView();

        if (getData() != null && getData().size() > 0) {

            setSearchResults(new ArrayList<String>());

            if (!TextUtils.isEmpty(query)) {

                for (int i = 0; i < getData().size(); i++) {
                    // notice that is checking contains and toLowerCased
                    if (getData().get(i).toLowerCase().contains(query.toLowerCase())) {
                        String month = getData().get(i);
                        getSearchResults().add(month);
                    }
                }

            } else {
                setSearchResults(getData());
            }
        }

    }

    private void updateCustomSearchTextView() {
        if (!TextUtils.isEmpty(getQuery())) {
            mCustomSearchTextView.setText(getQuery());
        } else {
            mCustomSearchTextView.setText(getString(R.string.type_your_keyword_here));
        }
    }

}
