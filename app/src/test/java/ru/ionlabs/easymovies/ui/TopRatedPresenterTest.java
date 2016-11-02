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
import ru.ionlabs.easymovies.ui.movies.toprated.TopRatedMvpView;
import ru.ionlabs.easymovies.ui.movies.toprated.TopRatedPresenter;
import ru.ionlabs.easymovies.common.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TopRatedPresenterTest {

    @Mock TopRatedMvpView mMockTopRatedMvpView;
    @Mock DataManager mMockDataManager;
    private TopRatedPresenter mTopRatedPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setup() {
        mTopRatedPresenter = new TopRatedPresenter(mMockDataManager);
        mTopRatedPresenter.attachView(mMockTopRatedMvpView);
    }

    @After
    public void tearDown() {
        mTopRatedPresenter.detachView();
    }

    @Test
    public void getMoviesShowMovies() {
        int page = 1;
        List<Movie> movies = TestDataFactory.makeListMovies(20);
        when(mMockDataManager.getMovies(page, false)).thenReturn(Observable.just(movies));

        mTopRatedPresenter.getMovies(page, false);
        verify(mMockTopRatedMvpView).showMovies(movies);
    }

    @Test
    public void refreshUpdateMovies() {
        int page = 1;
        List<Movie> movies = TestDataFactory.makeListMovies(20);
        when(mMockDataManager.getMovies(page, true)).thenReturn(Observable.just(movies));

        mTopRatedPresenter.refresh();
        verify(mMockTopRatedMvpView).hideLoader();
        verify(mMockTopRatedMvpView).updateMovies(movies);
    }
}
