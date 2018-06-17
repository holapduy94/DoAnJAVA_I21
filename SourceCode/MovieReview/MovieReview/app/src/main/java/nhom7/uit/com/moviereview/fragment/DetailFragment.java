package nhom7.uit.com.moviereview.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.ActorDetailAdapter;
import nhom7.uit.com.moviereview.adapter.GenreAdapter;
import nhom7.uit.com.moviereview.adapter.ImagesArrayAdapter;
import nhom7.uit.com.moviereview.adapter.MovieListAdapter;
import nhom7.uit.com.moviereview.adapter.VideoArrayAdapter;
import nhom7.uit.com.moviereview.controller.ImagesSliderActivity;
import nhom7.uit.com.moviereview.controller.TrailerMovieActivity;
import nhom7.uit.com.moviereview.model.Actor;
import nhom7.uit.com.moviereview.model.MovieData;
import nhom7.uit.com.moviereview.model.MovieGenre;
import nhom7.uit.com.moviereview.model.MoviePhoto;
import nhom7.uit.com.moviereview.model.MovieVideo;
import nhom7.uit.com.moviereview.objects.GlobalVariable;
import nhom7.uit.com.moviereview.objects.ListImages;
import nhom7.uit.com.moviereview.thread.DownloadJson;
import nhom7.uit.com.moviereview.utils.Constants;

import static nhom7.uit.com.moviereview.utils.Constants.IMAGE_BASE_URL;

/**
 * Created by phuocthang on 10/24/2017.
 */

public class DetailFragment extends Fragment {

    TextView khoichieu_txt, thoiluong_txt, imdb_txt, votes_txt, txt_overview, title_txt;
    ImageView poster_image_view;

    String ID = "346364";

    RecyclerView recyclerview_genres;
    RecyclerView recyclerview_actor;

    GenreAdapter genreAdapter;
    ActorDetailAdapter actorDetailAdapter;

    List<MovieGenre> genreList;
    List<Actor> actorList;


    GridView gridViewImages;
    ImagesArrayAdapter imagesArrayAdapter;
    List<MoviePhoto> moviePhotos;


    ListView listViewVideo;
    VideoArrayAdapter videoArrayAdapter;
    List<MovieVideo> movieVideos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        khoichieu_txt = (TextView) rootView.findViewById(R.id.khoichieu_txt);
        thoiluong_txt = (TextView) rootView.findViewById(R.id.thoiluong_txt);
        imdb_txt = (TextView) rootView.findViewById(R.id.imdb_txt);
        title_txt = (TextView) rootView.findViewById(R.id.title_txt);

        votes_txt = (TextView) rootView.findViewById(R.id.votes_txt);
        txt_overview = (TextView) rootView.findViewById(R.id.txt_overview);
        poster_image_view = (ImageView) rootView.findViewById(R.id.poster_image_view);
        gridViewImages = (GridView) rootView.findViewById(R.id.gridview);
        listViewVideo = (ListView) rootView.findViewById(R.id.listview_trailer);

        // tạo list
        genreList = new ArrayList<>();
        actorList = new ArrayList<>();
        moviePhotos = new ArrayList<>();
        movieVideos = new ArrayList<>();

        // setlayout
        recyclerview_genres = (RecyclerView) rootView.findViewById(R.id.recyclerview_genres);
        recyclerview_genres.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerview_actor = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor);
        recyclerview_actor.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));


        // click gridview
        gridViewImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showImageAt(i);
            }
        });
        // click listview
        listViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playVideoAt(i);
            }
        });


        // scroll gridview
        gridViewImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        // scroll listview
        listViewVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return rootView;
    }

    private void playVideoAt(int i) {
        Intent intent = new Intent(getContext(), TrailerMovieActivity.class);
        MovieVideo info = (MovieVideo) listViewVideo.getItemAtPosition(i);
        intent.putExtra("KEY", info.getKey());
        startActivity(intent);
    }

    private void showImageAt(int i) {
        Intent intent = new Intent(getContext(), ImagesSliderActivity.class);
        intent.putExtra("POSITION", i);
        if (GlobalVariable.listImages == null) {
            GlobalVariable.listImages = new ListImages();
            GlobalVariable.listImages.posters = new ArrayList<>();
            GlobalVariable.listImages.posters.addAll(moviePhotos);
        } else {
            GlobalVariable.listImages.posters.clear();
            GlobalVariable.listImages.posters.addAll(moviePhotos);
        }
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getString("id");

        }
        //  tổng quan , thể loại , chi tiết
        GetMovieByIDTask getMovieByIDTask = new GetMovieByIDTask();
        getMovieByIDTask.execute(ID);

        //  danh sách diễn viên
        GetCastByMovieIDTask getCastByMovieIDTask = new GetCastByMovieIDTask();
        getCastByMovieIDTask.execute(ID);

        //  danh sách hình ảnh
        GetPhotoByMovieIDTask getPhotoByMovieIDTask = new GetPhotoByMovieIDTask();
        getPhotoByMovieIDTask.execute(ID);

        //  danh sách trailer
        GetVideoByMovieIDTask getVideoByMovieIDTask = new GetVideoByMovieIDTask();
        getVideoByMovieIDTask.execute(ID);
    }

    // task
    private class GetVideoByMovieIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String id = strings[0];
            String language = "language=en-US";

            String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?" + api_key + "&" + language;
            //resul=url;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            //resul=data;
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String VIDEO_RESULT = "results";
                final String VIDEO_KEY = "key";
                final String VIDEO_ID = "id";
                final String VIDEO_NAME = "name";
                final String VIDEO_SIZE = "size";


                JSONObject resultObject = new JSONObject(s);
                JSONArray video_arr = resultObject.getJSONArray(VIDEO_RESULT);

                for (int i = 0; i < video_arr.length(); i++) {
                    JSONObject video_obj = video_arr.getJSONObject(i);
                    String video_id = video_obj.getString(VIDEO_ID);
                    String video_key = video_obj.getString(VIDEO_KEY);
                    String video_name = video_obj.getString(VIDEO_NAME);
                    String video_size = video_obj.getString(VIDEO_SIZE);

                    MovieVideo video = new MovieVideo();
                    video.setId(video_id);
                    video.setKey(video_key);
                    video.setName(video_name);
                    video.setSize(video_size);

                    movieVideos.add(video);
                }

                videoArrayAdapter = new VideoArrayAdapter(movieVideos,getContext());
                listViewVideo.setAdapter(videoArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetPhotoByMovieIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String id = strings[0];
            String language = "language=en-US";

            String url = "https://api.themoviedb.org/3/movie/" + id + "/images?" + api_key;
            //resul=url;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            //resul=data;
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String IMG_RESULT = "backdrops";
                final String IMG_PATH = "file_path";

                JSONObject resultObject = new JSONObject(s);
                JSONArray img_arr = resultObject.getJSONArray(IMG_RESULT);

                for (int i = 0; i < img_arr.length(); i++) {
                    JSONObject img_obj = img_arr.getJSONObject(i);
                    String img_path = img_obj.getString(IMG_PATH);
                    MoviePhoto photo = new MoviePhoto();
                    photo.setFile_path(img_path);
                    moviePhotos.add(photo);
                }

                imagesArrayAdapter = new ImagesArrayAdapter(getContext(), moviePhotos);
                gridViewImages.setAdapter(imagesArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetCastByMovieIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            //String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String api_key = "api_key=" + Constants.API_KEY;

            String id = strings[0];
            String url = "https://api.themoviedb.org/3/movie/" + id + "/credits?" + api_key;
            //resul=url;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            //resul=data;
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String CAST = "cast";

                final String CAST_ID = "cast_id";
                final String CAST_ROLE_NAME = "character";
                final String CREADIT_ID = "credit_id";
                final String ID = "id";
                final String CAST_NAME = "name";
                final String CAST_IMG = "profile_path";


                JSONObject resultObject = new JSONObject(s);
                JSONArray cast_arr = resultObject.getJSONArray(CAST);


                for (int i = 0; i < cast_arr.length(); i++) {
                    JSONObject cast_obj = cast_arr.getJSONObject(i);
                    String cast_name = cast_obj.getString(CAST_NAME);
                    String cast_role = cast_obj.getString(CAST_ROLE_NAME);
                    String cast_profile_img = cast_obj.getString(CAST_IMG);

                    String cast_id = cast_obj.getString(CAST_ID);
                    Actor actor = new Actor();
                    actor.setmId(cast_id);
                    actor.setmProfileImage(cast_profile_img);
                    actor.setmRole(cast_role);
                    actor.setmName(cast_name);

                    actorList.add(actor);
                }


                actorDetailAdapter = new ActorDetailAdapter(actorList, getContext(), (AppCompatActivity) getActivity());
                recyclerview_actor.setAdapter(actorDetailAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMovieByIDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            String api_key = "api_key=ccf3d2b939d5fdeb88dba8a53a499c89";
            String language = "language=en-US";
            String id = strings[0];
            String url = "https://api.themoviedb.org/3/movie/" + id + "?" + api_key + "&" + language;
            //resul=url;
            try {
                DownloadJson downLoadJSon = new DownloadJson();
                data = downLoadJSon.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("LoiData", data);

            //resul=data;
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("watch_s", s);
            try {
                final String MOV_RUNTIME = "runtime";
                final String MOV_OVERVIEW = "overview";
                final String MOV_POSTER = "poster_path";
                final String MOV_RELEASE_DATE = "release_date";
                final String MOV_NAME = "title";
                final String MOV_VOTES_COUNT = "vote_count";
                final String MOV_RATING = "vote_average";
                final String MOV_GENRE = "genres";
                final String MOV_GENRE_ID = "id";
                final String MOV_GENRE_NAME = "name";

                final String MOV_HOME_PAGE = "homepage";

                JSONObject tvShowObject = new JSONObject(s);
                JSONArray genre_arr = tvShowObject.getJSONArray(MOV_GENRE);


                String runtime = tvShowObject.getString(MOV_RUNTIME);
                String release_date = tvShowObject.getString(MOV_RELEASE_DATE);
                String title = tvShowObject.getString(MOV_NAME);
                String poster_path = tvShowObject.getString(MOV_POSTER);
                String vote_count = tvShowObject.getString(MOV_VOTES_COUNT);
                String vote_average = tvShowObject.getString(MOV_RATING);

                String overview = tvShowObject.getString(MOV_OVERVIEW);

                Constants.share_info_homepage_mov = tvShowObject.getString(MOV_HOME_PAGE);

                thoiluong_txt.setText(runtime);
                title_txt.setText(title);
                khoichieu_txt.setText(release_date);
                Picasso.with(getContext()).load(Constants.IMAGE_PROFILE_URL + poster_path).into(poster_image_view);
                txt_overview.setText(overview);
                imdb_txt.setText(vote_average);
                votes_txt.setText(vote_count);

                for (int i = 0; i < genre_arr.length(); i++) {
                    JSONObject genre_obj = genre_arr.getJSONObject(i);
                    String genre_name = genre_obj.getString(MOV_GENRE_NAME);
                    String genre_id = genre_obj.getString(MOV_GENRE_ID);
                    MovieGenre movieGenre = new MovieGenre();
                    movieGenre.setId(genre_id);
                    movieGenre.setName(genre_name);
                    genreList.add(movieGenre);
                }


                genreAdapter = new GenreAdapter(genreList, getContext(), (AppCompatActivity) getActivity());
                recyclerview_genres.setAdapter(genreAdapter);


                // resul=runtime;
                // Log.d("watch_runtime",runtime);

                //r = runtime;
                // progressDialog.dismiss();
                // acTorSearchAdapter = new ActorSearchAdapter(acTorList, SearchPeopleActivity.this);
                //  recyclerView.setAdapter(acTorSearchAdapter);

                // acTorSearchAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
