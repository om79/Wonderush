package com.programize.wonderush.Activities.Browsing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Fragments.BucketFragment;
import com.programize.wonderush.Fragments.LeaveReview;
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

public class Buckets extends AppCompatActivity {

    private String TAG = "TAG - BUCKET LIST SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView txt_head ;
    private ImageView img_arrow_back ;
    private ImageView img_arrow_down ;

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

    private ViewPager viewPager ;

    private Typeface tf_Bold ;
    private Typeface tf_Reg ;

    private static String response_str = "";
    private static String[] days = new String[7];
    private static String mLocation = "london";
    ViewPagerBucketAdapter bucket_adapter ;

    private TextView[] filter_day;

    private String avatar ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buckets);

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_listener();

        searchBuckets();

        regionListeners();
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
//        searchBuckets();
    }

    private void initialize_widgets()
    {
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        txt_head = (TextView)findViewById(R.id.bucketscreen_textview_head);
        img_arrow_back = (ImageView)findViewById(R.id.bucketscreen_imageview_backarrow);
        img_arrow_down = (ImageView)findViewById(R.id.bucketscreen_imageview_arrow_down);

        london = (TextView)findViewById(R.id.bucketscreen_textview_london);
        more_cities = (TextView)findViewById(R.id.bucketscreen_textview_morecities);
        north_london = (TextView)findViewById(R.id.bucketscreen_textview_northlondon);
        east_london = (TextView)findViewById(R.id.bucketscreen_textview_eastlondon);
        south_london = (TextView)findViewById(R.id.bucketscreen_textview_southlondon);
        west_london = (TextView)findViewById(R.id.bucketscreen_textview_westlondon);
        central_london = (TextView)findViewById(R.id.bucketscreen_textview_centrallondon);

        location_head = (LinearLayout)findViewById(R.id.bucketscreen_linear_head_inside);
        location_body = (LinearLayout)findViewById(R.id.bucketscreen_linear_location);

        viewPager = (ViewPager)findViewById(R.id.bucketscreen_viewpager);

        filter_day = new TextView[7];
        filter_day[0] = (TextView)findViewById(R.id.bucketscreen_textview_day1);
        filter_day[1] = (TextView)findViewById(R.id.bucketscreen_textview_day2);
        filter_day[2] = (TextView)findViewById(R.id.bucketscreen_textview_day3);
        filter_day[3] = (TextView)findViewById(R.id.bucketscreen_textview_day4);
        filter_day[4] = (TextView)findViewById(R.id.bucketscreen_textview_day5);
        filter_day[5] = (TextView)findViewById(R.id.bucketscreen_textview_day6);
        filter_day[6] = (TextView)findViewById(R.id.bucketscreen_textview_day7);


        //SET UP CALENDAR
        Calendar calendar = Calendar.getInstance();
        setupDays(calendar.get(Calendar.DAY_OF_WEEK));

        progressDialog = MyProgressDialog.ctor(Buckets.this);
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
    }

    private void set_up_images()
    {
    }

    private void set_up_listener()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialize_viewpager()
    {
        mLocation = "London" ;

        bucket_adapter = new ViewPagerBucketAdapter(getSupportFragmentManager());

        viewPager.setAdapter(bucket_adapter);

        bucket_adapter.notifyDataSetChanged();


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

    private void searchBuckets()
    {
        if(!mActions.checkInternetConnection(Buckets.this))
        {
            mActions.displayToast(Buckets.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonArrayResp("members", prefs.getInt("id", 0)+"/bucketlist", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.dismiss();
                            Log.v(TAG + "BUCKET LIST RESPONSE ", response.toString());
                            response_str = response.toString() ;
                            initialize_viewpager();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Buckets.this, "Something went wrong");
                        }
                    });
        }
    }

    static class ViewPagerBucketAdapter extends FragmentStatePagerAdapter {

        public ViewPagerBucketAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public BucketFragment getItem(int position) {
            if(position == 0)
            {
                return BucketFragment.newInstance(response_str, "all", mLocation, false);
            }
            else if(position == 1)
            {
                return BucketFragment.newInstance(response_str, days[0], mLocation, false);
            }
            else if(position == 2)
            {
                return BucketFragment.newInstance(response_str, days[1], mLocation, false);
            }
            else if(position == 3)
            {
                return BucketFragment.newInstance(response_str, days[2], mLocation, false);
            }
            else if(position == 4)
            {
                return BucketFragment.newInstance(response_str, days[3], mLocation, false);
            }
            else if(position == 5)
            {
                return BucketFragment.newInstance(response_str, days[4], mLocation, false);
            }
            else if(position == 6)
            {
                return BucketFragment.newInstance(response_str, days[5], mLocation, false);
            }
            else
            {
                return BucketFragment.newInstance(response_str, days[6], mLocation, false);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "All";
                case 1:
                    return days[0];
                case 2:
                    return days[1];
                case 3:
                    return days[2];
                case 4:
                    return days[3];
                case 5:
                    return days[4];
                case 6:
                    return days[5];
                case 7:
                    return days[6];
                default:
                    return "All";
            }
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
            public void onClick(View v)
            {
                if(location_body.getVisibility()==View.VISIBLE)
                {
                    location_body.setVisibility(View.GONE);
                    img_arrow_down.setImageResource(R.drawable.icon_arrow_down);
                }
                else
                {
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
        bucket_adapter.notifyDataSetChanged();
    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Buckets.this))
        {
            mActions.displayToast(Buckets.this, "No Internet Connection");
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

            Log.v(TAG, prefs.getString("last_login", null));
            params.put("since", prefs.getString("last_login", null));

            UserAPI.get_JsonArrayResp("members/"+prefs.getInt("id", -1)+"/completed_unreviewed_classes", headers, params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
//                            mActions.displayToast(Tickets.this, "GETTING UNREVIEWED FINISHED !");
                            Log.v(TAG + "UN REVIEWED RESPONSE ", response.toString());
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
                                new AlertDialog.Builder(Buckets.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("You did it!")
                                        .setMessage("How was "+ event_name +"\nWe want to hear all about it so please write a quick review and let us know how you got on.")
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
                            mActions.displayToast(Buckets.this, "Error Occurred");
                        }
                    });
        }
    }
}
