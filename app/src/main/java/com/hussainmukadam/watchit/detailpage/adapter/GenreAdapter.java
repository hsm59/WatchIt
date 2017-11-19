package com.hussainmukadam.watchit.detailpage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.adapter.MoviesGenreAdapter;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 19/11/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder>  {
    private static final String TAG = "GenreAdapter";
    private List<Genre> genreList;

    public GenreAdapter(List<Genre> genreList) {
        this.genreList = genreList;
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false);
        return new GenreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        Log.d(TAG, "onBindViewHolder: "+genre.toString());
        holder.tvGenre.setText(genre.getGenreName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.chip_genre)
        TextView tvGenre;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
