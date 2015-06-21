package com.techhack.mygymbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

/**
 * Created by Admin on 6/20/2015.
 */
public class InstructionActivity extends Activity {

    Button button;

    private HashMap<String, String> idTovideo = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idTovideo.put("0x236b0c", "wKJ9KzGQq0w");
        idTovideo.put("0x236d6f","dQw4w9WgXcQ");
        // Start up the background beacon monitoring service
        Intent myIntent = new Intent(this, MonitorService.class);
        startService(myIntent);
        setContentView(R.layout.instructions);
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(this);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructionActivity.this, youtubePlayback.class);
                startActivity(intent);
            }
        });


    }

    public void showVideo(String id)
    {
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(null);
        Intent intent = new Intent(InstructionActivity.this, youtubePlayback.class);
        intent.putExtra("id",idTovideo.get(id));
        startActivity(intent);

    }
}
