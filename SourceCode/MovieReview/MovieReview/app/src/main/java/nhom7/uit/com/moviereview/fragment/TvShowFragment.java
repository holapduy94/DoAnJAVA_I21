package nhom7.uit.com.moviereview.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.adapter.TvShowAdapter;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/15/2017.
 */

public class TvShowFragment extends Fragment {

    SwipeRefreshLayout swipe_refresh_tvshow_movie;
    RecyclerView recyclerView;
    private TvShowAdapter mAdapter;
    private List<TvShow> mTvShowData;

    RecyclerView.LayoutManager mLayoutManager;
    //GridLayoutManager mLayoutManager2;

    public TvShowFragment() {
        mTvShowData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_show, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tvshow_recycler_view);
        swipe_refresh_tvshow_movie = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_tvshow_movie);
        swipe_refresh_tvshow_movie.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorFloatButtonv1));
        new GetTvShow().execute("1");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe_refresh_tvshow_movie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetTvShow().execute("1");
            }
        });
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private class GetTvShow extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTvShowData.clear();

            try {
                final String MOV_ID = "id";

                final String MOV_LIST = "results";
                final String MOV_IMG = "poster_path";
                final String MOV_BACKGROUND = "backdrop_path";
                final String MOV_TITLE = "name";
                final String MOV_RELEASE_DATE = "first_air_date";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject movie_popular = listMovie.getJSONObject(i);
                    TvShow mv = new TvShow();
                    mv.setNameShow(movie_popular.getString(MOV_TITLE));
                    mv.setPosterPath(movie_popular.getString(MOV_IMG));
                    mv.setYearOnAir(movie_popular.getString(MOV_RELEASE_DATE));
                    mv.setBackgroundPath(movie_popular.getString(MOV_BACKGROUND));
                    mv.setTvID(movie_popular.getString(MOV_ID));
                    mTvShowData.add(mv);
                }
                mAdapter = new TvShowAdapter(mTvShowData, getContext(), (AppCompatActivity) getActivity());
                recyclerView.setAdapter(mAdapter);
                swipe_refresh_tvshow_movie.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String language = "language=en-US";
            String page = "page=";
            String url = "https://api.themoviedb.org/3/tv/popular?" + api_key + "&" + language + "&" + page;
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
}
