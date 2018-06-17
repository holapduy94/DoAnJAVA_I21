package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/20/2017.
 */

public class MovieSearchHolder extends RecyclerView.ViewHolder {
    public CardView cv_item_movie;
    public ImageView iv_movie ;
    public TextView movie_name;
    public TextView movie_date;
    public TextView movie_vote;
    public ProgressBar progressbar_search_movie;

    public MovieSearchHolder(View itemView) {
        super(itemView);
        cv_item_movie = (CardView) itemView.findViewById(R.id.cv_item_movie);
        iv_movie = (ImageView) itemView.findViewById(R.id.iv_movie);
        movie_name = (TextView) itemView.findViewById(R.id.movie_name);
        movie_date = (TextView) itemView.findViewById(R.id.movie_date);
        movie_vote = (TextView) itemView.findViewById(R.id.movie_vote);

        progressbar_search_movie = (ProgressBar) itemView.findViewById(R.id.progressbar_search_movie);
    }
}
