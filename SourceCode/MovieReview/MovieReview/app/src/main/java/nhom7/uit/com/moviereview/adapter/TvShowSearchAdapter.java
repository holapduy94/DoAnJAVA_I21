package nhom7.uit.com.moviereview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.controller.DetailTVShowActivity;
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.TvShow;

/**
 * Created by phuocthang on 10/18/2017.
 */

public class TvShowSearchAdapter extends RecyclerView.Adapter<TvShowSearchHolder> {

    Context context;
    private List<TvShow> tvShowsList;
    public TvShowSearchAdapter(List<TvShow> tvShowsList,Context context) {
        this.context=context;
        this.tvShowsList = tvShowsList;
    }


    @Override
    public TvShowSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tvshow_search, parent, false);

        return new TvShowSearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TvShowSearchHolder holder, final int position) {
        holder.tv_tvShow_name.setText(tvShowsList.get(position).getNameShow());
        holder.tv_tvShow_year.setText(tvShowsList.get(position).getYearOnAir());
        final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.tv_tvShow_name.setTypeface(type);
        holder.tv_tvShow_year.setTypeface(type1);

        holder.progressbar_search.setVisibility(View.VISIBLE);
        Picasso.with(context).load(tvShowsList.get(position).getPosterPath()).error(R.drawable.ic_broken_image).fit().into(holder.iv_tvShow, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar_search.setVisibility(View.GONE);
            }
            @Override
            public void onError() {
                holder.progressbar_search.setVisibility(View.GONE);
            }
        });


        holder.cv_tvshows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvShow tvShow = tvShowsList.get(position);
                Intent myIntent = new Intent(context, DetailTVShowActivity.class);
                Bundle bundle = new Bundle();
                String tvshowID = tvShow.getTvID();
                String tvshowPathBanner = tvShow.getBackgroundPath();
                bundle.putString("id", tvshowID);
                bundle.putString("backdrop_path", tvshowPathBanner);
                myIntent.putExtra("tvshow_bundle", bundle);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  tvShowsList.size();
    }
}
