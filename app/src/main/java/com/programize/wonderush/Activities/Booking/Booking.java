package com.programize.wonderush.Activities.Booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.Browsing.Experience;
import com.programize.wonderush.Activities.InitializeActivity;
import com.programize.wonderush.Models.BookTime;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Booking extends FragmentActivity {

    private String TAG = "TAG - BOOKING SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private String experience_id ;
    private String class_id = null ;
    private int day_indicator = -1 ;
    private String time_selected = null ;
    private String start_time_selected = null ;
    private String experience_name ;
    private String address ;

    private String image_url = "";
    private String description = "";

    private TextView txt_head ;
    private TextView txt_event_name;
    private TextView txt_head_time ;
    private TextView txt_head_time_time ;
    private TextView txt_location;
    private TextView txt_calendar;
    private TextView txt_pick_date ;
    private TextView txt_pick_session ;
    private TextView txt_confirm ;

    private ImageView img_arrow_back ;
//    private ImageView img_social ;
    private ImageView img_background ;
//    private ImageView img_confirm ;

    private TextView[] txt_day = new TextView[7];

    private Calendar calendar ;
    private String date ;

    private ArrayList<ArrayList<BookTime>> available_hours ;

    private LinearLayout lr ;
//    private RelativeLayout rl_confirm;

    private JSONArray experience_categories ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle extras = getIntent().getExtras();
        experience_id = extras.getString("experience_id");

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_days();

        set_up_listeners();

    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt_head = (TextView)findViewById(R.id.bookingscreen_textview_head);
        txt_event_name = (TextView)findViewById(R.id.bookingscreen_textview_eventname);
        txt_head_time = (TextView)findViewById(R.id.bookingscreen_textview_headtime);
        txt_head_time_time = (TextView)findViewById(R.id.bookingscreen_textview_headtime_time);
        txt_location = (TextView)findViewById(R.id.bookingscreen_textview_locationname);
        txt_calendar = (TextView)findViewById(R.id.bookingscreen_textview_calendar);
        txt_pick_date = (TextView)findViewById(R.id.bookingscreen_textview_pickdate);
        txt_pick_session = (TextView)findViewById(R.id.bookingscreen_textview_picksession);
        txt_confirm = (TextView)findViewById(R.id.bookingscreen_textview_confirm);
//        rl_confirm = (RelativeLayout)findViewById(R.id.bookingscreen_relative_confirm);

        txt_day[0] = (TextView)findViewById(R.id.bookingscreen_textview_day1);
        txt_day[1] = (TextView)findViewById(R.id.bookingscreen_textview_day2);
        txt_day[2] = (TextView)findViewById(R.id.bookingscreen_textview_day3);
        txt_day[3] = (TextView)findViewById(R.id.bookingscreen_textview_day4);
        txt_day[4] = (TextView)findViewById(R.id.bookingscreen_textview_day5);
        txt_day[5] = (TextView)findViewById(R.id.bookingscreen_textview_day6);
        txt_day[6] = (TextView)findViewById(R.id.bookingscreen_textview_day7);

        img_arrow_back = (ImageView)findViewById(R.id.bookingscreen_imageview_backarrow);
//        img_social = (ImageView)findViewById(R.id.bookingscreen_imageview_social);
        img_background = (ImageView)findViewById(R.id.bookingscreen_image_headimage);
//        img_confirm = (ImageView)findViewById(R.id.bookingscreen_imageview_confirm);

        lr = (LinearLayout)findViewById(R.id.bookingscreen_linear_sessions);

        calendar = Calendar.getInstance();

        progressDialog = MyProgressDialog.ctor(Booking.this);

//        txt_confirm.setBackgroundResource(R.drawable.style_gradientcolor_transparent);
//        rl_confirm.setBackgroundColor(Color.parseColor("#ffffff"));
//        txt_confirm.setTextColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
    }

    private void set_up_font() {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_event_name.setTypeface(tf_Bold);
        txt_location.setTypeface(tf_Reg);
        txt_calendar.setTypeface(tf_Reg);
        txt_pick_date.setTypeface(tf_Reg);
        txt_pick_session.setTypeface(tf_Reg);
        txt_confirm.setTypeface(tf_Bold);

        txt_day[0].setTypeface(tf_Bold);
        txt_day[1].setTypeface(tf_Bold);
        txt_day[2].setTypeface(tf_Bold);
        txt_day[3].setTypeface(tf_Bold);
        txt_day[4].setTypeface(tf_Bold);
        txt_day[5].setTypeface(tf_Bold);
        txt_day[6].setTypeface(tf_Bold);

        txt_head_time.setTypeface(tf_Bold);
        txt_head_time_time.setTypeface(tf_Bold);
    }

    private void set_up_images()
    {

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

        load_info();
    }

    private void load_info()
    {
        if(!mActions.checkInternetConnection(Booking.this))
        {
            mActions.displayToast(Booking.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonObjResp("experiences", experience_id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Booking.this, "Experience Info Loaded !");
                            Log.v(TAG + "EXPERIENCE RESPONSE ", response.toString());

                            try {
                                setValues(response);
                                set_up_days_listeners(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Booking.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void setValues(JSONObject response) throws JSONException {

        experience_name = response.getString("name") ;
        txt_head.setText(response.getString("name"));
        txt_event_name.setText(response.getString("name").toUpperCase());
        txt_location.setText(response.getString("city"));
        txt_calendar.setText(mActions.getDays(response.getJSONArray("days_of_week")));
        description = response.getString("description");
        address = response.getString("address_line_1") + ", " + response.getString("address_line_2") ;

        experience_categories = response.getJSONArray("experience_categories") ;

        image_url = Definitions.APIdomain + response.getString("experience_image") ;
//        Picasso.with(this).load(image_url).resize(600, 400).onlyScaleDown().memoryPolicy(MemoryPolicy.NO_STORE).into(img_background);
        Glide.with(this).load(image_url).centerCrop().into(img_background);
    }

    private void set_up_listeners()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (class_id != null) {
                    book_event();
                }
//                else {
//                    mActions.displayToast(Booking.this, "incomplete");
//                }

            }
        });
    }

    private void set_up_days()
    {

        for(int i =0;i<7;i++)
        {
            date = new SimpleDateFormat("dd MMMM").format(calendar.getTime());

            String day_print2 = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).substring(0,3);
//            txt_day[i].setText(day_print2 + " " + date.substring(0, 6));
//            txt_day[i].setText(day_print2 + " " + date.substring(0, 2) + "  "  + date.substring(3, 6));
            txt_day[i].setText(Html.fromHtml(day_print2 + "<br>" + date.substring(0, 2) + "<br>" + date.substring(3, 6)));

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.add(Calendar.DAY_OF_YEAR, -7);
    }

    private void set_up_days_listeners(JSONObject response) throws JSONException {
        JSONArray jarray = response.getJSONArray("experience_classes");
        available_hours = new ArrayList<>();
        ArrayList<BookTime> hours ;

        //FILL available_hours ARRAY WITH CORRECT VALUES
        for(int j=0;j<7;j++)
        {
            hours = new ArrayList<>();

            date = new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            for(int i=0;i<jarray.length();i++)
            {
                if(jarray.getJSONObject(i).getString("start_time").contains(date))
                {
                    hours.add(new BookTime(jarray.getJSONObject(i).getString("id"), jarray.getJSONObject(i).getString("start_time").substring(11, 16), jarray.getJSONObject(i).getInt("no_of_places_left"), jarray.getJSONObject(i).getBoolean("bookable"), jarray.getJSONObject(i).getString("start_time")));
                }
            }
            available_hours.add(hours);
        }
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        //FOR EACH DAY
        for(int i = 0;i<7;i++)
        {
            //IF DAY HAS NO CLASSES
            if(available_hours.get(i).size()==0)
            {
                txt_day[i].setTextColor(Color.parseColor("#d12b54"));
            }
            //IF DAY HAS CLASSES
            else
            {
                txt_day[i].setTextColor(Color.parseColor("#ffffff"));
                final int finalI = i;
                //IF DAY WITH AVAILABLE CLASS IS SELECTED
                txt_day[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lr.removeAllViews();

                        txt_head_time.setText("");
                        txt_head_time_time.setText("");

                        calendar.add(Calendar.DAY_OF_YEAR, finalI);
//                        Log.v("TAG", calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
                        date = new SimpleDateFormat("dd MMMM").format(calendar.getTime());
                        String day_print2 = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).substring(0,3);
                        String day_print = new SimpleDateFormat("dd").format(calendar.getTime());
                        String month_print = new SimpleDateFormat(" MMMM").format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_YEAR, -finalI);
                        if(day_print.equals("01")||(day_print.equals("21"))||(day_print.equals("31")))
                        {
                            day_print = day_print + "st";
                        }
                        else if(day_print.equals("02")||(day_print.equals("22")))
                        {
                            day_print = day_print + "nd";
                        }
                        else if(day_print.equals("03")||(day_print.equals("23")))
                        {
                            day_print = day_print + "rd";
                        }
                        else
                        {
                            day_print = day_print + "th";
                        }
                        txt_head_time.setText(day_print2 + " " + day_print + month_print);

                        class_id = null ;
                        time_selected = null ;
                        start_time_selected = null ;
//                        txt_confirm.setBackgroundResource(R.drawable.style_gradientcolor_transparent);
//                        txt_confirm.setTextColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
                        day_indicator = finalI ;

                        for (int j = 0; j < available_hours.get(finalI).size(); j++) {
                            final int finalJ = j;
                            final TextView txt1 = new TextView(getApplicationContext());
                            txt1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Reg.ttf"));

                            lr.addView(txt1);

                            txt1.setText(available_hours.get(finalI).get(j).getTime());
                            txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            txt1.setPadding(20, 20, 20, 20);

                            Log.v("TAG - exp bool", (available_hours.get(finalI).get(finalJ).isBookable())+ "");
                            if ((available_hours.get(finalI).get(finalJ).getPlaces() == 0) || (!(available_hours.get(finalI).get(finalJ).isBookable())))
                            {
                                txt1.setTextColor(Color.parseColor("#d12b54"));
                            }
                            else
                            {
                                txt1.setTextColor(Color.parseColor("#ffffff"));
                                txt1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        mActions.displayToast(Booking.this, available_hours.get(finalI).get(finalJ).getId());
                                        class_id = available_hours.get(finalI).get(finalJ).getId();
                                        txt_confirm.setTextColor(getResources().getColor(R.color.white));
                                        txt_confirm.setBackgroundResource(R.drawable.style_gradientcolor_profile);

//                                        calendar.add(Calendar.DAY_OF_YEAR, finalI);
//                                        date = new SimpleDateFormat("dd MMMM").format(calendar.getTime());
//                                        calendar.add(Calendar.DAY_OF_YEAR, -finalI);
//                                        txt_head_time.setText(date);
                                        txt_head_time_time.setText(available_hours.get(finalI).get(finalJ).getTime());
                                        time_selected = available_hours.get(finalI).get(finalJ).getTime() ;
                                        start_time_selected = available_hours.get(finalI).get(finalJ).getStart_time();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    private void book_event()
    {
        if(!mActions.checkInternetConnection(Booking.this))
        {
            mActions.displayToast(Booking.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            //Define Headers
            Map<String,String> params = new HashMap<>();
            params.put( "id", class_id );

            Log.v(TAG, class_id);

            UserAPI.post_StringResp("experience_class_bookings", null, headers, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Booking.this, "Booking was successful");

                            //Mix Panel event handling
                            try {
                                JSONObject props = new JSONObject();
                                props.put("Experience ID",experience_id );
                                props.put("Experience Name", experience_name);
                                props.put("Class Start Date", start_time_selected);
                                props.put("Class ID", class_id);

                                JSONArray categories = new JSONArray();
                                for (int i = 0 ; i<experience_categories.length();i++)
                                {
                                    categories.put(experience_categories.getJSONObject(i).getString("name"));
                                }

                                props.put("Categories", categories);
                                Log.v("TAG mix panel event", props.toString());
                                InitializeActivity.mMixpanel.track("Class Booking", props);
                            } catch (JSONException e) {
                                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                            }

                            Intent intent = new Intent(Booking.this, Experience.class);
                            intent.putExtra("experience_id", experience_id);
                            intent.putExtra("description", description);
                            intent.putExtra("image_url", image_url);
                            intent.putExtra("experience_name", experience_name);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("day_indicator", day_indicator);
                            intent.putExtra("time_selected", time_selected);
                            intent.putExtra("start_time_selected", start_time_selected);
                            intent.putExtra("address", address);
                            intent.putExtra("from_booking", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Booking.this, "Something went wrong");
//                            try {
//                                Log.v("TAG", new String(error.networkResponse.data,"UTF-8"));
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
        }

    }


}
