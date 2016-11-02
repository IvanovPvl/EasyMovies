package ru.ionlabs.easymovies.ui.detail;

import javax.inject.Inject;

import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.di.ConfigPersistent;
import ru.ionlabs.easymovies.ui.base.BasePresenter;
import ru.ionlabs.easymovies.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ConfigPersistent
public class DetailActivityPresenter extends BasePresenter<DetailActivityMvpView> {

    private final DataManager mDataManager;

    private Subscription mGetSubscription;
    private Subscription mUpdateSubscription;

    @Inject
    public DetailActivityPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mGetSubscription);
        RxUtil.unsubscribe(mUpdateSubscription);
        super.detachView();
    }

    public void loadMovie(int movieId) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetSubscription);
        mGetSubscription = mDataManager.getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> getMvpView().showMovie(movie), Timber::e);
    }

    public void updateFavorite(int movieId, boolean isFavorite) {
        checkViewAttached();
        RxUtil.unsubscribe(mUpdateSubscription);
        mUpdateSubscription = mDataManager.updateFavorite(movieId, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getMvpView().showFavorite(result), Timber::e);
    }
}
