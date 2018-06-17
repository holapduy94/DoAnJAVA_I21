package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by PATRICKLAGGER on 5/29/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    ImageView likeImg;
    CircularImageView avatarImg;
    TextView nameTxt;
    TextView commentTxt;
    TextView timeTxt;
    TextView likeCountTxt;
    RatingBar ratingBar;
    public CommentViewHolder(View itemView) {
        super(itemView);
        avatarImg = (CircularImageView) itemView.findViewById(R.id.avatar_comment_img);
        nameTxt = (TextView) itemView.findViewById(R.id.name_comment_txt);
        commentTxt = (TextView) itemView.findViewById(R.id.comment_txt);
        timeTxt = (TextView) itemView.findViewById(R.id.time_comment_txt);
        ratingBar = (RatingBar) itemView.findViewById(R.id.comment_rating_bar);
        likeImg = (ImageView) itemView.findViewById(R.id.comment_like_img);
        likeCountTxt = (TextView) itemView.findViewById(R.id.comment_like_count_txt);
    }
}
