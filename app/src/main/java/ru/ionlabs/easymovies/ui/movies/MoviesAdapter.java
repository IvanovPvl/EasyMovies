package ru.ionlabs.easymovies.ui.movies;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ionlabs.easymovies.R;
import ru.ionlabs.easymovies.data.model.Movie;
import ru.ionlabs.easymovies.data.remote.MoviesService;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Fragment mParentFragment;
    private List<Movie> mMovies = new ArrayList<>();
    private PublishSubject<Integer> mClicksSubject = PublishSubject.create();

    @Inject
    MoviesAdapter() {}

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mParentFragment == null) {
            throw new UnsupportedOperationException("Parent fragment can not be null.");
        }

        Movie movie = mMovies.get(position);
        holder.bind(movie);

        holder.itemView.setOnClickListener(v -> mClicksSubject.onNext(movie.id()));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }

    public void addMovies(List<Movie> movies) {
            mMovies.addAll(movies);
    }

    public void removeMovies() {
        mMovies.clear();
    }

    public Observable<Integer> getPosterClicks() {
        return mClicksSubject.asObservable();
    }

    public void setParentFragment(Fragment fragment) {
        mParentFragment = fragment;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_imageview) ImageView mPosterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Movie movie) {
            Glide.with(mParentFragment)
                    .load(MoviesService.Api.POSTER_GRID_ENDPOINT + movie.posterPath())
                    .into(mPosterImageView);
        }
    }
}
