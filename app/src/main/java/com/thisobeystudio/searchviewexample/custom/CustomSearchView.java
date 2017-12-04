package com.thisobeystudio.searchviewexample.custom;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
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

    //private final String TAG = FloatingMenu.class.getSimpleName();

    public final static int selectionDays = 1;
    public final static int selectionMonths = 2;
    public final static int selectionCountries = 3;

    private int mSelection = CustomSearchView.selectionDays;

    // Search Item onClick callbacks
    public interface SearchItemCallbacks {
        void onSearchItemCallbacks(int selection);
    }

    // Search Item onClick callbacks
    private SearchItemCallbacks mSearchItemsItemCallbacks;

    // Search View query listener
    private SearchView.OnQueryTextListener mOnQueryTextListener;

    /**
     * @param context context
     * @param parent  parent ConstraintLayout
     */
    public void showCustomSearchView(final Context context,
                                     final SearchView.OnQueryTextListener onQueryTextListener,
                                     final ConstraintLayout parent,
                                     final String query) {

        if (context == null || onQueryTextListener == null || parent == null) {
            setVisible(false);
            return;
        }

        if (!isVisible()) {

            // this makes show Custom SearchView looks better, not instant
            TransitionManager.beginDelayedTransition(parent);

            setVisible(true);

            setOnQueryTextListener(onQueryTextListener);

            setParentConstraintLayout(parent);

            setupFloatingSearchViewFrameLayout(context, query);
            setCancelableOnTouchOutside();

            final int accent = ContextCompat.getColor(context, R.color.colorAccent);

            switch (getSelection()) {
                case CustomSearchView.selectionDays:
                    setTextViewColors(getDaysTextView(), accent);
                    break;
                case CustomSearchView.selectionMonths:
                    setTextViewColors(getMonthsTextView(), accent);
                    break;
                case CustomSearchView.selectionCountries:
                    setTextViewColors(getCountriesTextView(), accent);
                    break;
            }
        }

    }

    /**
     * @param context context
     */
    private void setupFloatingSearchViewFrameLayout(final Context context, final String query) {

        if (context == null) return;

        LayoutInflater inflater = LayoutInflater.from(context);

        FrameLayout frameLayout = (FrameLayout) inflater
                .inflate(R.layout.custom_search_view, getParentConstraintLayout(), false);

        setFrameLayout(frameLayout);

        getParentConstraintLayout().addView(getFrameLayout());

        setSearchView((SearchView) frameLayout.findViewById(R.id.custom_search_view));
        setupSearchView(context);

        setDaysTextView((TextView) frameLayout.findViewById(R.id.days_text_view));
        setMonthsTextView((TextView) frameLayout.findViewById(R.id.months_text_view));
        setCountriesTextView((TextView) frameLayout.findViewById(R.id.countries_text_view));

        final int accent = ContextCompat.getColor(context, R.color.colorAccent);
        final int gray = ContextCompat.getColor(context, android.R.color.darker_gray);

        getDaysTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSearchItemsItemCallbacks() != null) {
                    getSearchItemsItemCallbacks().onSearchItemCallbacks(selectionDays);
                    setTextViewColors(getDaysTextView(), accent);
                    setTextViewColors(getMonthsTextView(), gray);
                    setTextViewColors(getCountriesTextView(), gray);
                }
            }
        });

        getMonthsTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSearchItemsItemCallbacks() != null) {
                    getSearchItemsItemCallbacks().onSearchItemCallbacks(selectionMonths);
                    setTextViewColors(getDaysTextView(), gray);
                    setTextViewColors(getMonthsTextView(), accent);
                    setTextViewColors(getCountriesTextView(), gray);
                }
            }
        });

        getCountriesTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSearchItemsItemCallbacks() != null) {
                    getSearchItemsItemCallbacks().onSearchItemCallbacks(selectionCountries);
                    setTextViewColors(getDaysTextView(), gray);
                    setTextViewColors(getMonthsTextView(), gray);
                    setTextViewColors(getCountriesTextView(), accent);
                }
            }
        });

        if (getSearchView() != null)
            getSearchView().setQuery(query, false);

    }

    private void setTextViewColors(TextView textView, int color) {
        textView.setTextColor(color);
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /**
     * Search View
     */
    private SearchView mSearchView;

    /**
     * @return Search View
     */
    @SuppressWarnings("WeakerAccess")
    public SearchView getSearchView() {
        return mSearchView;
    }

    /**
     * @param searchView Search View
     */
    private void setSearchView(SearchView searchView) {
        this.mSearchView = searchView;
    }

    /**
     * Setup SearchView.
     * <br><br>Actions:
     * <br><br><code>setActivated(true)</code>
     * <br><code>setQueryHint("Type your keyword here")</code>
     * <br><code>onActionViewExpanded()</code>
     * <br><code>setIconified(false)</code>
     * <br><code>setOnQueryTextListener(getOnQueryTextListener())</code>
     */
    private void setupSearchView(Context context) {
        if (context == null) return;

        getSearchView().setActivated(true);
        getSearchView().setQueryHint(context.getString(R.string.query_hint));
        getSearchView().onActionViewExpanded();
        getSearchView().setIconified(false);

        // set query listener
        getSearchView().setOnQueryTextListener(getOnQueryTextListener());
    }

    /**
     * Days TextView
     */
    private TextView mDaysTextView;

    /**
     * @param daysTextView Days TextView
     */
    private void setDaysTextView(TextView daysTextView) {
        this.mDaysTextView = daysTextView;
    }

    /**
     * @return Days TextView
     */
    private TextView getDaysTextView() {
        return mDaysTextView;
    }

    /**
     * Months TextView
     */
    private TextView mMonthsTextView;

    /**
     * @param monthsTextView Months TextView
     */
    private void setMonthsTextView(TextView monthsTextView) {
        this.mMonthsTextView = monthsTextView;
    }

    /**
     * @return Months TextView
     */
    private TextView getMonthsTextView() {
        return mMonthsTextView;
    }

    /**
     * Countries TextView
     */
    private TextView mCountriesTextView;

    /**
     * @param countriesTextView Countries TextView
     */
    private void setCountriesTextView(TextView countriesTextView) {
        this.mCountriesTextView = countriesTextView;
    }

    /**
     * @return Countries TextView
     */
    private TextView getCountriesTextView() {
        return mCountriesTextView;
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
     * @param visible visibility
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

        // this makes remove Custom SearchView looks better, not instant
        TransitionManager.beginDelayedTransition(getParentConstraintLayout());

        if (getParentConstraintLayout() == null ||
                getFrameLayout() == null ||
                getSearchView() == null)
            return;

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
    private boolean isCancelableOnTouchOutside() {
        return isCancelableOnTouchOutside;
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
     * determines query filter >>> days or months
     *
     * @return current selection
     */
    public int getSelection() {
        return mSelection;
    }

    /**
     * in this case determines query filter >>> days or months
     *
     * @param selection filter selection
     */
    public void setSelection(int selection) {
        mSelection = selection;
    }

    /**
     * {@link SearchItemCallbacks}
     *
     * @param mCallbacks Search Item Callbacks
     */
    public void setSearchItemCallbacks(SearchItemCallbacks mCallbacks) {
        this.mSearchItemsItemCallbacks = mCallbacks;
    }

    /**
     * {@link SearchItemCallbacks}
     *
     * @return Search Item Callbacks
     */
    private SearchItemCallbacks getSearchItemsItemCallbacks() {
        return mSearchItemsItemCallbacks;
    }

    /**
     * @param onQueryTextListener SearchView OnQueryTextListener
     */
    private void setOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
        this.mOnQueryTextListener = onQueryTextListener;
    }

    /**
     * @return SearchView OnQueryTextListener
     */
    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return mOnQueryTextListener;
    }

}
