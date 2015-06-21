package com.techhack.mygymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class youtubePlayback extends YouTubeFailureRecoveryActivity {

    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        InstructionActivity.setBluetooth(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.playerview_demo);
        Intent intent = this.getIntent();

        id = intent.getExtras().getString("id");
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setYoutubeActivity(this);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(id);
        }
    }

    @Override
    public void onResume() {
        InstructionActivity.setBluetooth(true);
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setYoutubeActivity(this);
        super.onResume();


    }
    protected void onDestroy() {
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setYoutubeActivity(null);
        super.onDestroy();
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}
