package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.model.Comment;

/**
 * Created by PATRICKLAGGER on 5/29/2017.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    List<Comment> listComment = new ArrayList<>();
    Context mContext;
    public CommentListAdapter(ArrayList<Comment> comments, Context context) {
        super();
        listComment = comments;
        mContext = context;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row_layout, parent, false);
        CommentViewHolder holder = new CommentViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Picasso.with(mContext).load(listComment.get(position).getUrlAvatar()).into(holder.avatarImg);
        holder.nameTxt.setText(listComment.get(position).getUserName());
        holder.commentTxt.setText(listComment.get(position).getComment());
        holder.ratingBar.setRating(Float.parseFloat(listComment.get(position).getPoint()));
        holder.timeTxt.setText(listComment.get(position).getDate());
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        Typeface typeface1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        holder.commentTxt.setTypeface(typeface);
        holder.nameTxt.setTypeface(typeface1);
        holder.timeTxt.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }
}
