package com.thisobeystudio.searchviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.thisobeystudio.searchviewexample.adapters.SearchRecyclerViewAdapter;
import com.thisobeystudio.searchviewexample.async.SearchDataAsync;
import com.thisobeystudio.searchviewexample.async.SearchDataAsyncResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleSearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchDataAsyncResponse {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;

    private SearchRecyclerViewAdapter mAdapter;

    private ArrayList<String> mMonths;
    private ArrayList<String> mSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_search);

        setMonths();

        setupSearchView();
        setupSearchRecyclerView();

    }

    private ArrayList<String> getMonths() {
        return mMonths;
    }

    private void setMonths(ArrayList<String> months) {
        this.mMonths = months;
    }

    private ArrayList<String> getSearchResults() {
        return mSearchResults;
    }

    private void setSearchResults(ArrayList<String> mSearchResults) {
        this.mSearchResults = mSearchResults;
    }

    private void setMonths() {
        String[] monthsArray = getResources().getStringArray(R.array.months);
        setMonths(new ArrayList<>(Arrays.asList(monthsArray)));
    }

    private void setupSearchView() {

        if (mSearchView == null)
            mSearchView = findViewById(R.id.search_view);

        mSearchView.setActivated(true);
        mSearchView.setQueryHint("Type your keyword here");
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);

        mSearchView.clearFocus();

        // set query listener
        mSearchView.setOnQueryTextListener(this);
    }

    private void setupSearchRecyclerView() {

        if (mRecyclerView == null)
            mRecyclerView = findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // set layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // this makes scroll smoothly
        mRecyclerView.setNestedScrollingEnabled(false);

        // specify an adapter
        mAdapter = new SearchRecyclerViewAdapter(this, getMonths());

        // set recyclerView adapter
        mRecyclerView.setAdapter(mAdapter);

        // set recyclerView divider decoration
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // set recyclerView VISIBLE
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (TextUtils.isEmpty(query) && mAdapter != null) {
            //mAdapter.swapData(null);
            mAdapter.swapData(getMonths());
        } else {
            SearchDataAsync searchDataAsync = new SearchDataAsync();
            searchDataAsync.delegate = this;
            searchDataAsync.execute(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText) && mAdapter != null) {
            //mAdapter.swapData(null);
            mAdapter.swapData(getMonths());
        } else {
            SearchDataAsync searchDataAsync = new SearchDataAsync();
            searchDataAsync.delegate = this;
            searchDataAsync.execute(newText);
        }
        return false;
    }

    @Override
    public void searchDataAsyncDoInBackground(String params) {
        setSearchResultsByQuery(params);
    }

    @Override
    public void searchDataAsyncOnPostExecute() {
        if (mAdapter != null) mAdapter.swapData(getSearchResults());
    }

    private void setSearchResultsByQuery(String query) {

        if (getMonths() != null && getMonths().size() > 0) {

            setSearchResults(new ArrayList<String>());

            for (int i = 0; i < getMonths().size(); i++) {
                // notice that is checking contains and toLowerCased
                if (getMonths().get(i).toLowerCase().contains(query.toLowerCase())) {
                    String month = getMonths().get(i);
                    getSearchResults().add(month);
                }
            }

        }
    }

    /*
    private void setOnQueryListener() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    */

}
