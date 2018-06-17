package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/19/2017.
 */

public class TvShowSearchHolder extends RecyclerView.ViewHolder {
    public TextView tv_tvShow_name;
    public TextView tv_tvShow_year;
    public ImageView iv_tvShow;
    public ProgressBar progressbar_search;
    public CardView cv_tvshows;
    public TvShowSearchHolder(View itemView) {
        super(itemView);
        cv_tvshows = (CardView) itemView.findViewById(R.id.cv_tvshows);
        iv_tvShow = (ImageView) itemView.findViewById(R.id.iv_tvShow);
        tv_tvShow_name = (TextView) itemView.findViewById(R.id.tv_tvShow_name);
        tv_tvShow_year = (TextView) itemView.findViewById(R.id.tv_tvShow_year);
        progressbar_search = (ProgressBar) itemView.findViewById(R.id.progressbar_search);
    }
}
