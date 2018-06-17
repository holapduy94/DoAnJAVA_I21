package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/25/2017.
 */

public class ActorDetailAdapter extends  RecyclerView.Adapter<ActorDetailViewHolder> {

    Context context;
    private List<Actor> acTorsList;
    AppCompatActivity appCompatActivity;

    public ActorDetailAdapter(List<Actor> acTorsList,Context context, AppCompatActivity activity) {
        this.context=context;
        this.acTorsList = acTorsList;
        appCompatActivity = activity;
    }

    public ActorDetailAdapter(List<Actor> acTorsList,Context context) {
        this.context=context;
        this.acTorsList = acTorsList;
    }

    @Override
    public ActorDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actor, parent, false);

        return new ActorDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ActorDetailViewHolder holder, int position) {


        holder.textview_job.setText(acTorsList.get(position).getmRole());
        holder.textview_name.setText(acTorsList.get(position).getmName());

        final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        final Typeface type1 = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.textview_job.setTypeface(type);
        holder.textview_name.setTypeface(type1);

        holder.progressbar_actor_detail.setVisibility(View.VISIBLE);
        Picasso.with(context).load(Constants.IMAGE_PROFILE_URL+acTorsList.get(position).getmProfileImage()).error(R.drawable.image_failed).into(holder.imageview_profile, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar_actor_detail.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressbar_actor_detail.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return acTorsList.size();
    }
}
