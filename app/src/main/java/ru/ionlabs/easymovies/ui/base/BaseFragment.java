package ru.ionlabs.easymovies.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.ionlabs.easymovies.EasyMoviesApplication;
import ru.ionlabs.easymovies.di.component.ApplicationComponent;
import ru.ionlabs.easymovies.di.component.FragmentComponent;
import ru.ionlabs.easymovies.di.module.FragmentModule;

public class BaseFragment extends Fragment {

    private FragmentComponent mFragmentComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentComponent = getAppComponent().fragmentComponent(new FragmentModule());
    }

    protected FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }

    private EasyMoviesApplication getApplication() {
        return (EasyMoviesApplication) getActivity().getApplication();
    }

    private ApplicationComponent getAppComponent() {
        return getApplication().getComponent();
    }
}
