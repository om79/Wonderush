package com.programize.wonderush.Activities;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.programize.wonderush.Activities.Booking.Tickets;
import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.Activities.Browsing.Listings;
import com.programize.wonderush.Activities.Profile.Profile1;
import com.programize.wonderush.Activities.Settings.Settings1;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import io.fabric.sdk.android.Fabric;


public class InitializeActivity extends Application implements Application.ActivityLifecycleCallbacks {

//    private SharedPreferences prefs;
//    private myActions mActions ;
//    private String TAG = "TAG - APPLICATION - " ;

    public static MixpanelAPI mMixpanel ;
    public static MixpanelAPI.People people ;

    Activity current_activity ;
    @Override
    public void onCreate() {
        super.onCreate();

        //TODO COMMENT ON DEVELOPMENT
        Fabric.with(this, new Crashlytics());

        mMixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_id));
        people = mMixpanel.getPeople();

        Definitions.initializeApplication(getApplicationContext());
        registerActivityLifecycleCallbacks(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

//        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE) ;

    }



    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {
//        Log.e(TAG, "onActivityCreated");

    }
    @Override
    public void onActivityDestroyed(Activity activity) {
//        Log.e(TAG,"onActivityDestroyed ");

        mMixpanel.flush();

    }
    @Override
    public void onActivityPaused(Activity activity) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("active", false);
//        editor.apply();
//        Log.e(TAG,"onActivityPaused "+activity.getClass());

    }
    @Override
    public void onActivityResumed(Activity activity) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("active", true);
//        editor.apply();

        current_activity = activity ;
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        Log.e(TAG,"onActivitySaveInstanceState");

    }
    @Override
    public void onActivityStarted(Activity activity) {
//        Log.e(TAG,"onActivityStarted");

    }
    @Override
    public void onActivityStopped(Activity activity) {
//        Log.e(TAG, "onActivityStopped");
    }

}
