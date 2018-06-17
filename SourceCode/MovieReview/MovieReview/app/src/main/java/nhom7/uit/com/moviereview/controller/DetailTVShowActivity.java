package nhom7.uit.com.moviereview.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorDetailAdapter;
import nhom7.uit.com.moviereview.adapter.DetaiViewPagerAdapter;
import nhom7.uit.com.moviereview.adapter.VideoArrayAdapter;
import nhom7.uit.com.moviereview.fragment.DetailTvShowsFragment;
import nhom7.uit.com.moviereview.fragment.ReviewDetailsFragment;
import nhom7.uit.com.moviereview.fragment.TestFragment;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.MovieVideo;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

public class DetailTVShowActivity extends AppCompatActivity {

    TabLayout tabs_tvshows_detail;
    ViewPager view_pager_detail_tvshows;
    Toolbar tvshow_toolbar;
    ImageButton tv_show_play_trailer;
    MovieVideo video;
    String ID = "66393";
    ImageView tvshow_backdrop;


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);
        addControl();
        addEvent();
    }

    private void addEvent() {



        tv_show_play_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideoAt();
            }
        });
    }

    private void playVideoAt() {
        Intent intent = new Intent(DetailTVShowActivity.this, TrailerMovieActivity.class);
        intent.putExtra("KEY", video.getKey());
        startActivity(intent);
    }

    private void addControl() {
        tvshow_toolbar = (Toolbar) findViewById(R.id.tvshow_toolbar);
        setSupportActionBar(tvshow_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvshow_backdrop = (ImageView) findViewById(R.id.tvshow_backdrop);

        tv_show_play_trailer = (ImageButton) findViewById(R.id.tv_show_play_trailer);

        video = new MovieVideo();



        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("tvshow_bundle");
        ID = bundle.getString("id");
        Log.d("xem_id",ID);
        String mov_banner_path = bundle.getString("backdrop_path");
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + mov_banner_path).fit().error(R.drawable.image_failed).into(tvshow_backdrop);


        new GetVideoByTVShowIDTask().execute(ID);

        tabs_tvshows_detail = (TabLayout) findViewById(R.id.tabs_tvshows_detail);
        view_pager_detail_tvshows = (ViewPager) findViewById(R.id.view_pager_detail_tvshows);
        setupViewPager(bundle);
        tabs_tvshows_detail.setupWithViewPager(view_pager_detail_tvshows);
    }

    private void setupViewPager(Bundle bundle) {
        DetaiViewPagerAdapter adapter = new DetaiViewPagerAdapter(getSupportFragmentManager());

        DetailTvShowsFragment detailTvShowsFragment = new DetailTvShowsFragment();
        detailTvShowsFragment.setArguments(bundle);
        adapter.addFragment(detailTvShowsFragment, "Chi Tiết");
        ReviewDetailsFragment reviewFragment = new ReviewDetailsFragment();
        reviewFragment.setArguments(bundle);
        adapter.addFragment(reviewFragment, "Bình Luận");

        view_pager_detail_tvshows.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
            finish();
        if (item.getItemId() == R.id.share_detail)
        {
            if (isLoggedIn() == false) { //  chưa đăng nhập
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            } else {
                shareYourContentOnFacebook();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    private void shareYourContentOnFacebook() {

        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(this.getClass().getSimpleName(), "shared successfully");
                finish();
            }

            @Override
            public void onCancel() {
                Log.d(this.getClass().getSimpleName(), "sharing cancelled");
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(this.getClass().getSimpleName(), "sharing error");
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(Constants.share_info_homepage_tv))
                    .build();
            shareDialog.show(linkContent);
        }

    }


    private class GetVideoByTVShowIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=" + Constants.API_KEY;
            String id = strings[0];
            String language = "language=en-US";
            String url = "https://api.themoviedb.org/3/tv/" + id + "/videos?" + api_key + "&" + language;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                final String VIDEO_RESULT = "results";
                final String VIDEO_KEY = "key";
                final String VIDEO_ID = "id";
                final String VIDEO_NAME = "name";
                final String VIDEO_SIZE = "size";


                JSONObject resultObject = new JSONObject(s);
                JSONArray video_arr = resultObject.getJSONArray(VIDEO_RESULT);

                JSONObject video_obj = video_arr.getJSONObject(0);
                String video_id = video_obj.getString(VIDEO_ID);
                String video_key = video_obj.getString(VIDEO_KEY);
                String video_name = video_obj.getString(VIDEO_NAME);
                String video_size = video_obj.getString(VIDEO_SIZE);

                MovieVideo mvideo = new MovieVideo();
                mvideo.setId(video_id);
                mvideo.setKey(video_key);
                mvideo.setName(video_name);
                mvideo.setSize(video_size);

                video = mvideo;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
