package com.programize.wonderush.Utilities.Definitions;

import android.content.Context;

import com.programize.wonderush.Utilities.volley.VolleyWebLayer;

public class Definitions {

    // ====================================== Definitions ====================================== //
    public static final String sharedprefname = "wonderush_pref" ;
    public static final String sharedprefname_app = "wonderush_app_pref" ;
//    public static final String APIdomain = "http://wonderush.programize.com"; //development
    public static final String APIdomain = "http://wonderush.poplify.com";  //staging
//    public static final String APIdomain = "https://wonderush.com";  //production

    public static final String stripe_id = "pk_test_uRrdcQ4022v5HyFQviqme1tS"; //development //staging
//    public static final String stripe_id = "pk_live_AzmRIbaEZCfWiL0bgmzN2ImY"; //production


    // ============================================ Others ============================================ //
    public static Context appContext;


    // ============================================ Initializer ============================================ //
    public static void initializeApplication(Context _appContext){
        appContext = _appContext;
        VolleyWebLayer.initialize();
    }
}
