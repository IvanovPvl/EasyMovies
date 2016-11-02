package ru.ionlabs.easymovies.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.util.MoviesAdapterFactory;
import rx.Observable;

public interface MoviesService {

    final class Builder {

        static Retrofit.Builder prepare() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ApiResponseInterceptor())
                    .build();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MoviesAdapterFactory.create())
                    .create();

            return new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }
    }

    interface Api {
        String API_ENDPOINT = "https://api.themoviedb.org/3/";
        String POSTER_ENDPOINT = "http://image.tmdb.org/t/p/w";
        String POSTER_GRID_ENDPOINT = POSTER_ENDPOINT + "342";
        String BACKDROP_DETAIL_ENDPOINT = POSTER_ENDPOINT + "500";

        @GET("movie/top_rated")
        Observable<List<Movie>> getTopRatedMovies(
                @Query("api_key") String apiKey,
                @Query("page") int page
        );

        class Creator {

            public static Api newService() {
                return Builder.prepare()
                        .baseUrl(API_ENDPOINT)
                        .build()
                        .create(Api.class);
            }
        }
    }
}
