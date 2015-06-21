package com.techhack.mygymbuddy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.HashMap;

/**
 * Created by Admin on 6/20/2015.
 */
public class InstructionActivity extends Activity {

    Button button;

    private static HashMap<String, String> idTovideo = new HashMap<String,String>();

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setBluetooth(true);
        super.onCreate(savedInstanceState);

        idTovideo.put("0x236b0c", "wOnP_oAXUMA");//bench press
        idTovideo.put("0x236d6f","dP4SjMwFIRk"); //squat
        // Start up the background beacon monitoring service
        Intent myIntent = new Intent(this, MonitorService.class);
        startService(myIntent);
        setContentView(R.layout.instructions);
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(this);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.notButton);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RegionNotifier.getInstanceForApplication(getApplicationContext()).notf = true;
            }
        });


    }

    @Override
    public void onResume() {
        this.setBluetooth(true);
        super.onResume();
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(this);

    }


    protected void onDestroy() {

        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(null);
        super.onDestroy();
    }

    public void showVideo(String id)
    {
        RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(null);
        Intent intent = new Intent(InstructionActivity.this, youtubePlayback.class);
        intent.putExtra("id",idTovideo.get(id));
        startActivity(intent);

    }
}
