package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.model.MovieVideo;

/**
 * Created by phuocthang on 10/27/2017.
 */

public class VideoArrayAdapter extends BaseAdapter {

    private List<MovieVideo> videoList;
    private Context context;

    public VideoArrayAdapter(List<MovieVideo> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MovieVideo details = videoList.get(i);
        ViewHolder viewHolder;

        if (view == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_video_detail, viewGroup, false);
            viewHolder.videoTitle = (TextView) view.findViewById(R.id.video_title);
            viewHolder.videoLengh = (TextView) view.findViewById(R.id.video_lengh);
            viewHolder.img_videoImg = (ImageView) view.findViewById(R.id.img_videoImg);

            view.setTag(viewHolder);

        }

        viewHolder = (ViewHolder) view.getTag();
        viewHolder.videoTitle.setText(details.getName());
        viewHolder.videoLengh.setText(String.valueOf(details.getSize()));
        // set image for video
        Picasso.with(context).load(details.getImageURL()).fit().into(viewHolder.img_videoImg);

        return view;
    }

    public class ViewHolder {
        public TextView videoTitle;
        public ImageView img_videoImg;
        public TextView videoLengh;
    }
}
