package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.model.MovieData;

/**
 * Created by phuocthang on 10/19/2017.
 */

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    /*
        private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;*/

    Context context;
    private List<MovieData> tvShowsList;

    public MovieSearchAdapter(List<MovieData> tvShowsList, Context context) {
        this.context = context;
        this.tvShowsList = tvShowsList;
    }


    @Override
    public MovieSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_search, parent, false);*/
        /*return new MovieSearchHolder(itemView);*/

        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie_search, parent, false);
            return new MovieSearchHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MovieSearchHolder holder, final int position) {
        holder.movie_name.setText(tvShowsList.get(position).getTitle());
        holder.movie_date.setText(tvShowsList.get(position).getDate());
        holder.movie_vote.setText(tvShowsList.get(position).getmVote());
        final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.movie_name.setTypeface(type);
        holder.movie_date.setTypeface(type1);
        holder.movie_vote.setTypeface(type1);


        holder.progressbar_search_movie.setVisibility(View.VISIBLE);
        Picasso.with(context).load(tvShowsList.get(position).getPosterPath()).error(R.drawable.ic_broken_image).fit().into(holder.iv_movie, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar_search_movie.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressbar_search_movie.setVisibility(View.GONE);
            }
        });

        holder.cv_item_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieData movieData = tvShowsList.get(position);
                Intent myIntent = new Intent(context, DetailsMovieActivity.class);
                Bundle bundle = new Bundle();
                String movieID = movieData.getID();
                String moviePathBanner = movieData.getPosterPanel();
                bundle.putString("id", movieID);
                bundle.putString("backdrop_path", moviePathBanner);
                myIntent.putExtra("showing_movie_bundle", bundle);
                context.startActivity(myIntent);
            }
        });


        /*if (holder instanceof MovieSearchHolder) {
            holder.movie_name.setText(tvShowsList.get(position).getTitle());
            holder.movie_date.setText(tvShowsList.get(position).getDate());
            holder.movie_vote.setText(tvShowsList.get(position).getmVote());

            final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
            final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
            holder.movie_name.setTypeface(type);
            holder.movie_date.setTypeface(type1);
            holder.movie_vote.setTypeface(type1);


            holder.progressbar_search_movie.setVisibility(View.VISIBLE);
            Picasso.with(context).load(tvShowsList.get(position).getPosterPath()).error(R.drawable.ic_broken_image).fit().into(holder.iv_movie, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressbar_search_movie.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.progressbar_search_movie.setVisibility(View.GONE);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }*/
    }

    @Override
    public int getItemCount() {
        /*return tvShowsList.size();*/
        return tvShowsList == null ? 0 : tvShowsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return tvShowsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

    }



    /*public void OnScroll(RecyclerView mRecyclerView){
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

 public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

    public void setLoaded() {
        isLoading = false;
    }*/


}
