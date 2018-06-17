package nhom7.uit.com.moviereview.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.utils.YoutubeConfig;

public class TrailerMovieActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YoutubeConfig youtubeConfig = new YoutubeConfig();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_movie);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerPreview);
        youTubePlayerView.initialize(youtubeConfig.getApiKey(), this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(getIntent().getExtras().getString("KEY"));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            String error = String.format("Error initializing YouTube player", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
