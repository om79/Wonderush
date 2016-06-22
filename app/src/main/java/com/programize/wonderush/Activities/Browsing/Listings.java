package com.programize.wonderush.Activities.Browsing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Fragments.ListingFragment;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Listings extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "TAG - LISTINGS SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView txt_head ;
    private ImageView img_arrow_back ;
    private ImageView img_arrow_down ;
    private ImageView img_location ;

    //pop up values for location filtering
    private LinearLayout location_head ; //head layout
    private LinearLayout location_body; //pop up layout

    private TextView london ;
    private TextView more_cities ;
    private TextView north_london ;
    private TextView east_london ;
    private TextView south_london ;
    private TextView west_london ;
    private TextView central_london ;

    private LinearLayout layout_tabs ;

    private ViewPager viewPager ;

    private String avatar ;

    private Typeface tf_Bold ;
    private Typeface tf_Reg ;

    private static String response_str = "";
    private static String[] days = new String[7];
    private static String mLocation = "london";
    ViewPagerListingAdapter listing_adapter ;

    private String experience_category = null ;
    private int curated_list = -1;
    private String search = "";

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private String myLat = "" ;
    private String myLng = "" ;
    private Boolean fromNearMe = false;

    private TextView[] filter_day;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        //TAKE VALUES FROM PREVIOUS SCREEN
        Bundle extras = getIntent().getExtras();

        if(getIntent().hasExtra("experience_category"))
        {
            experience_category = extras.getString("experience_category");
//            mActions.displayToast(Listings.this, "Category ID: " + experience_category);
        }
        if(getIntent().hasExtra("curated_list"))
        {
            curated_list = extras.getInt("curated_list");
//            mActions.displayToast(Listings.this, "Curated Array: " + extras.getInt("curated_list") + "");
        }
        if(getIntent().hasExtra("search"))
        {
            search = extras.getString("search");
//            mActions.displayToast(Listings.this, "Search: " + extras.getString("search") + "");
        }

        initialize_widgets();

        set_up_font();

        set_up_listener();

        regionListeners();

        searchListings();

        buildGoogleApiClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }

        mMenu = mActions.set_up_menu(this);

        get_unreviewed_events();
    }

    private void initialize_widgets()
    {
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        txt_head = (TextView)findViewById(R.id.listingsscreen_textview_head);
        img_arrow_back = (ImageView)findViewById(R.id.listingsscreen_imageview_backarrow);
        img_arrow_down = (ImageView)findViewById(R.id.listingsscreen_imageview_arrow_down);
        img_location = (ImageView)findViewById(R.id.listingsscreen_imageview_location);

        london = (TextView)findViewById(R.id.listingsscreen_textview_london);
        more_cities = (TextView)findViewById(R.id.listingsscreen_textview_morecities);
        north_london = (TextView)findViewById(R.id.listingsscreen_textview_northlondon);
        east_london = (TextView)findViewById(R.id.listingsscreen_textview_eastlondon);
        south_london = (TextView)findViewById(R.id.listingsscreen_textview_southlondon);
        west_london = (TextView)findViewById(R.id.listingsscreen_textview_westlondon);
        central_london = (TextView)findViewById(R.id.listingsscreen_textview_centrallondon);

        location_head = (LinearLayout)findViewById(R.id.listingsscreen_linear_head_inside);
        location_body = (LinearLayout)findViewById(R.id.listingsscreen_linear_location);

        layout_tabs = (LinearLayout)findViewById(R.id.listingsscreen_linear_tabs);
        viewPager = (ViewPager)findViewById(R.id.listingsscreen_viewpager);

        filter_day = new TextView[7];
        filter_day[0] = (TextView)findViewById(R.id.listingsscreen_textview_day1);
        filter_day[1] = (TextView)findViewById(R.id.listingsscreen_textview_day2);
        filter_day[2] = (TextView)findViewById(R.id.listingsscreen_textview_day3);
        filter_day[3] = (TextView)findViewById(R.id.listingsscreen_textview_day4);
        filter_day[4] = (TextView)findViewById(R.id.listingsscreen_textview_day5);
        filter_day[5] = (TextView)findViewById(R.id.listingsscreen_textview_day6);
        filter_day[6] = (TextView)findViewById(R.id.listingsscreen_textview_day7);

        //SET UP CALENDAR
        Calendar calendar = Calendar.getInstance();
        setupDays(calendar.get(Calendar.DAY_OF_WEEK));

        progressDialog = MyProgressDialog.ctor(Listings.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);

        london.setTypeface(tf_Bold);
        more_cities.setTypeface(tf_Reg);
        north_london.setTypeface(tf_Reg);
        east_london.setTypeface(tf_Reg);
        south_london.setTypeface(tf_Reg);
        west_london.setTypeface(tf_Reg);
        central_london.setTypeface(tf_Reg);

        filter_day[0].setTypeface(tf_Reg);
        filter_day[1].setTypeface(tf_Reg);
        filter_day[2].setTypeface(tf_Reg);
        filter_day[3].setTypeface(tf_Reg);
        filter_day[4].setTypeface(tf_Reg);
        filter_day[5].setTypeface(tf_Reg);
        filter_day[6].setTypeface(tf_Reg);
    }

    private void set_up_listener()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //NEAR ME CLICK LISTENER
        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromNearMe = true;
                searchListings();
            }
        });
    }



    private void initialize_viewpager()
    {
        mLocation = "London" ;

        listing_adapter = new ViewPagerListingAdapter(getSupportFragmentManager());

        listing_adapter.notifyDataSetChanged();

        viewPager.setAdapter(listing_adapter);

        filter_day[0].setText(days[0]);
        filter_day[1].setText(days[1]);
        filter_day[2].setText(days[2]);
        filter_day[3].setText(days[3]);
        filter_day[4].setText(days[4]);
        filter_day[5].setText(days[5]);
        filter_day[6].setText(days[6]);

        filter_day[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewPager.getCurrentItem()==1)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(1);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
            }
        });
        filter_day[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==2)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(2);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
            }
        });
        filter_day[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==3)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(3);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }

            }
        });
        filter_day[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==4)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(4);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }

            }
        });
        filter_day[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==5)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(5);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }

            }
        });
        filter_day[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==6)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(6);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }

            }
        });

        filter_day[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==7)
                {
                    viewPager.setCurrentItem(0);
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                }
                else
                {
                    viewPager.setCurrentItem(7);
                    filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                    filter_day[6].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                }

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position)
                {
                    case 0:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 1:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 2:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 3:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 4:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 5:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 6:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));
                        break;
                    case 7:
                        filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                        filter_day[6].setBackgroundColor(getResources().getColor(R.color.selected_pink));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void searchListings()
    {
        if(!mActions.checkInternetConnection(Listings.this))
        {
            mActions.displayToast(Listings.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "Content-Type", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            //Define Parameters
            Map<String,String> params = new HashMap<>();

            if(experience_category!=null)
            {
                params.put("q[experience_categories_name_cont]", experience_category);
            }

            if(curated_list != -1)
            {
                params.put("curated_list_id", curated_list + "");
                params.put("q[area_cont]", "All");
            }

            if(fromNearMe)
            {
                params.put("latitude", myLat);
                params.put("longitude", myLng);
                fromNearMe = false ;

                filter_day[0].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[1].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[2].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[3].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[4].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[5].setBackgroundColor(getResources().getColor(R.color.pink1));
                filter_day[6].setBackgroundColor(getResources().getColor(R.color.pink1));

                txt_head.setText("LONDON / All Areas");
                london.setTypeface(tf_Bold);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

            }

            UserAPI.get_JsonArrayResp2("experiences", headers, params, search,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.dismiss();

                            response_str = response.toString();

                            Log.v("TAG resp", response_str);
                            initialize_viewpager();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Listings.this, "Something went wrong");
                        }
                    });
        }
    }

    static class ViewPagerListingAdapter extends FragmentStatePagerAdapter {

        public ViewPagerListingAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public ListingFragment getItem(int position) {
            if(position == 0)
            {
                return ListingFragment.newInstance(response_str, "all", mLocation, false);
            }
            else if(position == 1)
            {
                return ListingFragment.newInstance(response_str, days[0], mLocation, false);
            }
            else if(position == 2)
            {
                return ListingFragment.newInstance(response_str, days[1], mLocation, false);
            }
            else if(position == 3)
            {
                return ListingFragment.newInstance(response_str, days[2], mLocation, false);
            }
            else if(position == 4)
            {
                return ListingFragment.newInstance(response_str, days[3], mLocation, false);
            }
            else if(position == 5)
            {
                return ListingFragment.newInstance(response_str, days[4], mLocation, false);
            }
            else if(position == 6)
            {
                return ListingFragment.newInstance(response_str, days[5], mLocation, false);
            }
            else
            {
                return ListingFragment.newInstance(response_str, days[6], mLocation, false);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void setupDays(int day)
    {
        switch (day) {
            case Calendar.SUNDAY:
                days[0] ="SUN";
                days[1] ="MON";
                days[2] ="TUE";
                days[3] ="WED";
                days[4] ="THU";
                days[5] ="FRI";
                days[6] ="SAT";
                break;
            case Calendar.MONDAY:
                days[0] ="MON";
                days[1] ="TUE";
                days[2] ="WED";
                days[3] ="THU";
                days[4] ="FRI";
                days[5] ="SAT";
                days[6] ="SUN";
                break;
            case Calendar.TUESDAY:
                days[0] ="TUE";
                days[1] ="WED";
                days[2] ="THU";
                days[3] ="FRI";
                days[4] ="SAT";
                days[5] ="SUN";
                days[6] ="MON";
                break;
            case Calendar.WEDNESDAY:
                days[0] ="WED";
                days[1] ="THU";
                days[2] ="FRI";
                days[3] ="SAT";
                days[4] ="SUN";
                days[5] ="MON";
                days[6] ="TUE";
                break;
            case Calendar.THURSDAY:
                days[0] ="THU";
                days[1] ="FRI";
                days[2] ="SAT";
                days[3] ="SUN";
                days[4] ="MON";
                days[5] ="TUE";
                days[6] ="WED";
                break;
            case Calendar.FRIDAY:
                days[0] ="FRI";
                days[1] ="SAT";
                days[2] ="SUN";
                days[3] ="MON";
                days[4] ="TUE";
                days[5] ="WED";
                days[6] ="THU";
                break;
            case Calendar.SATURDAY:
                days[0] ="SAT";
                days[1] ="SUN";
                days[2] ="MON";
                days[3] ="TUE";
                days[4] ="WED";
                days[5] ="THU";
                days[6] ="FRI";
                break;
        }
    }

    private void regionListeners()
    {
        //LOCATION CLICK LISTENER
        location_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location_body.getVisibility() == View.VISIBLE) {
                    location_body.setVisibility(View.GONE);
                    img_arrow_down.setImageResource(R.drawable.icon_arrow_down);
                } else {
                    {
                        location_body.setVisibility(View.VISIBLE);
                        img_arrow_down.setImageResource(R.drawable.icon_arrow_up);
                    }
                }
            }
        });

        /*START LOCATION LISTENERS ****************************/
        london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / All Areas");
                london.setTypeface(tf_Bold);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("London");
            }
        });

        north_london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / North London");
                london.setTypeface(tf_Reg);
                north_london.setTypeface(tf_Bold);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("North London");
            }
        });

        east_london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / East London");
                london.setTypeface(tf_Reg);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Bold);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("East London");
            }
        });

        south_london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / South London");
                london.setTypeface(tf_Reg);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Bold);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("South London");
            }
        });

        west_london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / West London");
                london.setTypeface(tf_Reg);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Bold);
                central_london.setTypeface(tf_Reg);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("West London");
            }
        });

        central_london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_head.setText("LONDON / Central London");
                london.setTypeface(tf_Reg);
                north_london.setTypeface(tf_Reg);
                east_london.setTypeface(tf_Reg);
                south_london.setTypeface(tf_Reg);
                west_london.setTypeface(tf_Reg);
                central_london.setTypeface(tf_Bold);

                location_body.setVisibility(View.GONE);
                img_arrow_down.setImageResource(R.drawable.icon_arrow_down);

                add_location_filter("Central London");
            }
        });

        location_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*END LOCATION LISTENERS ****************************/
    }

    private void add_location_filter(String location)
    {
        mLocation = location ;
        listing_adapter.notifyDataSetChanged();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            myLat = String.valueOf(mLastLocation.getLatitude());
            myLng = String.valueOf(mLastLocation.getLongitude());
        } else {
            //no coordinations
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Listings.this))
        {
            mActions.displayToast(Listings.this, "No Internet Connection");
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

            params.put("since", prefs.getString("last_login", null));

            UserAPI.get_JsonArrayResp("members/" + prefs.getInt("id", -1) + "/completed_unreviewed_classes", headers, params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String my_time;
                            my_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            my_time = my_time + "T" + new SimpleDateFormat("hh:mmZ").format(new Date());

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("last_login", my_time);
                            editor.apply();

                            if (response.length() != 0) {
                                String event_name = "";
                                String event_id = "";
                                try {
                                    event_name = response.getJSONObject(0).getJSONObject("experience").getString("name");
                                    event_id = response.getJSONObject(0).getJSONObject("experience").getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (prefs.contains("avatar")) {
                                    avatar = prefs.getString("avatar", null);
                                } else {
                                    avatar = "no_avatar";
                                }

                                final String finalEvent_id = event_id;
                                final String finalEvent_name = event_name;
                                new AlertDialog.Builder(Listings.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("You did it!")
                                        .setMessage("How was " + event_name + "\nWe want to hear all about it so please write a quick review and let us know how you got on.")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                LeaveReview dialogFragment = LeaveReview.newInstance(avatar, finalEvent_id, finalEvent_name);
                                                dialogFragment.show(getFragmentManager(), "Sample Fragment");
                                            }
                                        })
                                        .setNegativeButton("NO", null)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            mActions.ProgressDialogHide();
                            mActions.displayToast(Listings.this, "Error Occurred");
                        }
                    });
        }
    }

}
