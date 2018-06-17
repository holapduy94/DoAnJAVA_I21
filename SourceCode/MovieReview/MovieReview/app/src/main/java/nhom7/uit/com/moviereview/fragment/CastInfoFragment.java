package nhom7.uit.com.moviereview.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ImagesArrayAdapter;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.adapter.PhotoActorAdapter;
import nhom7.uit.com.moviereview.controller.ActorPhotoSliderActivity;
import nhom7.uit.com.moviereview.controller.ImagesSliderActivity;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.ActorPhoto;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.MoviePhoto;
import nhom7.uit.com.moviereview.objects.GlobalVariable;
import nhom7.uit.com.moviereview.objects.ListImages;
import nhom7.uit.com.moviereview.objects.ListPhotoActor;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/28/2017.
 */

public class CastInfoFragment extends Fragment {

    ImageView poster_image_view;
    TextView tv_birthday, tv_deathday, tv_place, tv_info;
    GridView gridview_img_cast;

    String ID = "235731";
    List<ActorPhoto> actorPhotos;
    PhotoActorAdapter photoActorAdapter;

    public CastInfoFragment() {
        actorPhotos = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cast_info, container, false);

        poster_image_view = (ImageView) rootView.findViewById(R.id.poster_image_view_cast);

        tv_birthday = (TextView) rootView.findViewById(R.id.tv_birthday);
        tv_deathday = (TextView) rootView.findViewById(R.id.tv_deathday);
        tv_place = (TextView) rootView.findViewById(R.id.tv_place);
        tv_info = (TextView) rootView.findViewById(R.id.tv_info);

        gridview_img_cast = (GridView) rootView.findViewById(R.id.gridview_img_cast);

        // scroll gridview
        gridview_img_cast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        // click gridview
        gridview_img_cast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showImageAt(i);
            }
        });
        return rootView;
    }

    private void showImageAt(int i) {
        Intent intent = new Intent(getContext(), ActorPhotoSliderActivity.class);
        intent.putExtra("POSITION", i);
        if (GlobalVariable.listPhotoActor == null) {
            GlobalVariable.listPhotoActor = new ListPhotoActor();
            GlobalVariable.listPhotoActor.actorPhotoList = new ArrayList<>();
            GlobalVariable.listPhotoActor.actorPhotoList.addAll(actorPhotos);
        } else {
            GlobalVariable.listPhotoActor.actorPhotoList.clear();
            GlobalVariable.listPhotoActor.actorPhotoList.addAll(actorPhotos);
        }
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getString("id");
        }


        new GetDeTailActorByIDTask().execute(ID);
        new GetPhotoByACTORIDTask().execute(ID);

    }
    private class GetPhotoByACTORIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String id = strings[0];
            String url = "https://api.themoviedb.org/3/person/" + id + "/images?" + api_key;
            //resul=url;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            //resul=data;
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String IMG_RESULT = "profiles";
                final String IMG_PATH = "file_path";

                JSONObject resultObject = new JSONObject(s);
                JSONArray img_arr = resultObject.getJSONArray(IMG_RESULT);

                for (int i = 0; i < img_arr.length(); i++) {
                    JSONObject img_obj = img_arr.getJSONObject(i);
                    String img_path = img_obj.getString(IMG_PATH);
                    ActorPhoto actorPhoto = new ActorPhoto();
                    actorPhoto.setFile_path(img_path);
                    actorPhotos.add(actorPhoto);
                }

                photoActorAdapter = new PhotoActorAdapter(getContext(), actorPhotos);
                gridview_img_cast.setAdapter(photoActorAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class GetDeTailActorByIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                final String ACTOR_BIRTHDAY = "birthday";
                final String ACTOR_DEATHDAY = "deathday";
                final String ACTOR_NAME = "name";
                final String ACTOR_PLACE_OF_BIRTH = "place_of_birth";
                final String ACTOR_BIOGRAPHY = "biography";
                final String ACTOR_PROFILE_PATH = "profile_path";

                JSONObject result = new JSONObject(s);
                String profile_path = result.getString(ACTOR_PROFILE_PATH);
                Picasso.with(getContext()).load(Constants.IMAGE_PROFILE_URL+profile_path).error(R.drawable.image_failed).fit().into(poster_image_view);
                String birthday = result.getString(ACTOR_BIRTHDAY);
                String deathday = result.getString(ACTOR_DEATHDAY);
                String place_of_birth = result.getString(ACTOR_PLACE_OF_BIRTH);
                String biography = result.getString(ACTOR_BIOGRAPHY);



                if (result.getString(ACTOR_DEATHDAY).equalsIgnoreCase(""))
                    deathday = "Unknown";
                else if (result.getString(ACTOR_DEATHDAY).equalsIgnoreCase("null"))
                    deathday = "Unknown";
                else
                    deathday = result.getString(ACTOR_DEATHDAY);

                tv_birthday.setText(birthday);
                tv_deathday.setText(deathday);
                tv_place.setText(place_of_birth);
                tv_info.setText(biography);

                } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            // mMovieData.clear();
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String language = "language=en-US";
            String id = strings[0];
            String url = "https://api.themoviedb.org/3/person/" + id + "?" + api_key + "&" + language;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
    }
}
