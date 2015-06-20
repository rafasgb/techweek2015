package com.motorolasolutions.mpact.mpactclientsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.motorolasolutions.mpact.mpactclient.MPactBeaconType;
import com.motorolasolutions.mpact.mpactclient.MPactClient;
import com.motorolasolutions.mpact.mpactclient.MPactClientConsumer;
import com.motorolasolutions.mpact.mpactclient.MPactClientNotifier;
import com.motorolasolutions.mpact.mpactclient.MPactServerInfo;
import com.motorolasolutions.mpact.mpactclient.MPactTag;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements MPactClientConsumer, MPactClientNotifier,
        Switch.OnCheckedChangeListener
{
    public static final String TAG = "MainActivity";

    MPactClient mpactClient;
    Map<String,TableRow> rowMap = new HashMap<String,TableRow>();
    private static CookieManager cookieManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MPactClient.LOG_DEBUG = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mpactClient = MPactClient.getInstanceForApplication(this.getApplicationContext());
        mpactClient.bind(this);

        if(savedInstanceState == null) {
            // Populate fields with defaults
            setLabelText(R.id.textViewSDKVersion, mpactClient.getVersion());
            MPactServerInfo mpactServerInfo = new MPactServerInfo();
            setLabelText(R.id.textViewClientID, mpactClient.getClientID());
            setEditText(R.id.editTextClientName, mpactClient.getClientName());
            setEditText(R.id.editTextServer, mpactServerInfo.getHost());
            setEditText(R.id.editTextServerPort, String.valueOf(mpactServerInfo.getPort()));
            setEditText(R.id.editTextLoginID, mpactServerInfo.getLoginID());
            setEditText(R.id.editTextPassword, mpactServerInfo.getPassword());
            setEditText(R.id.editTextUUID, mpactClient.getiBeaconUUID());
            Switch switchTemp = (Switch)findViewById(R.id.switchHTTPS);
            switchTemp.setChecked(mpactServerInfo.getUseHTTPS());
            switchTemp = (Switch)findViewById(R.id.switchAuthenticate);
            switchTemp.setChecked(mpactServerInfo.getAuthenticate());
        }

        Switch scanSwitch = (Switch)findViewById(R.id.switchScan);
        scanSwitch.setOnCheckedChangeListener(this);

        // Setup cookies
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    @Override
    public void onMPactClientServiceConnect() {
        mpactClient.setNotifier(this);

        // See if we need to start the scan
        Switch scanSwitch = (Switch)findViewById(R.id.switchScan);
        if(scanSwitch.isChecked())
        {
            // scan switch was enabled before the MPact Service was ready.
            // Start scanning now that the service is ready.
            onCheckedChanged(scanSwitch, scanSwitch.isChecked());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mpactClient.unBind(this);
    }

    @Override
    public void didDetermineClosestTag(MPactTag mpactTag) {
        setLabelText(R.id.textViewClosestTag, mpactTag.getTagID());
        setLabelText(R.id.textViewRssi, String.valueOf(mpactTag.getRssi()));
        setLabelText(R.id.textViewBatteryLevel, String.valueOf(mpactTag.getBatteryLife()));
    }

    @Override
    public void didDetermineState(int state) {
        String displayString = "Unknown Region";
        switch (state) {
            case INSIDE:
                displayString = "Inside Region";
                break;
            case OUTSIDE:
                displayString = "Outside Region";
                break;
            default:
                break;
        }

        setLabelText(R.id.textViewRegionState, displayString);
    }

    private void setLabelText(final int textViewId, final String displayString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView) findViewById(textViewId);
                view.setText(displayString);
            }
        });

    }

    private void setEditText( final int editViewId, final String displayString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText view = (EditText) findViewById(editViewId);
                view.setText(displayString);
            }
        });

    }

    private String getEditText( final int editViewId) {
        EditText view = (EditText) findViewById(editViewId);
        return view.getText().toString();
    }

    private void clearOutputFields() {
        setLabelText(R.id.textViewClosestTag, "?");
        setLabelText(R.id.textViewRssi, "?");
        setLabelText(R.id.textViewBatteryLevel, "?");
        setLabelText(R.id.textViewRegionState, "Unknown");
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            // Clear output values
            clearOutputFields();

            // Setup the MPact Client parameters
            MPactServerInfo mpactServerInfo = new MPactServerInfo();
            mpactClient.setClientName(getEditText(R.id.editTextClientName));
            mpactServerInfo.setHost(getEditText(R.id.editTextServer));
            mpactServerInfo.setPort(Integer.parseInt(getEditText(R.id.editTextServerPort)));
            mpactServerInfo.setLoginID(getEditText(R.id.editTextLoginID));
            mpactServerInfo.setPassword(getEditText(R.id.editTextPassword));
            Switch switchTemp = (Switch)findViewById(R.id.switchHTTPS);
            mpactServerInfo.setUseHTTPS(switchTemp.isChecked());
            switchTemp = (Switch)findViewById(R.id.switchAuthenticate);
            mpactServerInfo.setAuthenticate(switchTemp.isChecked());
            mpactClient.setServer(mpactServerInfo);
            mpactClient.setiBeaconUUID(getEditText(R.id.editTextUUID));

            RadioGroup beaconTypeGroup = (RadioGroup) findViewById(R.id.groupBeaconType);
            int selectedBeaconType = beaconTypeGroup.getCheckedRadioButtonId();
            if(selectedBeaconType == R.id.radioButtonIBeacon) {
                mpactClient.setBeaconType(MPactBeaconType.iBeacon);
            } else if(selectedBeaconType == R.id.radioButtonMPactBeacon) {
                mpactClient.setBeaconType(MPactBeaconType.MPact);
            } else {
                mpactClient.setBeaconType(MPactBeaconType.BatterySave);
            }

            // The client ID will change if the client name changes
            setLabelText(R.id.textViewClientID, mpactClient.getClientID());

            // Start Scanning
            try {
                mpactClient.Start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            // Stop Scanning
            try {
                mpactClient.Stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBeaconTypeClick(View view) {
        if(view.getId() == R.id.radioButtonIBeacon) {
            mpactClient.setBeaconType(MPactBeaconType.iBeacon);
        } else if(view.getId() == R.id.radioButtonIBeacon) {
            mpactClient.setBeaconType(MPactBeaconType.MPact);
        } else {
            mpactClient.setBeaconType(MPactBeaconType.BatterySave);
        }
    }
}
