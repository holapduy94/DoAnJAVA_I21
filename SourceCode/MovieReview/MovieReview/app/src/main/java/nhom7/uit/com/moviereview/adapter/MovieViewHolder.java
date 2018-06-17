package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/15/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView imdb;
    TextView khoichieu;
    ImageView imageView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        imdb = (TextView) itemView.findViewById(R.id.imdb);
        khoichieu = (TextView) itemView.findViewById(R.id.khoichieu);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
