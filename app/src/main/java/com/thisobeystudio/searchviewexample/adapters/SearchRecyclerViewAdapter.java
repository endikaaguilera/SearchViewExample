package com.thisobeystudio.searchviewexample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thisobeystudio.searchviewexample.R;

import java.util.ArrayList;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class SearchRecyclerViewAdapter extends
        RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {

    //private final String TAG = getClass().getSimpleName();

    private final Context mContext;
    private ArrayList<String> mSearchResults;

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private final TextView recyclerViewItem;

        SearchViewHolder(View itemView) {
            super(itemView);
            this.recyclerViewItem = itemView.findViewById(R.id.recycler_view_item_text_view);
        }
    }

    public SearchRecyclerViewAdapter(Context context, ArrayList<String> results) {
        this.mContext = context;
        this.mSearchResults = results;
    }

    @Override
    public int getItemCount() {
        if (this.mContext == null
                || this.mSearchResults == null
                || mSearchResults.size() <= 0) {
            return 0;
        } else return mSearchResults.size();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new SearchViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder viewHolder, int i) {

        if (this.mContext == null
                || this.mSearchResults == null
                || i >= mSearchResults.size()
                || mSearchResults.size() <= 0) {
            return;
        }

        final String title = mSearchResults.get(i);
        viewHolder.recyclerViewItem.setText(title);

    }

    public void swapData(ArrayList<String> newResults) {
        mSearchResults = newResults;
        notifyDataSetChanged();
    }
}
