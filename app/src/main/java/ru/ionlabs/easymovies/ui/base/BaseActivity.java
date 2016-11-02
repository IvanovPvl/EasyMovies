package ru.ionlabs.easymovies.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import java.util.concurrent.atomic.AtomicInteger;

import ru.ionlabs.easymovies.EasyMoviesApplication;
import ru.ionlabs.easymovies.di.component.ActivityComponent;
import ru.ionlabs.easymovies.di.component.ConfigPersistentComponent;
import ru.ionlabs.easymovies.di.module.ActivityModule;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    private static final SparseArray<ConfigPersistentComponent> sComponents = new SparseArray<>();

    private ActivityComponent mActivityComponent;
    private int mActivityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getInt(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (sComponents.indexOfKey(mActivityId) < 0) {
            Timber.d("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = EasyMoviesApplication.get(this)
                    .getComponent().configPersistentComponent();
            sComponents.put(mActivityId, configPersistentComponent);
        } else {
            Timber.d("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponents.get(mActivityId);
        }

        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            sComponents.remove(mActivityId);
        }
        super.onDestroy();
    }
}
