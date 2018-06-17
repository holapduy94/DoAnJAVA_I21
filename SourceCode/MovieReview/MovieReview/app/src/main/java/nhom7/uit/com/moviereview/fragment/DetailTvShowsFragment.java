package nhom7.uit.com.moviereview.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorDetailAdapter;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/29/2017.
 */

public class DetailTvShowsFragment extends Fragment {

    ImageView tv_show_poster;
    TextView tv_show_name_txt, tv_show_genres, tv_vote_point_txt, tv_show_popularity, tv_show_language, button_toggle;
    ExpandableTextView expandableTextView;
    RecyclerView tv_cast_recycler_view;


    List<Actor> actorList;
    ActorDetailAdapter actorDetailAdapter;


    String ID = "66393";
    String languages = "";
    String genre_name = "";

    public DetailTvShowsFragment() {
        actorList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_tvshows, container, false);

        final Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Light.ttf");
        final Typeface type1 = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        final Typeface type2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Bold.ttf");

        tv_show_poster = (ImageView) rootView.findViewById(R.id.tv_show_poster);

        tv_show_name_txt = (TextView) rootView.findViewById(R.id.tv_show_name_txt);
        tv_show_genres = (TextView) rootView.findViewById(R.id.tv_show_genres);
        tv_vote_point_txt = (TextView) rootView.findViewById(R.id.tv_vote_point_txt);
        tv_show_popularity = (TextView) rootView.findViewById(R.id.tv_show_popularity);
        tv_show_language = (TextView) rootView.findViewById(R.id.tv_show_language);
        button_toggle = (TextView) rootView.findViewById(R.id.button_toggle);

        expandableTextView = (ExpandableTextView) rootView.findViewById(R.id.expandableTextView);
        tv_cast_recycler_view = (RecyclerView) rootView.findViewById(R.id.tv_cast_recycler_view);



        tv_show_name_txt.setTypeface(type2);
        tv_show_genres.setTypeface(type1);
        tv_vote_point_txt.setTypeface(type);
        expandableTextView.setTypeface(type);
        tv_show_popularity.setTypeface(type);
        tv_show_language.setTypeface(type1);
        button_toggle.setTypeface(type);


        button_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (expandableTextView.isExpanded()) {
                    expandableTextView.collapse();
                    button_toggle.setText("Xem thêm");
                } else {
                    expandableTextView.expand();
                    button_toggle.setText("Thu gọn");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getString("id");

        }

        new GetTvShowByIDTask().execute(ID);
        new GetCastByTVShowIDTask().execute(ID);
        tv_cast_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
    }

    private class GetTvShowByIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String TV_IMG = "poster_path";
                final String TV_TITLE = "original_name";
                final String TV_RELEASE_DATE = "first_air_date";
                final String TV_GENRE = "genres";
                final String TV_RATING = "vote_average";
                final String TV_VOTES = "vote_count";
                final String TV_POPULARITY = "popularity";

                final String TV_LANGUAGE = "languages";
                final String TV_OVERVIEW = "overview";
                final String TV_GENRE_NAME = "name";

                final String TV_HOME_PAGE = "homepage";


                JSONObject tvShowObject = new JSONObject(s);

                String poster_path = tvShowObject.getString(TV_IMG);
                String original_name = tvShowObject.getString(TV_TITLE);
                String vote_average = tvShowObject.getString(TV_RATING);
                String vote_count = tvShowObject.getString(TV_VOTES);
                String popularity = tvShowObject.getString(TV_POPULARITY);

                Constants.share_info_homepage_tv = tvShowObject.getString(TV_HOME_PAGE);



                String over_view = tvShowObject.getString(TV_OVERVIEW);

                JSONArray languages_arr = tvShowObject.getJSONArray(TV_LANGUAGE);

                JSONArray genres_arr = tvShowObject.getJSONArray(TV_GENRE);

                Picasso.with(getContext()).load(Constants.IMAGE_BASE_URL + poster_path).fit().into(tv_show_poster);
                tv_show_name_txt.setText(original_name);
                tv_vote_point_txt.setText(vote_average);
                tv_show_popularity.setText(popularity);
                expandableTextView.setText(over_view);
                for (int i = 0; i < languages_arr.length(); i++) {
                    String a = languages_arr.getString(i);
                    languages = languages + a + "\r";

                }
                tv_show_language.setText(languages);
                for (int i = 0; i < genres_arr.length(); i++) {
                    JSONObject genres_arrJSONObject = genres_arr.getJSONObject(i);
                    genre_name = genre_name + genres_arrJSONObject.getString(TV_GENRE_NAME) + "\r";
                }
                tv_show_genres.setText(genre_name);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            languages = "";
            genre_name = "";
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String id = strings[0];
            String language = "language=en-US";
            String url = "https://api.themoviedb.org/3/tv/" + id + "?" + api_key + "&" + language;
            Log.d("LoiURL", url);

            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            return data;
        }
    }

    private class GetCastByTVShowIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            //String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String api_key = "api_key=" + Constants.API_KEY;

            String id = strings[0];
            String url = "https://api.themoviedb.org/3/tv/" + id + "/credits?" + api_key;
            Log.d("log_url_cast", url);

            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("log_s",s);
            try {
                final String CAST = "cast";
                final String CAST_ID = "cast_id";
                final String CAST_ROLE_NAME = "character";
                final String CREADIT_ID = "credit_id";
                final String ID = "id";
                final String CAST_NAME = "name";
                final String CAST_IMG = "profile_path";


                JSONObject resultObject = new JSONObject(s);

                JSONArray cast_arr = resultObject.getJSONArray(CAST);
                Log.d("log_actor_cast_arr",cast_arr.toString());


                for (int i = 0; i < cast_arr.length(); i++) {
                    JSONObject cast_obj = cast_arr.getJSONObject(i);
                    String cast_name = cast_obj.getString(CAST_NAME);
                    String cast_role = cast_obj.getString(CAST_ROLE_NAME);
                    String cast_profile_img = cast_obj.getString(CAST_IMG);
                    String id = cast_obj.getString(ID);
                    Log.d("log__id",id);


                    Actor actor = new Actor();
                    actor.setmId(id);
                    actor.setmProfileImage(cast_profile_img);
                    actor.setmRole(cast_role);
                    actor.setmName(cast_name);

                    actorList.add(actor);
                }

                actorDetailAdapter = new ActorDetailAdapter(actorList, getContext(),(AppCompatActivity) getActivity());
                tv_cast_recycler_view.setAdapter(actorDetailAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
