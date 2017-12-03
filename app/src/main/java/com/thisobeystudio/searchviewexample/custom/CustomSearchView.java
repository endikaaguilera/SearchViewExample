package com.thisobeystudio.searchviewexample.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.thisobeystudio.searchviewexample.R;

/**
 * Created by thisobeystudio on 3/12/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class CustomSearchView {

    public final static int selectionDays = 1;
    public final static int selectionMonths = 2;

    private int mSelection = CustomSearchView.selectionDays;

    //private final String TAG = FloatingMenu.class.getSimpleName();
    public interface SearchItemCallbacks {
        @SuppressWarnings("unused")
        void onSearchItemCallbacks(int selection);
    }

    // menu item onclick callbacks
    private SearchItemCallbacks mSearchItemsItemCallbacks;

    public void setSearchItemCallbacks(SearchItemCallbacks mCallbacks) {
        this.mSearchItemsItemCallbacks = mCallbacks;
    }

    private SearchItemCallbacks getSearchItemsItemCallbacks() {
        return mSearchItemsItemCallbacks;
    }

    private SearchView.OnQueryTextListener mOnQueryTextListener;

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return mOnQueryTextListener;
    }

    private void setOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
        this.mOnQueryTextListener = onQueryTextListener;
    }

    /**
     * @param context context
     * @param parent  parent ConstraintLayout
     */
    public void showCustomSearchView(final Context context,
                                     final SearchView.OnQueryTextListener onQueryTextListener,
                                     final ConstraintLayout parent) {

        if (context == null
                || onQueryTextListener == null
                || parent == null) {
            setVisible(false);
            return;
        }

        if (!isVisible()) {

            setVisible(true);

            setOnQueryTextListener(onQueryTextListener);

            setParentConstraintLayout(parent);

            setupFloatingMenuFrameLayout(context);
            setCancelableOnTouchOutside();

            switch (getSelection()) {
                case CustomSearchView.selectionDays:
                    getDaysTextView()
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    break;
                case CustomSearchView.selectionMonths:
                    getMonthsTextView()
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * @param context context
     */
    private void setupFloatingMenuFrameLayout(final Context context) {

        if (context == null) return;

        LayoutInflater inflater = LayoutInflater.from(context);

        FrameLayout frameLayout = (FrameLayout) inflater
                .inflate(R.layout.custom_search_view, getParentConstraintLayout(), false);

        setFrameLayout(frameLayout);

        getParentConstraintLayout().addView(getFrameLayout());

        setSearchView((SearchView) frameLayout.findViewById(R.id.custom_search_view));
        setupSearchView();

        setDaysTextView((TextView) frameLayout.findViewById(R.id.days_text_view));
        setMonthsTextView((TextView) frameLayout.findViewById(R.id.months_text_view));

        getDaysTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSearchItemsItemCallbacks() != null) {
                    getSearchItemsItemCallbacks().onSearchItemCallbacks(selectionDays);
                    getDaysTextView().setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    getMonthsTextView().setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                }
            }
        });

        getMonthsTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSearchItemsItemCallbacks() != null) {
                    getSearchItemsItemCallbacks().onSearchItemCallbacks(selectionMonths);
                    getDaysTextView().setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                    getMonthsTextView().setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                }
            }
        });

    }

    private SearchView mSearchView;

    public SearchView getSearchView() {
        return mSearchView;
    }

    private void setSearchView(SearchView searchView) {
        this.mSearchView = searchView;
    }

    private void setupSearchView() {

        getSearchView().setActivated(true);
        getSearchView().setQueryHint("Type your keyword here");
        getSearchView().onActionViewExpanded();
        getSearchView().setIconified(false);

        getSearchView().clearFocus();

        // set query listener
        getSearchView().setOnQueryTextListener(getOnQueryTextListener());
    }

    private TextView mDaysTextView;

    private void setDaysTextView(TextView daysTextView) {
        this.mDaysTextView = daysTextView;
    }

    private TextView getDaysTextView() {
        return mDaysTextView;
    }

    private TextView mMonthsTextView;

    private void setMonthsTextView(TextView monthsTextView) {
        this.mMonthsTextView = monthsTextView;
    }

    private TextView getMonthsTextView() {
        return mMonthsTextView;
    }

    /**
     * set Cancelable On Touch Outside
     */
    private void setCancelableOnTouchOutside() {

        if (isCancelableOnTouchOutside()) {
            getFrameLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeCustomSearchView();
                }
            });
        }

    }

    /**
     * CustomSearchView visibility
     */
    private boolean isVisible;

    /**
     * @return CustomSearchView visibility
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param visible menu visibility
     */
    private void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * parent ConstraintLayout
     */
    private ConstraintLayout mParentConstraintLayout;

    /**
     * @param parentConstraintLayout parent ConstraintLayout
     */
    private void setParentConstraintLayout(ConstraintLayout parentConstraintLayout) {
        this.mParentConstraintLayout = parentConstraintLayout;
    }

    /**
     * @return parent ConstraintLayout
     */
    private ConstraintLayout getParentConstraintLayout() {
        return mParentConstraintLayout;
    }

    /**
     * CustomSearchView container
     */
    private FrameLayout mFrameLayout;

    /**
     * @return CustomSearchView container
     */
    private FrameLayout getFrameLayout() {
        return mFrameLayout;
    }

    /**
     * @param frameLayout CustomSearchView container
     */
    private void setFrameLayout(FrameLayout frameLayout) {
        this.mFrameLayout = frameLayout;
    }


    /**
     * remove CustomSearchView and update visibility
     */
    public void removeCustomSearchView() {
        if (getParentConstraintLayout() == null || getFrameLayout() == null) return;

        if (getSearchView().hasFocus())
            getSearchView().clearFocus();

        getParentConstraintLayout().removeView(getFrameLayout());
        setVisible(false);

    }

    /**
     * handle on Back Pressed removes CustomSearchView if true
     * default is true
     */
    private boolean isCancelable = true;

    /**
     * @return cancelable on Back Pressed
     */
    public boolean isCancelable() {
        return isCancelable;
    }

    /**
     * @param cancelable cancelable on Back Pressed
     */
    @SuppressWarnings("unused")
    public void setCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
    }

    /**
     * handle On Touch Outside removes menu if true
     * default is true
     */
    private boolean isCancelableOnTouchOutside = true;

    /**
     * if true CustomSearchView will be removed on any out of CustomSearchView touch, must be set before showCustomSearchView()
     *
     * @param cancelable cancelable On Touch Outside
     */
    @SuppressWarnings("unused")
    public void setCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelableOnTouchOutside = cancelable;
    }

    /**
     * @return cancelable On Touch Outside
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isCancelableOnTouchOutside() {
        return isCancelableOnTouchOutside;
    }

    public int getSelection() {
        return mSelection;
    }

    public void setSelection(int selection) {
        mSelection = selection;
    }

}
