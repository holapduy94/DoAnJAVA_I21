package nhom7.uit.com.moviereview.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import nhom7.uit.com.moviereview.adapter.ComingMovieListAdapter;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.adapter.TvShowAdapter;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/28/2017.
 */

public class CastMovieFragment extends Fragment {

    String ID = "235731";

    RecyclerView recyclerview_cast_tvshow;
    TvShowAdapter mAdapter;
    List<TvShow> mTvShowData;
    RecyclerView.LayoutManager mLayoutManager;


    RecyclerView recyclerview_cast_movie;
    ComingMovieListAdapter movieAdapter;
    List<MovieData> mMovieData;
    RecyclerView.LayoutManager movieLayoutManager;

    public CastMovieFragment() {
        mTvShowData = new ArrayList<>();
        mMovieData = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cast_movie, container, false);

        recyclerview_cast_tvshow = (RecyclerView) rootView.findViewById(R.id.recyclerview_cast_tvshow);
        recyclerview_cast_movie = (RecyclerView) rootView.findViewById(R.id.recyclerview_cast_movie);

        mLayoutManager = new GridLayoutManager(getContext(), 2);
        movieLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerview_cast_tvshow.setLayoutManager(mLayoutManager);
        recyclerview_cast_movie.setLayoutManager(movieLayoutManager);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getString("id");
            Log.d("xem_id_movie_cast",ID);

        }
        new GetTvShowByActorIDTask().execute(ID);
        new GetMovieByActorIDTask().execute(ID);
    }

    private class GetTvShowByActorIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String TV_RESULT = "cast";
                final String TV_ID = "id";
                final String TV_IMG = "poster_path";
                final String TV_BACKGROUND = "backdrop_path";
                final String TV_TITLE = "name";
                final String TV_RELEASE_DATE = "first_air_date";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray cast_arr = tvShowObject.getJSONArray(TV_RESULT);
                for (int i = 0; i < cast_arr.length(); i++) {
                    JSONObject movie_popular = cast_arr.getJSONObject(i);
                    TvShow tv = new TvShow();
                    tv.setNameShow(movie_popular.getString(TV_TITLE));
                    tv.setPosterPath(movie_popular.getString(TV_IMG));
                    tv.setYearOnAir(movie_popular.getString(TV_RELEASE_DATE));
                    tv.setBackgroundPath(movie_popular.getString(TV_BACKGROUND));
                    tv.setTvID(movie_popular.getString(TV_ID));
                    mTvShowData.add(tv);
                }
                mAdapter = new TvShowAdapter(mTvShowData, getContext(), (AppCompatActivity) getActivity());
                recyclerview_cast_tvshow.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String language = "language=en-US";
            String id = strings[0];
            String url = "https://api.themoviedb.org/3/person/" + id + "/tv_credits?" + api_key + "&" + language;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
    }

    private class GetMovieByActorIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                final String MOV_LIST = "cast";
                final String MOV_IMG = "poster_path";
                final String MOV_TITLE = "title";
                final String MOV_VOTE = "vote_average";
                final String MOV_RELEASE_DATE = "release_date";
                final String MOV_BANNER = "backdrop_path";
                final String MOV_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject movie_popular = listMovie.getJSONObject(i);
                    MovieData mv = new MovieData();
                    mv.setID(movie_popular.getString(MOV_ID));
                    mv.setTitle(movie_popular.getString(MOV_TITLE));
                    mv.setPosterPath(movie_popular.getString(MOV_IMG));
                    mv.setmVote(movie_popular.getString(MOV_VOTE));
                    mv.setDate(movie_popular.getString(MOV_RELEASE_DATE));
                    mv.setPosterPanel(movie_popular.getString(MOV_BANNER));

                    mMovieData.add(mv);
                }
                movieAdapter = new ComingMovieListAdapter(mMovieData, getContext(), (AppCompatActivity) getActivity());
                recyclerview_cast_movie.setAdapter(movieAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String language = "language=en-US";
            String id = strings[0];
            String url = "https://api.themoviedb.org/3/person/" + id + "/movie_credits?" + api_key + "&" + language;
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
