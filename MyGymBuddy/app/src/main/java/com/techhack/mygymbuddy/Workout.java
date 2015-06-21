package com.techhack.mygymbuddy;

/**
 * Created by Rafael Zulli on 6/21/2015.
 */
public class Workout {
    private String beaconId;
    private String  youtubeId;
    private String  reducedTitle;
    private  String title;
    private  String descriptiom;

    public Workout(String beaconId,String youtubeID, String reducedtitle, String title, String description)
    {
        this.beaconId = beaconId;
        this.youtubeId = youtubeID;
        this.reducedTitle = reducedtitle;
        this.title = title;
        this.descriptiom = description;

    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getReducedTitle() {
        return reducedTitle;
    }

    public void setReducedTitle(String reducedTitle) {
        this.reducedTitle = reducedTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptiom() {
        return descriptiom;
    }

    public void setDescriptiom(String descriptiom) {
        this.descriptiom = descriptiom;
    }
}
