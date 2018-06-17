package nhom7.uit.com.moviereview.controller;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nhom7.uit.com.moviereview.Helper.GoogleApiHelper;
import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.MainViewPagerAdapter;
import nhom7.uit.com.moviereview.fragment.ComingMovieFragment;
import nhom7.uit.com.moviereview.fragment.ShowingMovieFragment;
import nhom7.uit.com.moviereview.fragment.TvShowFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout mTablayout;
    ViewPager mMainViewPager;
    private FirebaseUser mUser;
    private GoogleApiHelper mGoogleApiHelper;
    private Menu mMenu;
    private CircularImageView mAvatarNav;
    private TextView mNameNav;
    private TextView mEmailNav;
    private NavigationView mNavigationView;
    private Toolbar toolbar;

    com.github.clans.fab.FloatingActionButton f_btn_search_film, f_btn_search_tvshow, f_btn_search_actor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mGoogleApiHelper = GoogleApiHelper.getInstance(this);
        addViews();
        addEvents();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                MenuItem item = mMenu.getItem(0);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    item.setTitle("Đăng nhập");
                    item.setIcon(R.drawable.login_icon);
                    mAvatarNav.setImageResource(R.drawable.user_icon_2);
                    mNameNav.setText("user");
                    mEmailNav.setText("user@mail.com");
                }else {
                    item.setTitle("Đăng xuất");
                    item.setIcon(R.drawable.logout_icon);
                    Picasso.with(MainActivity.this).load(user.getPhotoUrl().toString()).into(mAvatarNav);
                    mNameNav.setText(user.getDisplayName());
                    mEmailNav.setText(user.getEmail());
                }
            }
        });
    }

    private void addEvents() {
        f_btn_search_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_film_activity = new Intent(MainActivity.this, SearchMovieActivity.class);
                startActivity(search_film_activity);
            }
        });
        f_btn_search_tvshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_tvshow_activity = new Intent(MainActivity.this, SearchTvShowActivity.class);
                startActivity(search_tvshow_activity);
            }
        });
        f_btn_search_actor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_actor_activity = new Intent(MainActivity.this, SearchPeopleActivity.class);
                startActivity(search_actor_activity);
            }
        });
    }

    private void addViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mMainViewPager = (ViewPager) findViewById(R.id.view_pager_home);
        setupViewPager();
        mTablayout.setupWithViewPager(mMainViewPager);

        f_btn_search_film = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.f_btn_search_film);
        f_btn_search_tvshow = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.f_btn_search_tvshow);
        f_btn_search_actor = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.f_btn_search_actor);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        LinearLayout linearLayout = (LinearLayout) mNavigationView.getHeaderView(0);
        mAvatarNav = (CircularImageView) linearLayout.findViewById(R.id.avatar_nav);
        mNameNav = (TextView) linearLayout.findViewById(R.id.name_nav);
        mEmailNav = (TextView) linearLayout.findViewById(R.id.email_nav);


        setUpNavigationDrawer();
    }

    private void setupViewPager() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShowingMovieFragment(), "Đang Chiếu");
        adapter.addFragment(new ComingMovieFragment(), "Sắp Chiếu");
        adapter.addFragment(new TvShowFragment(), "TvShow");

        mMainViewPager.setAdapter(adapter);
    }

    public void setUpNavigationDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mMenu = mNavigationView.getMenu();
        MenuItem item = mMenu.getItem(0);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            item.setTitle("Đăng nhập");
            item.setIcon(R.drawable.login_icon);
        }else {
            item.setTitle("Đăng xuất");
            item.setIcon(R.drawable.logout_icon);
        }
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            if(mUser == null){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                FirebaseAuth.getInstance().signOut();
                mGoogleApiHelper.signOut();
                LoginManager.getInstance().logOut();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
