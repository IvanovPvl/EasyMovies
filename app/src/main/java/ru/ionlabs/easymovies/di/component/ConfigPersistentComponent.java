package ru.ionlabs.easymovies.di.component;

import dagger.Subcomponent;
import ru.ionlabs.easymovies.di.ConfigPersistent;
import ru.ionlabs.easymovies.di.module.ActivityModule;

@ConfigPersistent
@Subcomponent
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);
}
