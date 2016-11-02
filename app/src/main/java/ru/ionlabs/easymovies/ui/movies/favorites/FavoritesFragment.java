package ru.ionlabs.easymovies.ui.movies.favorites;

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
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.ui.base.BaseFragment;
import ru.ionlabs.easymovies.ui.movies.MoviesAdapter;
import ru.ionlabs.easymovies.ui.detail.DetailActivity;
import ru.ionlabs.easymovies.util.RxUtil;
import rx.Subscription;

public class FavoritesFragment extends BaseFragment implements FavoritesMvpView {

    @Inject FavoritesPresenter mPresenter;
    @Inject MoviesAdapter mAdapter;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private Subscription mClicksSubscription;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
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
        mSwipeLayout.setEnabled(false);
        setupRecyclerView();
        mPresenter.getFavorites();
    }

    @Override
    public void onDestroyView() {
        RxUtil.unsubscribe(mClicksSubscription);
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showFavorites(List<Movie> movies) {
        mAdapter.setMovies(movies);
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.span_count));

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setParentFragment(this);
        mRecyclerView.setAdapter(mAdapter);

        mClicksSubscription = mAdapter.getPosterClicks().subscribe(movieId -> {
            Intent intent = DetailActivity.newIntent(getActivity(), movieId);
            startActivity(intent);
        });
    }
}
