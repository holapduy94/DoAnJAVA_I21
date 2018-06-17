package nhom7.uit.com.moviereview.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorSearchAdapter;
import nhom7.uit.com.moviereview.adapter.DetaiViewPagerAdapter;
import nhom7.uit.com.moviereview.fragment.*;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_BASE_URL;

public class DetailsMovieActivity extends AppCompatActivity {

    TabLayout mTablayout;
    ViewPager mViewPager;
    Toolbar detail_toolbar;
    NestedScrollView nested_scrollview;
    ImageView detail_backdrop;


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        addControl();

    }

    private void addControl() {
        detail_toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detail_backdrop = (ImageView) findViewById(R.id.detail_backdrop);

        Bundle bundle = getIntent().getBundleExtra("showing_movie_bundle");
        String mov_banner_path= bundle.getString("backdrop_path");

        Picasso.with(this).load(Constants.IMAGE_BASE_URL+mov_banner_path).fit().error(R.drawable.image_failed).into(detail_backdrop);

        nested_scrollview = (NestedScrollView) findViewById(R.id.nested_scrollview);
        //nested_scrollview.setFillViewport(true);

        mTablayout = (TabLayout) findViewById(R.id.tab_detail_movie);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_detail);
        setupViewPager(bundle);
        mTablayout.setupWithViewPager(mViewPager);




    }

    private void setupViewPager(Bundle bundle) {
        DetaiViewPagerAdapter adapter = new DetaiViewPagerAdapter(getSupportFragmentManager());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        adapter.addFragment(detailFragment, "Chi Tiết");
        ReviewDetailsFragment reviewFragment = new ReviewDetailsFragment();
        reviewFragment.setArguments(bundle);
        adapter.addFragment(reviewFragment, "Bình Luận");
        mViewPager.setAdapter(adapter);
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
                    .setContentUrl(Uri.parse(Constants.share_info_homepage_mov))
                    .build();
            shareDialog.show(linkContent);
        }

    }





}
