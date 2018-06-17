package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/20/2017.
 */

public class LoadingViewHolder extends MovieSearchHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
    }
}
