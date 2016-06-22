package com.programize.wonderush.Activities.Browsing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.InitializeActivity;
import com.programize.wonderush.Activities.SplashNew;
import com.programize.wonderush.Adapters.GridCategoriesAdapter;
import com.programize.wonderush.Fragments.CuratedFragment;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.UI.OnSwipeTouchListener;
import com.programize.wonderush.Utilities.volley.UserAPI;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Browse extends FragmentActivity {

    private String TAG = "TAG - BROWSE SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView categories ;
    private ImageView img_arrow_down ;
    private ImageView img_arrow_back ;
    private ViewPager viewPager_curated ;
    private SlidingUpPanelLayout sliding_panel ;
    private GridView gridView ;

    private String avatar ;
    private int curated_size = 0 ;

    private Handler handler ;
    private Timer swipeTimer ;

    private LinearLayout viewpager_indicator;
    private ImageView[] dots ;
    private int dotsCount ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        //initialize progress bar
        progressDialog = MyProgressDialog.ctor(Browse.this);


        //deep linking handling
        //open experience screen
        if (getIntent().hasExtra("experience_from_URI"))
        {
            Log.v(TAG + " experience_from_URI: ", getIntent().getExtras().getString("experience_from_URI"));

            Intent intent = new Intent(this, Experience.class);
            intent.putExtra("experience_id",getIntent().getExtras().getString("experience_from_URI"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_listeners();

        //Initialize Mix Panel for push notifications
        InitializeActivity.people.identify(prefs.getInt("id", 0) + "");
        InitializeActivity.mMixpanel.identify(prefs.getInt("id", 0) + "");
        InitializeActivity.people.set("$email", prefs.getString("email", ""));
        InitializeActivity.people.set("$name", prefs.getString("firstname", ""));
        InitializeActivity.people.set("user_id", prefs.getInt("id", 0));

//        InitializeActivity.people.initPushHandling("98832535808");
        InitializeActivity.people.initPushHandling(getResources().getString(R.string.google_app_id));

        //if avatar exists, then it is stored locally
        if(prefs.contains("avatar"))
        {
            avatar = prefs.getString("avatar", null);
        }
        else
        {
            avatar = "no_avatar" ;
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Browse.this, Listings.class);
                intent.putExtra("experience_category", parent.getItemAtPosition(position) + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        viewPager_curated.setOnTouchListener(new OnSwipeTouchListener() {
            public void onTouch() {
                swipeTimer.cancel();

            }});

        //code to get keystore hash for Facebook console
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.programize.wonderush",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

//        getCuratedList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        swipeTimer.cancel();

        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCuratedList();
        if(!prefs.contains("token"))
        {
            Intent intent = new Intent(getApplicationContext(), SplashNew.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            if(mMenu!=null) {
                mMenu.myTerminateFunc(true);
            }

            mMenu = mActions.set_up_menu(this);


            get_unreviewed_events();
            get_settings();
        }

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

    }

    private void initialize_widgets()
    {
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        categories = (TextView)findViewById(R.id.browsescreen_textview_categories);
        img_arrow_down = (ImageView)findViewById(R.id.browsescreen_imageview_arrowdown);
        img_arrow_back = (ImageView)findViewById(R.id.browsescreen_imageview_backarrow);
        viewPager_curated = (ViewPager)findViewById(R.id.browsescreen_viewpager);

        viewpager_indicator = (LinearLayout)findViewById(R.id.browsescreen_viewpager_indicator);

        sliding_panel = (SlidingUpPanelLayout)findViewById(R.id.browsescreen_sliding_layout);
        gridView = (GridView)findViewById(R.id.browsescreen_gridview);

        handler = new Handler();
        swipeTimer = new Timer();
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        categories.setTypeface(tf_Bold);
    }

    private void set_up_images() {
        //SETTING UP IMAGES
        img_arrow_down.setImageResource(R.drawable.icon_arrow_down);
    }

    private void set_up_listeners()
    {
        sliding_panel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {
                img_arrow_down.setVisibility(View.VISIBLE);
                img_arrow_back.setVisibility(View.INVISIBLE);

                categories.setText("CATEGORIES");
            }

            @Override
            public void onPanelExpanded(View panel) {
                img_arrow_down.setVisibility(View.INVISIBLE);
                img_arrow_back.setVisibility(View.VISIBLE);
                categories.setText("Categories");
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("EXIT")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void getCuratedList()
    {
        if(!mActions.checkInternetConnection(Browse.this))
        {
            mActions.displayToast(Browse.this, "No Internet Connection");
        }
        else if(!prefs.contains("token"))
        {

        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_JsonArrayResp("curated_lists", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
//                            progressDialog.dismiss();
                            set_up_curated(response);
                            getCategories();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mActions.displayToast(Browse.this, "Something went wrong");
                        }
                    });
        }
    }

    private void set_up_curated(JSONArray response)
    {
        ViewPagerCuratedAdapter adapter = new ViewPagerCuratedAdapter(getSupportFragmentManager());
        curated_size = response.length() ;
        for(int i = 0 ; i <curated_size;i++)
        {
            ArrayList<String> categories_array = new ArrayList<>();

            try {
                adapter.addFrag(CuratedFragment.newInstance(response.getJSONObject(i).getInt("id"),response.getJSONObject(i).getString("title"), response.getJSONObject(i).getString("image"),categories_array));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        viewPager_curated.setAdapter(adapter);


        dotsCount = adapter.getCount() ;
        dots = new ImageView[dotsCount];
        viewpager_indicator.removeAllViews();

        //add dots on layout view
        for (int i=0;i<dotsCount;i++)
        {
            dots[i] = new ImageView(this);

            if(i==0)
            {
                dots[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.style_selecteditem_dot, null));
            }
            else
            {
                dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.style_nonselecteditem_dot, null));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(11, 0, 11, 0);

            viewpager_indicator.addView(dots[i], params);
        }

        //change dot color on selected image
        viewPager_curated.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i =0;i<dotsCount;i++)
                {
                    dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.style_nonselecteditem_dot, null));
                }
                dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.style_selecteditem_dot, null));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getCategories()
    {
        if(!mActions.checkInternetConnection(Browse.this))
        {
            mActions.displayToast(Browse.this, "No Internet Connection");
        }
        else
        {
//            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "Content-Type", "application/json" );
            headers.put("X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));

            UserAPI.get_JsonArrayResp("experience_categories", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            gridView.setAdapter(new GridCategoriesAdapter(getApplicationContext(), response));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mActions.displayToast(Browse.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    static class ViewPagerCuratedAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerCuratedAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Browse.this))
        {
            mActions.displayToast(Browse.this, "No Internet Connection");
        }
        else
        {
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            //Define Params
            Map<String,String> params = new HashMap<>();

            //last time "completed_unreviewed_classes" was called
            params.put("since", prefs.getString("last_login", null));


            UserAPI.get_JsonArrayResp("members/"+prefs.getInt("id", -1)+"/completed_unreviewed_classes", headers, params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String my_time ;
                            my_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            my_time = my_time + "T" + new SimpleDateFormat("hh:mmZ").format(new Date());

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("last_login", my_time);
                            editor.apply();

                            if(response.length()!=0)
                            {
                                String event_name = "";
                                String event_id = "";
                                try {
                                    event_name= response.getJSONObject(0).getJSONObject("experience").getString("name");
                                    event_id= response.getJSONObject(0).getJSONObject("experience").getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //if avatar exists, then it is stored locally
                                if(prefs.contains("avatar"))
                                {
                                    avatar = prefs.getString("avatar", null);
                                }
                                else
                                {
                                    avatar = "no_avatar" ;
                                }

                                final String finalEvent_id = event_id;
                                final String finalEvent_name = event_name;
                                new AlertDialog.Builder(Browse.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("You did it!")
                                        .setMessage("How was "+ event_name +"\nWe want to hear all about it so please write a quick review and let us know how you got on.")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                swipeTimer.cancel();
                                                LeaveReview dialogFragment = LeaveReview.newInstance(avatar, finalEvent_id, finalEvent_name);
                                                dialogFragment.show(getFragmentManager(), "Prompt");
                                            }

                                        })
                                        .setNegativeButton("NO", null)
                                        .show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage()!=null && error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found")){
                                Log.v(TAG, "logout1");
                                mActions.displayToast(Browse.this, "Please log in again!");
                                prefs.edit().clear().apply();
                                Intent intent = new Intent(Browse.this, SplashNew.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if(error instanceof AuthFailureError)
                            {
                                Log.v(TAG, "logout2");
                                mActions.displayToast(Browse.this, "Please log in again!");
                                prefs.edit().clear().apply();
                                Intent intent = new Intent(Browse.this, SplashNew.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                mActions.displayToast(Browse.this, "Something went wrong");
                            }




                        }
                    });
        }
    }



    Runnable Update = new Runnable() {
                    public void run() {

                        if(viewPager_curated!=null && curated_size!=0) {
                            int currentPage = viewPager_curated.getCurrentItem() ;
                            if (currentPage == curated_size - 1) {
                                viewPager_curated.setCurrentItem(0, true);
                            }
                            else
                            {
                                viewPager_curated.setCurrentItem(currentPage + 1, true);
                }
            }
        }
    };

    private void get_settings()
    {
        if(!mActions.checkInternetConnection(Browse.this))
        {
            mActions.displayToast(Browse.this, "No Internet Connection");
        }
        else
        {

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_JsonObjResp("user_settings_set", headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                InitializeActivity.people.set("notifications", response.getBoolean("receive_push_notifications"));
                                InitializeActivity.people.set("paid", response.getBoolean("has_completed_sign_up"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mActions.displayToast(Browse.this, "Something went wrong");
                        }
                    });
        }
    }


}

