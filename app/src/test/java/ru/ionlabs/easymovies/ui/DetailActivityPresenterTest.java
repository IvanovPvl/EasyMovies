package ru.ionlabs.easymovies.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.common.TestDataFactory;
import ru.ionlabs.easymovies.ui.detail.DetailActivityMvpView;
import ru.ionlabs.easymovies.ui.detail.DetailActivityPresenter;
import ru.ionlabs.easymovies.common.RxSchedulersOverrideRule;
import rx.Observable;
import rx.Single;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DetailActivityPresenterTest {

    @Mock DetailActivityMvpView mMockDetailActivityMvpView;
    @Mock DataManager mMockDataManager;
    private DetailActivityPresenter mDetailActivityPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setup() {
        mDetailActivityPresenter = new DetailActivityPresenter(mMockDataManager);
        mDetailActivityPresenter.attachView(mMockDetailActivityMvpView);
    }

    @After
    public void tearDown() {
        mDetailActivityPresenter.detachView();
    }

    @Test
    public void loadMovieShowMovie() {
        int id = 1;
        Movie movie = TestDataFactory.makeMovie(id, false);

        when(mMockDataManager.getMovieById(id)).thenReturn(Observable.just(movie));
        mDetailActivityPresenter.loadMovie(id);

        verify(mMockDetailActivityMvpView).showMovie(movie);
    }

    @Test
    public void updateFavoriteShowFavorite() {
        int id = 1;
        boolean isFavorite = true;
        when(mMockDataManager.updateFavorite(id, isFavorite)).thenReturn(Single.just(isFavorite));

        mDetailActivityPresenter.updateFavorite(id, isFavorite);
        verify(mMockDetailActivityMvpView).showFavorite(isFavorite);
    }
}
