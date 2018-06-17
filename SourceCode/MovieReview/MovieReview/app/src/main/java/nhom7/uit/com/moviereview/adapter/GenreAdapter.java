package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.MovieGenre;

/**
 * Created by phuocthang on 10/25/2017.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder> {
    Context context;
    private List<MovieGenre> genreList;
    AppCompatActivity appCompatActivity;

    public GenreAdapter(List<MovieGenre> genreList, Context context, AppCompatActivity activity) {
        this.context=context;
        this.genreList = genreList;
        appCompatActivity = activity;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);

        return new GenreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        holder.textViewName.setText(genreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}
