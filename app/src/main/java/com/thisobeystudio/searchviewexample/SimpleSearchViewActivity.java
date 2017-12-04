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

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleSearchViewActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchDataAsyncResponse {

    // SearchView
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.search_view)
    SearchView mSearchView;
    // Data container RecyclerView
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    // RecyclerView Adapter
    private SearchRecyclerViewAdapter mAdapter;

    // Demo data
    private ArrayList<String> mData;
    // Search results data
    private ArrayList<String> mSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_search);

        ButterKnife.bind(this);

        setTitle(getClass().getSimpleName());

        setData();

        setupSearchView();
        setupSearchRecyclerView();

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
        String[] monthsArray = getResources().getStringArray(R.array.countries);
        setData(new ArrayList<>(Arrays.asList(monthsArray)));
    }

    private void setupSearchView() {

        mSearchView.setActivated(true);
        mSearchView.setQueryHint(getString(R.string.query_hint));
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);

        //mSearchView.clearFocus();

        // set query listener
        mSearchView.setOnQueryTextListener(this);
    }

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
        mAdapter = new SearchRecyclerViewAdapter(this, getData());

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
            mAdapter.swapData(getData());
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
            mAdapter.swapData(getData());
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

    // filter results
    private void setSearchResultsByQuery(String query) {

        if (getData() != null && getData().size() > 0) {

            setSearchResults(new ArrayList<String>());

            for (int i = 0; i < getData().size(); i++) {
                // notice that is checking contains and toLowerCased
                if (getData().get(i).toLowerCase().contains(query.toLowerCase())) {
                    String month = getData().get(i);
                    getSearchResults().add(month);
                }
            }

        }
    }

}
