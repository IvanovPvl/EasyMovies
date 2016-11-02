package ru.ionlabs.easymovies.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.ionlabs.easymovies.data.remote.MoviesService;

@Module
public class ApiServiceModule {

    @Provides
    @Singleton
    MoviesService.Api provideApiServiceModule() {
        return MoviesService.Api.Creator.newService();
    }
}
