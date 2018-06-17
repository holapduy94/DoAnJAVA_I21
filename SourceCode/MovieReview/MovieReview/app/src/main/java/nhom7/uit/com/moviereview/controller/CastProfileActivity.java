package nhom7.uit.com.moviereview.controller;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.DetaiViewPagerAdapter;
import nhom7.uit.com.moviereview.fragment.CastInfoFragment;
import nhom7.uit.com.moviereview.fragment.CastMovieFragment;
import nhom7.uit.com.moviereview.fragment.DetailFragment;
import nhom7.uit.com.moviereview.fragment.TestFragment;

public class CastProfileActivity extends AppCompatActivity {

    TabLayout tabs_cast_profile;
    ViewPager vg_cast_profile;
    Toolbar toolbar_cast_profile;

    String ID ="235731";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_profile);
        addControl();
        addEvents();
    }

    private void addEvents() {

    }

    private void addControl() {
        Bundle bundle = getIntent().getBundleExtra("actor_bundle");
        ID = bundle.getString("id");
        String name = bundle.getString("name");

        toolbar_cast_profile = (Toolbar) findViewById(R.id.toolbar_cast_profile);
        setSupportActionBar(toolbar_cast_profile);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabs_cast_profile = (TabLayout) findViewById(R.id.tabs_cast_profile);
        vg_cast_profile = (ViewPager) findViewById(R.id.vg_cast_profile);
        setupViewPager(bundle);
        tabs_cast_profile.setupWithViewPager(vg_cast_profile);


    }

    private void setupViewPager(Bundle bundle) {
        DetaiViewPagerAdapter adapter = new DetaiViewPagerAdapter(getSupportFragmentManager());

        CastInfoFragment castInfoFragment = new CastInfoFragment();
        castInfoFragment.setArguments(bundle);
        adapter.addFragment(castInfoFragment, "Thông Tin");

        CastMovieFragment castMovieFragment = new CastMovieFragment();
        castMovieFragment.setArguments(bundle);
        adapter.addFragment(castMovieFragment, "Phim");

        vg_cast_profile.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
            finish();
        return super.onOptionsItemSelected(item);
    }
}
