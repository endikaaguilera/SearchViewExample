package com.thisobeystudio.searchviewexample.async;

import android.os.AsyncTask;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class SearchDataAsync extends AsyncTask<String, Void, Void> {

    public SearchDataAsyncResponse delegate = null;

    @Override
    protected Void doInBackground(String... params) {
        delegate.searchDataAsyncDoInBackground(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        delegate.searchDataAsyncOnPostExecute();
    }
}