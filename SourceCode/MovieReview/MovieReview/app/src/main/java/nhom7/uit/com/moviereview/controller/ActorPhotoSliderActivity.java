package nhom7.uit.com.moviereview.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.objects.GlobalVariable;
import nhom7.uit.com.moviereview.objects.ListImages;
import nhom7.uit.com.moviereview.objects.ListPhotoActor;
import nhom7.uit.com.moviereview.utils.Constants;

public class ActorPhotoSliderActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_photo_slider);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), GlobalVariable.listPhotoActor.actorPhotoList.size());

        mViewPager = (ViewPager) findViewById(R.id.vg_actor_photo_slider);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("POSITION", 0), true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ListPhotoActor listPhotoActor;
        private ProgressBar progressBar;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            listPhotoActor = GlobalVariable.listPhotoActor;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_images_slider, container, false);

            progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);

            ImageView section_view = (ImageView) rootView.findViewById(R.id.section_view);
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            Picasso.with(getContext())
                    .load(Constants.IMAGE_PROFILE_URL + listPhotoActor.actorPhotoList.get(position).getFile_path())
                    .fit()
                    .centerCrop()
                    .into(section_view, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int size;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.size = size;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);

        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
