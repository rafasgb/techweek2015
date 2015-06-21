//============================================================================
//
//   Zebra Technologies  - MPACT
//
//  Copyright (c) 2015  Zebra Technologies . All Rights Reserved.
//
//  All information contained herein is the property of Zebra Technologies.  All software
//  within this document, of the software, is distributed on an "AS IS" BASIS, WITHOUT
//  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//
//============================================================================
package com.techhack.mygymbuddy;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zebra.mpact.mpactclient.MPactClient;

import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       setLabelText(R.id.textViewSDKVersion, MPactClient.getVersion());

        //RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(this);
    }

    @Override
    protected void onDestroy() {
        //RegionNotifier.getInstanceForApplication(getApplicationContext()).setMainActivity(null);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==  R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * This section holds GUI specific code.
     */

    public void setLabelText(final int textViewId, final String displayString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView) findViewById(textViewId);
                view.setText(displayString);
            }
        });

    }

}
