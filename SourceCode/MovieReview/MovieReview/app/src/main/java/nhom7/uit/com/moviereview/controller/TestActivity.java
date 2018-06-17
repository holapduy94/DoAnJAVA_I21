package nhom7.uit.com.moviereview.controller;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.fragment.ShowingMovieFragment;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private MovieListAdapter mAdapter;
    private List<MovieData> mMovieData;
    TextView testview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*recyclerView = (RecyclerView) findViewById(R.id.showing_recycler_view);
        testview = (TextView) findViewById(R.id.testview);
        mMovieData = new ArrayList<>();*/

        new GetPopularFilm().execute("");





    }

    private class GetPopularFilm extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String MOV_LIST = "results";

                final String MOV_IMG = "poster_path";
                final String MOV_TITLE = "title";
                final String MOV_VOTE = "vote_average";
                final String MOV_RELEASE_DATE = "release_date";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject movie_popular = listMovie.getJSONObject(i);
                    MovieData mv = new MovieData();
                    mv.setTitle(movie_popular.getString(MOV_TITLE));
                    mv.setPosterPath(movie_popular.getString(MOV_IMG));
                    mv.setmVote(movie_popular.getString(MOV_VOTE));
                    mv.setDate(movie_popular.getString(MOV_RELEASE_DATE));
                    mMovieData.add(mv);
                }
                //mAdapter = new MovieListAdapter(mMovieData,TestActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key="+ Constants.API_KEY;
            String language = "language=en-US";
            String url = "https://api.themoviedb.org/3/movie/popular?" + api_key + "&" + language ;
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
