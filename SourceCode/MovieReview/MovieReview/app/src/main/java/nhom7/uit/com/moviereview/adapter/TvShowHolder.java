package nhom7.uit.com.moviereview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nhom7.uit.com.moviereview.R;

/**
 * Created by phuocthang on 10/18/2017.
 */

public class TvShowHolder extends RecyclerView.ViewHolder {
    public ImageView posterImg;
    public TextView nameTxt;
    public TextView yearTxt;

    public TvShowHolder(View itemView) {
        super(itemView);
        posterImg = (ImageView) itemView.findViewById(R.id.tv_show_poster);
        nameTxt = (TextView) itemView.findViewById(R.id.name_tv_show);
        yearTxt = (TextView) itemView.findViewById(R.id.year_on_air);
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

    public TextView getYearTxt() {
        return yearTxt;
    }

    public void setYearTxt(TextView yearTxt) {
        this.yearTxt = yearTxt;
    }
}
