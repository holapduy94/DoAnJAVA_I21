package nhom7.uit.com.moviereview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.controller.CastProfileActivity;
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/19/2017.
 */

public class ActorSearchAdapter extends RecyclerView.Adapter<ActorSearchHolder> {

    Context context;
    private List<Actor> actorsList;
    ProgressBar progressbar_search_actor_img;

    public ActorSearchAdapter(List<Actor> actorsList, Context context) {
        this.context = context;
        this.actorsList = actorsList;
    }


    @Override
    public ActorSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_people, parent, false);

        return new ActorSearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ActorSearchHolder holder, final int position) {
        holder.nameTxt.setText(actorsList.get(position).getmName());
        final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.nameTxt.setTypeface(type1);
        holder.progressbar_search_img_actor.setVisibility(View.VISIBLE);
        Picasso.with(context).load(actorsList.get(position).getmProfileImage()).error(R.drawable.image_failed).into(holder.posterImg, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar_search_img_actor.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressbar_search_img_actor.setVisibility(View.GONE);
            }
        });

        holder.posterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, CastProfileActivity.class);

                Bundle bundle = new Bundle();
                String actorID = actorsList.get(position).getmId();
                String actorName = actorsList.get(position).getmName();
                bundle.putString("id", actorID);
                bundle.putString("name", actorName);
                myIntent.putExtra("actor_bundle", bundle);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return actorsList.size();
    }
}
