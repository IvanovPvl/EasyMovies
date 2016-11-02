package ru.ionlabs.easymovies.ui.movies.favorites;

import javax.inject.Inject;

import ru.ionlabs.easymovies.data.DataManager;
import ru.ionlabs.easymovies.ui.base.BasePresenter;
import ru.ionlabs.easymovies.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class FavoritesPresenter extends BasePresenter<FavoritesMvpView> {

    private final DataManager mDataManager;

    private Subscription mGetSubscription;

    @Inject
    public FavoritesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mGetSubscription);
        super.detachView();
    }

    public void getFavorites() {
        checkViewAttached();
        RxUtil.unsubscribe(mGetSubscription);
        mGetSubscription = mDataManager.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> getMvpView().showFavorites(movies), Timber::e, () -> {});
    }
}
