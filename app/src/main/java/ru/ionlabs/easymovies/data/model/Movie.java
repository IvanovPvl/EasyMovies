package ru.ionlabs.easymovies.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Movie {

    public abstract int id();

    public abstract String title();

    @SerializedName("poster_path")
    public abstract String posterPath();

    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    public abstract String overview();

    @SerializedName("release_date")
    public abstract String releaseDate(); // yyyy-MM-dd

    public abstract int favorite();

    public static Movie create(int id, String title, String posterPath, String backdropPath,
                               String overview, String releaseDate, int favorite) {
        return new AutoValue_Movie(id, title, posterPath, backdropPath, overview, releaseDate, favorite);
    }

    public boolean isFavorite() {
        return favorite() == 1;
    }

    public static TypeAdapter<Movie> typeAdapter(Gson gson) {
        return new AutoValue_Movie.GsonTypeAdapter(gson);
    }
}
