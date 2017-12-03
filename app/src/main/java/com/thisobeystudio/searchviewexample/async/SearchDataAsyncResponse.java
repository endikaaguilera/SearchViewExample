package com.thisobeystudio.searchviewexample.async;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public interface SearchDataAsyncResponse {
    void searchDataAsyncDoInBackground(String params);

    void searchDataAsyncOnPostExecute();
}
