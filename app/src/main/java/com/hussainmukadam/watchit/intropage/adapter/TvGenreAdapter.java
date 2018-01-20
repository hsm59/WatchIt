package com.hussainmukadam.watchit.intropage.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/21/17.
 */

public class TvGenreAdapter extends RecyclerView.Adapter<TvGenreAdapter.ViewHolder> {
    private static final String TAG = "MoviesGenreAdapter";
    private List<Genre> genreList;
    final private TvListItemClickListener onClickListener;

    public interface TvListItemClickListener{
        void onTvListItemSelected(Genre genre);
        void onTvListItemUnSelected(Genre genre);
    }

    public TvGenreAdapter(List<Genre> genreList, TvListItemClickListener onClickListener) {
        this.genreList = genreList;
        this.onClickListener = onClickListener;
    }

    @Override
    public TvGenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false);
        return new TvGenreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TvGenreAdapter.ViewHolder holder, int position) {
        final Genre genre = genreList.get(position);
        Util.debugLog(TAG, "onBindViewHolder: "+genre.toString());
        holder.tvGenre.setText(genre.getGenreName());

        holder.tvGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvGenre.isSelected()){
                    holder.tvGenre.setSelected(false);
                    holder.tvGenre.setTextColor(ContextCompat.getColor(holder.tvGenre.getContext(), R.color.colorBlack));
                    onClickListener.onTvListItemUnSelected(genre);
                } else {
                    holder.tvGenre.setSelected(true);
                    holder.tvGenre.setTextColor(ContextCompat.getColor(holder.tvGenre.getContext(), R.color.colorPrimary));
                    onClickListener.onTvListItemSelected(genre);
                }
            }
        });
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

        public void setOnClickListener(View.OnClickListener onClickListener){
            tvGenre.setOnClickListener(onClickListener);
        }
    }
}

