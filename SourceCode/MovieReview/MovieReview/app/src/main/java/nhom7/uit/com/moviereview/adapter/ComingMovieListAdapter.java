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
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/27/2017.
 */

public class ComingMovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    Context context;
    private List<MovieData> moviesList;
    AppCompatActivity appCompatActivity;

    public ComingMovieListAdapter(List<MovieData> moviesList, Context context, AppCompatActivity activity) {
        this.context = context;
        this.moviesList = moviesList;
        appCompatActivity = activity;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.title.setText(moviesList.get(position).getTitle());
        holder.khoichieu.setText(moviesList.get(position).getDate());
        holder.imdb.setText(moviesList.get(position).getmVote());
//        holder.desciption.setText(list.get(position).getDescription());
        final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.title.setTypeface(type1);
        holder.khoichieu.setTypeface(type1);
        holder.imdb.setTypeface(type1);

//        new DownloadImageTask(holder.imageView).execute(list.get(position).getPosterPath());
        Picasso.with(context).load(Constants.IMAGE_PROFILE_URL + moviesList.get(position).getPosterPath()).error(R.drawable.image_failed).into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"click item "+position,Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, DetailsMovieActivity.class);
                //Khai báo Bundle
                Bundle bundle = new Bundle();
                String movieID = moviesList.get(position).getID();
                String moviePathBanner = moviesList.get(position).getPosterPanel();
                //đưa dữ liệu riêng lẻ vào Bundle
                bundle.putString("id", movieID);
                bundle.putString("backdrop_path", moviePathBanner);
                //Đưa Bundle vào Intent
                myIntent.putExtra("showing_movie_bundle", bundle);

                //Mở Activity ResultActivity
                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
