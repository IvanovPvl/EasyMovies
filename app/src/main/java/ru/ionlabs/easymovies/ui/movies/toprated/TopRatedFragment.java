package ru.ionlabs.easymovies.ui.movies.toprated;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ionlabs.easymovies.R;
import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.ui.base.BaseFragment;
import ru.ionlabs.easymovies.ui.movies.MoviesAdapter;
import ru.ionlabs.easymovies.ui.detail.DetailActivity;
import ru.ionlabs.easymovies.util.EndlessRecyclerViewScrollListener;
import ru.ionlabs.easymovies.util.RxUtil;
import rx.Subscription;
import timber.log.Timber;

public class TopRatedFragment extends BaseFragment implements TopRatedMvpView {

    @Inject TopRatedPresenter mPresenter;
    @Inject MoviesAdapter mAdapter;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private static int sPage = 1;

    private boolean mRefreshFromEndScroll = false;
    private Subscription mClicksSubscription;

    public static TopRatedFragment newInstance() {
        return new TopRatedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fragmentComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        setupRecyclerView();
        mPresenter.getMovies(sPage, false);

        mSwipeLayout.setOnRefreshListener(() -> {
            if (!mRefreshFromEndScroll) {
                sPage = 1;
                mPresenter.refresh();
            }
        });
    }

    @Override
    public void onDestroyView() {
        RxUtil.unsubscribe(mClicksSubscription);
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mRefreshFromEndScroll = false;
        mPresenter.unsubscribe();
        mAdapter.addMovies(movies);
        mAdapter.notifyItemRangeInserted((sPage - 1) * DataManager.PER_PAGE, movies.size());
    }

    @Override
    public void updateMovies(List<Movie> movies) {
        mAdapter.removeMovies();
        mAdapter.addMovies(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideLoader() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.span_count));

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setParentFragment(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mRefreshFromEndScroll = true;
                mPresenter.getMovies(++sPage, false);
            }
        });

        mClicksSubscription = mAdapter.getPosterClicks().subscribe(movieId -> {
            Intent intent = DetailActivity.newIntent(getActivity(), movieId);
            startActivity(intent);
        });
    }
}
