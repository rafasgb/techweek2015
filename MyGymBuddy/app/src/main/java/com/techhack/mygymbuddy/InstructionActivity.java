package com.techhack.mygymbuddy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

import java.util.HashMap;

/**
 * Created by Admin on 6/20/2015.
 */
public class InstructionActivity extends LoginBaseActivity{

    private final static String TAG = "Instruction Activity";

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
        intent.putExtra("id", idTovideo.get(id));
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_instruction_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "settings clicked");
                return true;
            case R.id.action_logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(){
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
        }

}
