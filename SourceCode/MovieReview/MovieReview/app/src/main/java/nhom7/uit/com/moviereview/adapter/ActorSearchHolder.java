package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/19/2017.
 */

public class ActorSearchHolder extends RecyclerView.ViewHolder {

    public ImageView posterImg;
    public TextView nameTxt;
    public ProgressBar progressbar_search_img_actor;

    public ActorSearchHolder(View itemView) {
        super(itemView);
        posterImg = (ImageView) itemView.findViewById(R.id.iv_actor);
        nameTxt = (TextView) itemView.findViewById(R.id.tv_name_actor);
        progressbar_search_img_actor = (ProgressBar) itemView.findViewById(R.id.progressbar_search_img_actor);
    }

    public ImageView getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(ImageView posterImg) {
        this.posterImg = posterImg;
    }

    public TextView getNameTxt() {
        return nameTxt;
    }

    public void setNameTxt(TextView nameTxt) {
        this.nameTxt = nameTxt;
    }
}
