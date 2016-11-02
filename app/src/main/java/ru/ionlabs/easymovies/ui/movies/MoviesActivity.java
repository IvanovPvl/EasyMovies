package ru.ionlabs.easymovies.ui.movies;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ionlabs.easymovies.R;
import ru.ionlabs.easymovies.ui.movies.favorites.FavoritesFragment;
import ru.ionlabs.easymovies.ui.movies.toprated.TopRatedFragment;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TopRatedFragment.newInstance(), getString(R.string.top_rated));
        adapter.addFragment(FavoritesFragment.newInstance(), getString(R.string.favorites));
        viewPager.setAdapter(adapter);
    }
}