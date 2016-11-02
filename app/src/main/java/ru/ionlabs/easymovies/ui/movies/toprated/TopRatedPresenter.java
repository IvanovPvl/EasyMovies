package ru.ionlabs.easymovies.ui.movies.toprated;

import javax.inject.Inject;

import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.ui.base.BasePresenter;
import ru.ionlabs.easymovies.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TopRatedPresenter extends BasePresenter<TopRatedMvpView> {

    private final DataManager mDataManager;

    private Subscription mGetSubscription;

    @Inject
    public TopRatedPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        unsubscribe();
        super.detachView();
    }

    public void getMovies(int page, boolean needRefresh) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetSubscription);

        mGetSubscription = mDataManager.getMovies(page, needRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> getMvpView().showMovies(movies), Timber::e, () -> {});
    }

    public void refresh() {
        checkViewAttached();
        RxUtil.unsubscribe(mGetSubscription);
        mGetSubscription = mDataManager.getMovies(1, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    getMvpView().hideLoader();
                    getMvpView().updateMovies(movies);
                }, Timber::e, () -> {});
    }

    void unsubscribe() {
        RxUtil.unsubscribe(mGetSubscription);
    }
}
