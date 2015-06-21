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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.zebra.mpact.mpactclient.MPactBeaconType;
import com.zebra.mpact.mpactclient.MPactClientNotifier;
import com.zebra.mpact.mpactclient.MPactProximity;
import com.zebra.mpact.mpactclient.MPactTag;

public class RegionNotifier implements MPactClientNotifier {
    private static RegionNotifier client = null;
    private Context context;
    private int regionState = MPactClientNotifier.OUTSIDE;
    private int previousState = MPactClientNotifier.OUTSIDE;

    public boolean notf = true;
    private String previousID ;
    private InstructionActivity mainActivity = null;
    private YouTubeBaseActivity youActivity = null;
    public static RegionNotifier getInstanceForApplication(Context context) {
        if (client == null) {
            client = new RegionNotifier(context.getApplicationContext());
        }
        return client;
    }

    protected RegionNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void didDetermineClosestTag(MPactTag mpactTag) {
        String id = mpactTag.getTagID();
        if(regionState == MPactClientNotifier.INSIDE) {

            if (id.equals("0x236d6f")) {
                id = "squats";
            } else if (id.equals("0x236b0c"))
            {
                id = "bench";
            }
            if(!id.equals(previousID))
            {
                previousID = id;
                if(youActivity != null)
                {
                    youActivity.finish();
                }
                notify("If you want some tips on " + id + " workout, click me");

            }
            previousState = regionState;

        }

        updateID(mpactTag.getTagID());
    }

    @Override
    public void didDetermineState(int state) {
        previousState = regionState;
        regionState = state;
        updateActivityRegionState(state);
    }

    @Override
    public void didDetermineState(int state, Integer major, Integer minor) {

    }

    @Override
    public void didChangeIBeaconUUID(String uuid) {

    }

    @Override
    public void didChangeBeaconType(MPactBeaconType beaconType) {

    }

    @Override
    public void didChangeProximityRange(MPactProximity proximityRange) {

    }

    // This method only uses one message id which means there will be at most one notification.
    // Multiple calls to this method will only display the most current message.
    private void notify(String msg) {
        // Generate a notification
        if(!notf) return;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Gym Buddy")
                .setContentText(msg);
        Intent resultIntent = new Intent(context, InstructionActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(InstructionActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

        // Turn on the screen if the device is sleeping
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Gym Buddy");
        wl.acquire(3000);
        wl.release();
    }
    private void updateID(String id) {
        if(mainActivity == null) {
            return;
        }


        mainActivity.showVideo(id);


    }
    private void updateActivityRegionState(int state) {
        if(youActivity == null) {
            return;
        }
        if(state == MPactClientNotifier.OUTSIDE) {
            youActivity.finish();
            previousID = "";


        }

    }
    public void setYoutubeActivity(YouTubeBaseActivity youActivity) {
        this.youActivity = youActivity;
        updateActivityRegionState(regionState);
    }
    public void setMainActivity(InstructionActivity mainActivity) {
        this.mainActivity = mainActivity;
        updateActivityRegionState(regionState);
    }

}
