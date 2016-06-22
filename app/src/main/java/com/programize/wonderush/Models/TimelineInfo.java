package com.programize.wonderush.Models;

public class TimelineInfo {
    private String id ;
    private String mURL ;
    private String mName ;
    private String mDate ;
    private String mType ;

    public TimelineInfo(String id, String type, String mURL, String mName, String mDate) {
        this.id = id ;
        this.mType = type ;

        this.mURL = mURL;
        this.mName = mName;
        this.mDate = mDate;
    }

    public String getID()
    {
        return id;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mType) {
        this.mName = mType;
    }

    public String getDate() {
        return mDate.substring(0,10);
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
