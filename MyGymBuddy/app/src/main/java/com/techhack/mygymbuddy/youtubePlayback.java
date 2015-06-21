package com.techhack.mygymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class youtubePlayback extends YouTubeFailureRecoveryActivity {

    TextView workoutInstructionsTV, workoutInstructionTitleTV;
    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.playerview_demo);
        Intent intent = this.getIntent();

        id = intent.getExtras().getString("id");
        workoutInstructionsTV = (TextView) findViewById(R.id.instructions_textView);
        workoutInstructionTitleTV = (TextView) findViewById(R.id.video_screen_title);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {


            //BenchPress
        if (id.equals("wOnP_oAXUMA")) {
            workoutInstructionTitleTV.setText(R.string.bench_text);
            workoutInstructionsTV.setText(R.string.bench_instruction);

        }

        //Squat press
            else if (id.equals("dP4SjMwFIRk")){
            workoutInstructionTitleTV.setText(R.string.squat_text);
            workoutInstructionsTV.setText(R.string.squat_instructions);
        }
            //use this to play video automatically
            player.loadVideo(id);


            //player.cueVideo(id);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}
