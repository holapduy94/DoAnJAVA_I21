package nhom7.uit.com.moviereview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.model.ActorPhoto;
import nhom7.uit.com.moviereview.model.MoviePhoto;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/28/2017.
 */

public class PhotoActorAdapter extends BaseAdapter {

    private Context context;
    private List<ActorPhoto> imagesList;

    public PhotoActorAdapter(Context context, List<ActorPhoto> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int i) {
        return imagesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ActorPhoto details = imagesList.get(i);
        PhotoActorAdapter.ViewHolder viewHolder;

        if (view == null) {

            viewHolder = new PhotoActorAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_photo_actor, viewGroup, false);
            viewHolder.img_ImageImg = (ImageView) view.findViewById(R.id.itemimage_actor);
            view.setTag(viewHolder);

        }
        viewHolder = (PhotoActorAdapter.ViewHolder) view.getTag();

        Picasso.with(context).load(Constants.IMAGE_PROFILE_URL + details.getFile_path()).error(R.drawable.image_failed).fit().into(viewHolder.img_ImageImg);
        return view;
    }


    public class ViewHolder {
        public ImageView img_ImageImg;
    }
}
