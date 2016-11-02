package ru.ionlabs.easymovies.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private GridLayoutManager mLayoutManager;
    private int visibleThreshold = 1;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    protected EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int newState) {
        int lastVisibleItemPosition;
        int startingPageIndex = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}
