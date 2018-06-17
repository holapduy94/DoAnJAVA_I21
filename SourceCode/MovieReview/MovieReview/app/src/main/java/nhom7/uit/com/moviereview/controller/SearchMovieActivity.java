package nhom7.uit.com.moviereview.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.adapter.MovieSearchAdapter;
import nhom7.uit.com.moviereview.adapter.TvShowSearchAdapter;
import nhom7.uit.com.moviereview.listener.EndlessRecyclerViewScrollListener;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;
import nhom7.uit.com.moviereview.utils.FormatStringUrl;

import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_BASE_URL;
import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_PROFILE_URL;

public class SearchMovieActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    RecyclerView lv_movie_list;
    SearchView mySearchView;
    List<MovieData> moVieList;

    //MovieSearchAdapter moVieSearchAdapter;
    MovieListAdapter moVieSearchAdapter;

    ProgressDialog progressDialog;
    //LinearLayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager;


    long toTalPages;


    private EndlessRecyclerViewScrollListener scrollListener;
    List<MovieData> moVieList_temp;
    String mquery = "";
    int mpages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        addViewControls();
        addEvents();
    }

    private void addEvents() {


    }

    private void addViewControls() {
        toolbar = (Toolbar) findViewById(R.id.search_movie_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_movie_list = (RecyclerView) findViewById(R.id.lv_movie_list);
        moVieList = new ArrayList<>();
        moVieList_temp = new ArrayList<>();

        //mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager = new GridLayoutManager(SearchMovieActivity.this, 2);
        lv_movie_list.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                moVieList_temp = moVieList;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mpages = page + 1;
                        //Toast.makeText(SearchMovieActivity.this, "day la :" + mpages, Toast.LENGTH_SHORT).show();
                        loadNextDataFromApi();
                    }
                }, 2000);
            }
        };
        lv_movie_list.addOnScrollListener(scrollListener);
    }

    private void loadNextDataFromApi() {
        new LoadmoreTask().execute(mquery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_movie_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_movie_id);
        mySearchView = (SearchView) menuItem.getActionView();
        mySearchView.setOnQueryTextListener(this);
        mySearchView.setQueryHint("Tìm theo tên phim ");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        moVieList.clear();
        mquery = query;
        new SearchMovieTask().execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class LoadmoreTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String MOV_BANNER_PATH = "backdrop_path";
                final String MOV_LIST = "results";
                final String MOV_TITLE = "title";
                final String MOV_DATE = "release_date";
                final String MOV_POSTER = "poster_path";
                final String MOV_VOTE = "vote_average";
                final String MOV_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);

                if (listMovie.length() > 0) {

                    for (int i = 0; i < listMovie.length(); i++) {
                        JSONObject movie = listMovie.getJSONObject(i);
                        MovieData movieData = new MovieData();
                        movieData.setID(movie.getString(MOV_ID));
                        movieData.setTitle(movie.getString(MOV_TITLE));
                        movieData.setDate(movie.getString(MOV_DATE));
                        movieData.setmVote(movie.getString(MOV_VOTE));
                        movieData.setPosterPanel(movie.getString(MOV_BANNER_PATH));
                        //movieData.setPosterPath(IMAGE_PROFILE_URL.concat(movie.getString(MOV_POSTER)));
                        movieData.setPosterPath(movie.getString(MOV_POSTER));
                        moVieList.add(movieData);

                    }
                    moVieSearchAdapter.notifyItemRangeInserted(moVieList_temp.size() - 1, moVieList.size() - 1);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;

            String language = "language=en-US";
            String query_input = FormatStringUrl.SpaceTo(strings[0]);
            String page = "page=" + mpages;
            String query = "query=" + query_input;
            String adult = "include_adult=false";
            String url = "https://api.themoviedb.org/3/search/movie?" + api_key + "&" + language + "&" + query + "&" + page + "&" + adult;

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

    private class SearchMovieTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String MOV_TOTAL_PAGE = "total_pages";
                final String MOV_BANNER_PATH = "backdrop_path";
                final String MOV_LIST = "results";
                final String MOV_TITLE = "title";
                final String MOV_DATE = "release_date";
                final String MOV_POSTER = "poster_path";
                final String MOV_VOTE = "vote_average";
                final String MOV_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                String total_pages = tvShowObject.getString(MOV_TOTAL_PAGE);
                Log.d("watch_total_page", total_pages);
                toTalPages = Long.parseLong(total_pages);
                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject movie = listMovie.getJSONObject(i);
                    MovieData movieData = new MovieData();
                    movieData.setID(movie.getString(MOV_ID));
                    movieData.setTitle(movie.getString(MOV_TITLE));
                    movieData.setDate(movie.getString(MOV_DATE));
                    movieData.setmVote(movie.getString(MOV_VOTE));
                    movieData.setPosterPanel(movie.getString(MOV_BANNER_PATH));
                    //movieData.setPosterPath(IMAGE_PROFILE_URL.concat(movie.getString(MOV_POSTER)));
                    movieData.setPosterPath(movie.getString(MOV_POSTER));
                    moVieList.add(movieData);

                }
                progressDialog.dismiss();
                //moVieSearchAdapter = new MovieSearchAdapter(moVieList, SearchMovieActivity.this);
                moVieSearchAdapter = new MovieListAdapter(moVieList, SearchMovieActivity.this);

                lv_movie_list.setAdapter(moVieSearchAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;

            //String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String language = "language=en-US";
            String query_input = FormatStringUrl.SpaceTo(strings[0]);
            Log.d("watch_query_input", query_input);
            String query = "query=" + query_input;
            String adult = "include_adult=false";
            //String page = "page=";
            String url = "https://api.themoviedb.org/3/search/movie?" + api_key + "&" + language + "&" + query + "&" + adult;

            Log.d("watch_url", url);
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
