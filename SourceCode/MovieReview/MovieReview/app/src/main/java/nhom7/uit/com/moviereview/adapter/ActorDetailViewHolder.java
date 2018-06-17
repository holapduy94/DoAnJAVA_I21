package nhom7.uit.com.moviereview.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/25/2017.
 */

public class ActorDetailViewHolder extends RecyclerView.ViewHolder  {
    public TextView textview_job;
    public TextView textview_name;
    public ProgressBar progressbar_actor_detail;
    public ImageView imageview_profile;

    public ActorDetailViewHolder(View itemView) {
        super(itemView);
        imageview_profile = (ImageView) itemView.findViewById(R.id.imageview_profile);
        textview_job = (TextView) itemView.findViewById(R.id.textview_job);
        textview_name = (TextView) itemView.findViewById(R.id.textview_name);

        progressbar_actor_detail = (ProgressBar) itemView.findViewById(R.id.progressbar_actor_detail);
    }
}
