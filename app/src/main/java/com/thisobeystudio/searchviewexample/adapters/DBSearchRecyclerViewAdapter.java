package com.thisobeystudio.searchviewexample.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thisobeystudio.searchviewexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thisobeystudio.searchviewexample.db.DemoContract.PROJECTION_INDEX;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class DBSearchRecyclerViewAdapter extends
        RecyclerView.Adapter<DBSearchRecyclerViewAdapter.SearchViewHolder> {

    //private final String TAG = getClass().getSimpleName();

    private final Context mContext;
    private Cursor mData;

    class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_view_item_text_view)
        TextView recyclerViewItem;

        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DBSearchRecyclerViewAdapter(Context context, Cursor data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        if (this.mContext == null
                || mData == null
                || mData.getCount() <= 0
                || !mData.moveToFirst()) {
            return 0;
        } else return mData.getCount();
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
                || mData == null) {
            return;
        }

        mData.moveToPosition(i);

        final String title = mData.getString(PROJECTION_INDEX);
        viewHolder.recyclerViewItem.setText(title);

    }

    public void swapData(Cursor newResults) {
        mData = newResults;
        notifyDataSetChanged();
    }
}
