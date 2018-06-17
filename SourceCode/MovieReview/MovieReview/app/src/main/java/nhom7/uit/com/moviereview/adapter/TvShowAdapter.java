package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.controller.DetailTVShowActivity;
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/18/2017.
 */

public class TvShowAdapter extends RecyclerView.Adapter<TvShowHolder> {

    Context context;
    private List<TvShow> tvshowsList;
    AppCompatActivity appCompatActivity;

    public TvShowAdapter(List<TvShow> tvshowsList, Context context, AppCompatActivity activity) {
        this.context = context;
        this.tvshowsList = tvshowsList;
        appCompatActivity = activity;
    }
    public TvShowAdapter(List<TvShow> tvshowsList, Context context) {
        this.context = context;
        this.tvshowsList = tvshowsList;
    }

    @Override
    public TvShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tvshow_item, parent, false);

        return new TvShowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TvShowHolder holder, final int position) {
        //Picasso.with(context).load(tvshowsList.get(position).getPosterPath()).fit().into(holder.posterImg);
        //Picasso.with(mContext).load(tvShowList.get(position).getImageDrawable()).resize(420,640).centerCrop().into(holder.posterImg);

        Picasso.with(context).load(Constants.IMAGE_PROFILE_URL + tvshowsList.get(position).getPosterPath()).error(R.drawable.image_failed).fit().into(holder.posterImg);

        holder.nameTxt.setText(tvshowsList.get(position).getNameShow());
        final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.nameTxt.setTypeface(type);
        holder.yearTxt.setText(tvshowsList.get(position).getYearOnAir());

        holder.posterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, DetailTVShowActivity.class);
                Bundle bundle = new Bundle();
                String tvID = tvshowsList.get(position).getTvID();
                String tvShowPosterPath = tvshowsList.get(position).getBackgroundPath();
                bundle.putString("id", tvID);
                bundle.putString("backdrop_path", tvShowPosterPath);
                myIntent.putExtra("tvshow_bundle", bundle);
                context.startActivity(myIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return tvshowsList.size();
    }
}
