package ru.ionlabs.easymovies.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ionlabs.easymovies.R;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.data.remote.MoviesService;
import ru.ionlabs.easymovies.ui.base.BaseActivity;
import ru.ionlabs.easymovies.util.DateUtil;
import timber.log.Timber;

public class DetailActivity extends BaseActivity implements DetailActivityMvpView {

    private static final String EXTRA_MOVIE_ID = "ru.easymovies.movie_id";

    @Inject DetailActivityPresenter mPresenter;

    @BindView(R.id.poster) ImageView mPosterImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.favorite_button) FloatingActionButton mFavoriteButton;

    public static Intent newIntent(Context packageContext, int movieId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mPresenter.attachView(this);
        setupToolbar();

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        mPresenter.loadMovie(movieId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMovie(Movie movie) {
        Glide.with(this)
                .load(MoviesService.Api.BACKDROP_DETAIL_ENDPOINT + movie.backdropPath())
                .crossFade()
                .into(mPosterImageView);

        mToolbar.setTitle(movie.title());
        mOverview.setText(movie.overview());
        mReleaseDate.setText(DateUtil.formatDateString(movie.releaseDate()));

        showFavorite(movie.isFavorite());
        mFavoriteButton.setOnClickListener(v -> mPresenter.updateFavorite(movie.id(), !movie.isFavorite()));
    }

    @Override
    public void showFavorite(boolean isFavorite) {
        int resourceId = R.drawable.ic_star_outline_white_24dp;
        if (isFavorite) {
            resourceId = R.drawable.ic_star_outline_accent_100_24dp;
        }
        mFavoriteButton.setImageResource(resourceId);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            boolean isDialog = getResources().getBoolean(R.bool.is_dialog);
            if (isDialog) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            }
        }
    }
}
