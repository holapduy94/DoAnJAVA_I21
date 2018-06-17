package nhom7.uit.com.moviereview.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorSearchAdapter;
import nhom7.uit.com.moviereview.listener.EndlessRecyclerViewScrollListener;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;
import nhom7.uit.com.moviereview.utils.FormatStringUrl;

import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_PROFILE_URL;

public class SearchPeopleActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    SearchView mySearchView;
    List<Actor> acTorList;

    ActorSearchAdapter acTorSearchAdapter;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager mLayoutManager;

    private EndlessRecyclerViewScrollListener scrollListener;
    List<Actor> actorListTemp;
    String mquery = "";
    int mpages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);
        addViewControls();
        addEvents();
    }

    private void addEvents() {

    }

    private void addViewControls() {
        toolbar = (Toolbar) findViewById(R.id.search_actor_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        acTorList = new ArrayList<>();
        actorListTemp = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.actor_search_recycler_view);

        mLayoutManager = new GridLayoutManager(SearchPeopleActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                actorListTemp = acTorList;
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
        getMenuInflater().inflate(R.menu.search_actor_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_actor_id);
        mySearchView = (SearchView) menuItem.getActionView();
        mySearchView.setOnQueryTextListener(this);
        mySearchView.setQueryHint("Tìm theo tên diễn viên ");

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
        acTorList.clear();
        mquery = query;
        new SearchActorTask().execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //actorListTemp.clear();
        //acTorSearchAdapter.notifyItemRangeRemoved(0, acTorList.size());
        return false;
    }

    private class SearchActorTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String ACTOR_LIST = "results";
                final String ACTOR_NAME = "name";
                final String ACTOR_IMAGES = "profile_path";
                final String ACTOR_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(ACTOR_LIST);

                for (int i = 0; i < listMovie.length(); i++) {
                    JSONObject actorObj = listMovie.getJSONObject(i);
                    Actor actor = new Actor();

                    actor.setmId(actorObj.getString(ACTOR_ID));
                    actor.setmName(actorObj.getString(ACTOR_NAME));
                    actor.setmProfileImage(IMAGE_PROFILE_URL.concat(actorObj.getString(ACTOR_IMAGES)));

                    acTorList.add(actor);
                }
                progressDialog.dismiss();
                acTorSearchAdapter = new ActorSearchAdapter(acTorList, SearchPeopleActivity.this);
                recyclerView.setAdapter(acTorSearchAdapter);

                // acTorSearchAdapter.notifyDataSetChanged();

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
            String adult = "include_adult=false";
            String url = "https://api.themoviedb.org/3/search/person?" + api_key + "&" + language + "&" + query + "&" + adult;
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

    private class LoadmoreTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final String ACTOR_LIST = "results";
                final String ACTOR_NAME = "name";
                final String ACTOR_IMAGES = "profile_path";
                final String ACTOR_ID = "id";
                JSONObject tvShowObject = new JSONObject(s);
                JSONArray listMovie = tvShowObject.getJSONArray(ACTOR_LIST);
                if (listMovie.length() > 0) {
                    for (int i = 0; i < listMovie.length(); i++) {
                        JSONObject actorObj = listMovie.getJSONObject(i);
                        Actor actor = new Actor();
                        actor.setmId(actorObj.getString(ACTOR_ID));
                        actor.setmName(actorObj.getString(ACTOR_NAME));
                        actor.setmProfileImage(IMAGE_PROFILE_URL.concat(actorObj.getString(ACTOR_IMAGES)));
                        acTorList.add(actor);
                    }
                    acTorSearchAdapter.notifyItemRangeInserted(actorListTemp.size() - 1, acTorList.size() - 1);

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
            String url = "https://api.themoviedb.org/3/search/person?" + api_key + "&" + language + "&" + query + "&" + page + "&" + adult;

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
