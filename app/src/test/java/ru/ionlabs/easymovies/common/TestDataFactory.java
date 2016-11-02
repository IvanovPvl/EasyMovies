package ru.ionlabs.easymovies.common;

import java.util.ArrayList;
import java.util.List;

import ru.ionlabs.easymovies.data.model.Movie;

public class TestDataFactory {

    public static List<Movie> makeListMovies(int number) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Movie movie = makeMovie(i, false);
            movies.add(movie);
        }

        return movies;
    }

    public static Movie makeMovie(int id, boolean isFavorite) {
        int favorite = isFavorite ? 1 : 0;
        return Movie.create(
                id + 1,
                "Title_" + id,
                "http://image.tmdb.org/t/p/w342/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
                "http://backdrop_path_" + id,
                "overview_" + id,
                "16 June, 1994",
                favorite
        );
    }
}
