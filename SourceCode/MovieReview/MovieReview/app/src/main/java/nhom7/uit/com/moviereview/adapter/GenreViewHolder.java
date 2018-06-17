package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/25/2017.
 */

public class GenreViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewName;

    public GenreViewHolder(View itemView) {
        super(itemView);
        textViewName = (TextView) itemView.findViewById(R.id.textview_name);
    }
}
