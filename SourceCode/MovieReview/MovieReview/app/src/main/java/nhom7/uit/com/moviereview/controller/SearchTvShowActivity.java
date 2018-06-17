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
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorSearchAdapter;
import nhom7.uit.com.moviereview.adapter.TvShowAdapter;
import nhom7.uit.com.moviereview.adapter.TvShowSearchAdapter;
import nhom7.uit.com.moviereview.listener.EndlessRecyclerViewScrollListener;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.TvShow;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;
import nhom7.uit.com.moviereview.utils.FormatStringUrl;

import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_BASE_URL;
import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_PROFILE_URL;

public class SearchTvShowActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    SearchView mySearchView;
    List<TvShow> tvShowList;
    //TvShowSearchAdapter tvShowSearchAdapter;
    TvShowAdapter tvShowSearchAdapter;

    ProgressDialog progressDialog;
    RecyclerView.LayoutManager mLayoutManager;


    private EndlessRecyclerViewScrollListener scrollListener;
    List<TvShow> tvShowList_temp;
    String mquery = "";
    int mpages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_show);
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

        recyclerView = (RecyclerView) findViewById(R.id.lv_tvshow_list);
        tvShowList = new ArrayList<>();
        tvShowList_temp = new ArrayList<>();

        //mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager = new GridLayoutManager(SearchTvShowActivity.this, 2);

        recyclerView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                tvShowList_temp = tvShowList;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mpages = page+1;
                        //Toast.makeText(SearchPeopleActivity.this, "day la :" + mpages, Toast.LENGTH_SHORT).show();
                        loadNextDataFromApi();
                    }
                }, 2000);

            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

    }

    private void loadNextDataFromApi() {
        new LoadmoreTask().execute(mquery);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_tvshow_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.look_movie);
        mySearchView = (SearchView) menuItem.getActionView();
        mySearchView.setOnQueryTextListener(this);
        mySearchView.setQueryHint("Tìm theo tên Tvshow ");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        tvShowList.clear();
        mquery = query;

        new SearchTvShowTask().execute(query);
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
                final String MOV_BACHGROUND_PATH = "backdrop_path";
                final String MOV_LIST = "results";
                final String MOV_TITLE = "original_name";
                final String MOV_YEAR = "first_air_date";
                final String MOV_POSTER = "poster_path";
                final String MOV_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                if (listMovie.length() > 0) {
                    for (int i = 0; i < listMovie.length(); i++) {
                        JSONObject tvShow = listMovie.getJSONObject(i);
                        TvShow tv = new TvShow();
                        tv.setNameShow(tvShow.getString(MOV_TITLE));
                        if (tvShow.getString(MOV_YEAR).equalsIgnoreCase(""))
                            tv.setYearOnAir("Unknow");
                        else
                            tv.setYearOnAir(tvShow.getString(MOV_YEAR));
                        //tv.setPosterPath(IMAGE_PROFILE_URL.concat(tvShow.getString(MOV_POSTER)));
                        tv.setPosterPath(tvShow.getString(MOV_POSTER));

                        Log.d("watch_year", tv.getYearOnAir());
                        tv.setTvID(tvShow.getString(MOV_ID));
                        tv.setBackgroundPath(tvShow.getString(MOV_BACHGROUND_PATH));
                        tvShowList.add(tv);
                    }
                    tvShowSearchAdapter.notifyItemRangeInserted(tvShowList_temp.size() - 1, tvShowList.size() - 1);
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
            String url = "https://api.themoviedb.org/3/search/tv?" + api_key + "&" + language + "&" + query + "&" + page + "&" + adult;

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
    private class SearchTvShowTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String MOV_BACHGROUND_PATH = "backdrop_path";
                final String MOV_LIST = "results";
                final String MOV_TITLE = "original_name";
                final String MOV_YEAR = "first_air_date";
                final String MOV_POSTER = "poster_path";
                final String MOV_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(MOV_LIST);
                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject tvShow = listMovie.getJSONObject(i);
                    TvShow tv = new TvShow();
                    tv.setNameShow(tvShow.getString(MOV_TITLE));
                    if (tvShow.getString(MOV_YEAR).equalsIgnoreCase(""))
                        tv.setYearOnAir("Unknow");
                    else
                        tv.setYearOnAir(tvShow.getString(MOV_YEAR));
                   // tv.setPosterPath(IMAGE_PROFILE_URL.concat(tvShow.getString(MOV_POSTER)));
                    tv.setPosterPath(tvShow.getString(MOV_POSTER));

                    Log.d("watch_year", tv.getYearOnAir());
                    tv.setTvID(tvShow.getString(MOV_ID));
                    tv.setBackgroundPath(tvShow.getString(MOV_BACHGROUND_PATH));
                    tvShowList.add(tv);
                }
                progressDialog.dismiss();
                //tvShowSearchAdapter.notifyDataSetChanged();
                tvShowSearchAdapter = new TvShowAdapter(tvShowList, SearchTvShowActivity.this);
                recyclerView.setAdapter(tvShowSearchAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            //String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String api_key = "api_key=" + Constants.API_KEY;

            String language = "language=en-US";
            String query_input = FormatStringUrl.SpaceTo(strings[0]);

            String query = "query=" + query_input;
            String url = "https://api.themoviedb.org/3/search/tv?" + api_key + "&" + language + "&" + query;
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
