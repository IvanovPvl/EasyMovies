package ru.ionlabs.easymovies.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.common.TestDataFactory;
import ru.ionlabs.easymovies.ui.movies.favorites.FavoritesMvpView;
import ru.ionlabs.easymovies.ui.movies.favorites.FavoritesPresenter;
import ru.ionlabs.easymovies.common.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FavoritesPresenterTest {

    @Mock FavoritesMvpView mMockFavoritesMvpView;
    @Mock DataManager mMockDataManager;
    private FavoritesPresenter mFavoritesPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setup() {
        mFavoritesPresenter = new FavoritesPresenter(mMockDataManager);
        mFavoritesPresenter.attachView(mMockFavoritesMvpView);
    }

    @After
    public void tearDown() {
        mFavoritesPresenter.detachView();
    }

    @Test
    public void getFavoritesShowFavorites() {
        List<Movie> movies = TestDataFactory.makeListMovies(20);
        when(mMockDataManager.getFavorites()).thenReturn(Observable.just(movies));

        mFavoritesPresenter.getFavorites();
        verify(mMockFavoritesMvpView).showFavorites(movies);
    }
}
