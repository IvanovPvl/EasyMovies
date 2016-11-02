package ru.ionlabs.easymovies.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import ru.ionlabs.easymovies.BuildConfig;
import ru.ionlabs.easymovies.data.local.DatabaseHelper;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.data.remote.MoviesService;
import ru.ionlabs.easymovies.common.TestDataFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock MoviesService.Api mMockApiService;
    private DataManager mDataManager;

    @Before
    public void setup() {
        mDataManager = new DataManager(mMockDatabaseHelper, mMockApiService);
    }

    @Test
    public void needRefreshMovies() {
        int count = 20;
        int page = 1;
        List<Movie> movies = TestDataFactory.makeListMovies(count);

        when(mMockApiService.getTopRatedMovies(BuildConfig.API_KEY, page))
                .thenReturn(Observable.just(movies));

        when(mMockDatabaseHelper.setMovies(movies, true))
                .thenReturn(Observable.just(movies));

        TestSubscriber<List<Movie>> result = new TestSubscriber<>();
        mDataManager.getMovies(page, true).subscribe(result);
        assertSubscriber(result, movies);

        verify(mMockDatabaseHelper, never()).getMovies(DataManager.PER_PAGE * (page - 1), DataManager.PER_PAGE);
        verify(mMockApiService).getTopRatedMovies(BuildConfig.API_KEY, page);
        verify(mMockDatabaseHelper).setMovies(movies, true);
        verify(mMockDatabaseHelper, never()).setMovies(movies, false);
    }

    @Test
    public void getMovies() {
        int count = 30;
        int page = 2;
        List<Movie> movies = TestDataFactory.makeListMovies(count);
        List<Movie> page2 = movies.subList(DataManager.PER_PAGE * (page - 1), count);

        when(mMockDatabaseHelper.getMovies(DataManager.PER_PAGE * (page - 1), DataManager.PER_PAGE))
                .thenReturn(Observable.just(page2));

        TestSubscriber<List<Movie>> result = new TestSubscriber<>();
        mDataManager.getMovies(page, false).subscribe(result);
        assertSubscriber(result, page2);

        verify(mMockApiService, never()).getTopRatedMovies(BuildConfig.API_KEY, page);
        verify(mMockDatabaseHelper, never()).setMovies(page2, true);
        verify(mMockDatabaseHelper, never()).setMovies(page2, false);
        verify(mMockDatabaseHelper).getMovies(20, 20);
    }

    @Test
    public void getMoviesIfDbEmpty() {
        int count = 20;
        int page = 1;
        List<Movie> movies = TestDataFactory.makeListMovies(count);

        when(mMockDatabaseHelper.getMovies(DataManager.PER_PAGE * (page - 1), DataManager.PER_PAGE))
                .thenReturn(Observable.just(new ArrayList<>()));

        when(mMockApiService.getTopRatedMovies(BuildConfig.API_KEY, page))
                .thenReturn(Observable.just(movies));

        when(mMockDatabaseHelper.setMovies(movies, true))
                .thenReturn(Observable.just(movies));

        TestSubscriber<List<Movie>> result = new TestSubscriber<>();
        mDataManager.getMovies(page, false).subscribe(result);
        assertSubscriber(result, movies);

        verify(mMockApiService).getTopRatedMovies(BuildConfig.API_KEY, page);
        verify(mMockDatabaseHelper).setMovies(movies, true);
        verify(mMockDatabaseHelper, never()).setMovies(movies, false);
    }

    @Test
    public void getMoviesIfItsNotExistsInDb() {
        int count = 30;
        int page = 2;
        List<Movie> movies = TestDataFactory.makeListMovies(count);
        List<Movie> page2 = movies.subList(DataManager.PER_PAGE * (page - 1), count);

        when(mMockDatabaseHelper.getMovies(DataManager.PER_PAGE * (page - 1), DataManager.PER_PAGE))
                .thenReturn(Observable.just(new ArrayList<>()));

        when(mMockApiService.getTopRatedMovies(BuildConfig.API_KEY, page))
                .thenReturn(Observable.just(page2));

        when(mMockDatabaseHelper.setMovies(page2, false))
                .thenReturn(Observable.just(page2));

        TestSubscriber<List<Movie>> result = new TestSubscriber<>();
        mDataManager.getMovies(page, false).subscribe(result);
        assertSubscriber(result, page2);

        verify(mMockApiService).getTopRatedMovies(BuildConfig.API_KEY, page);
        verify(mMockDatabaseHelper, never()).setMovies(page2, true);
        verify(mMockDatabaseHelper).setMovies(page2, false);
    }

    private void assertSubscriber(TestSubscriber<List<Movie>> subscriber, List<Movie> movies) {
        subscriber.assertNoErrors();
        subscriber.assertValue(movies);
    }
}
