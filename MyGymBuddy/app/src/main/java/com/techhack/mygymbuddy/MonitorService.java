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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.zebra.mpact.mpactclient.MPactClient;
import com.zebra.mpact.mpactclient.MPactClientConsumer;

public class MonitorService extends Service
        implements MPactClientConsumer
{
    static MPactClient mpactClient = null;
    static RegionNotifier regionNotifier = null;

    @Override
    public IBinder onBind(Intent intent) {
        // Binding is not supported
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mpactClient == null) {
            // Get instance of the MPact Service
            mpactClient = MPactClient.getInstanceForApplication(getApplicationContext());
        }

        // Bind to the MPact Service
        mpactClient.bind(this);

        // This return code will respawn the app if it gets killed
        return START_STICKY;
    }

    @Override
    public void onMPactClientServiceConnect() {
        // Setup parameters and start the MPact Client
        if(regionNotifier == null) {
            // Get instance of the MPact Service
            regionNotifier = RegionNotifier.getInstanceForApplication(getApplicationContext());
        }

        mpactClient.setNotifier(regionNotifier);
        mpactClient.setiBeaconUUID("fe913213-b311-4a42-8c16-47faeac938db");
        try {
            mpactClient.Start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
