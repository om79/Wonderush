package com.programize.wonderush.Activities.Booking;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Fragments.ShareInfo;
import com.programize.wonderush.Fragments.TicketPrevious;
import com.programize.wonderush.Fragments.TicketUpcoming;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tickets extends AppCompatActivity {

    private String TAG = "TAG - TICKETS SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private ViewPager viewpager_upcoming ;
    private ViewPager viewpager_previous ;

    private TextView txt_head ;
    private TextView txt_upcoming;
    private TextView txt_previous;
    private TextView txt_browse;
    private TextView txt_nobook1;
    private TextView txt_nobook2;

    private ImageView img_social ;
    private ImageView img_arrow_back ;

    private ViewPagerTicketsAdapter_upcoming adapter_upcoming ;
    private ViewPagerTicketsAdapter_previous adapter_previous ;
    private RelativeLayout rl_browse;

    private JSONArray jarray_previous ;
    private JSONArray jarray_upcoming ;

    String avatar ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_listeners();

        get_previous_booking();
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
//        get_previous_booking();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt_head = (TextView)findViewById(R.id.ticketscreen_textview_head);
        txt_upcoming = (TextView)findViewById(R.id.ticketscreen_textview_upcoming);
        txt_previous = (TextView)findViewById(R.id.ticketscreen_textview_previous);
        txt_nobook1 = (TextView)findViewById(R.id.ticketscreen_text_nobook1);
        txt_nobook2 = (TextView)findViewById(R.id.ticketscreen_text_nobook2);
        txt_browse = (TextView)findViewById(R.id.ticketscreen_text_browse);

        img_social = (ImageView)findViewById(R.id.ticketscreen_imageview_social);
        img_arrow_back = (ImageView)findViewById(R.id.ticketscreen_imageview_backarrow);

        viewpager_upcoming = (ViewPager)findViewById(R.id.ticketscreen_viewpager_upcoming);
        viewpager_previous = (ViewPager)findViewById(R.id.ticketscreen_viewpager_previous);
        rl_browse = (RelativeLayout)findViewById(R.id.ticketscreen_rl_browse);

        jarray_previous = new JSONArray();
        jarray_upcoming = new JSONArray();

        progressDialog = MyProgressDialog.ctor(Tickets.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_upcoming.setTypeface(tf_Reg);
        txt_previous.setTypeface(tf_Reg);
        txt_nobook1.setTypeface(tf_Reg);
        txt_nobook2.setTypeface(tf_Reg);
        txt_browse.setTypeface(tf_Reg);
    }

    private void set_up_images()
    {
        //SETTING UP IMAGES
        img_arrow_back.setImageResource(R.drawable.icon_arrow_back);
        img_social.setImageResource(R.drawable.icon_share);
    }

    private void set_up_viewpager_upcoming()
    {
        viewpager_upcoming.setClipToPadding(false);
        viewpager_upcoming.setPadding(50, 0, 50, 0);
        viewpager_upcoming.setPageMargin(15);

        adapter_upcoming = new ViewPagerTicketsAdapter_upcoming(getSupportFragmentManager());
        adapter_upcoming.notifyDataSetChanged();
        viewpager_upcoming.setAdapter(adapter_upcoming);

        if(!getIntent().hasExtra("from_profile"))
        {
            if(jarray_upcoming.length()==0)
            {
                viewpager_upcoming.setVisibility(View.GONE);
                viewpager_previous.setVisibility(View.GONE);
                if(jarray_previous.length()==0)
                {
                    txt_nobook1.setText("You haven't booked anything just yet.");
                }
                else
                {
                    txt_nobook1.setText("You don’t currently have any live bookings.");
                }
                rl_browse.setVisibility(View.VISIBLE);
            }
            else
            {
                viewpager_upcoming.setVisibility(View.VISIBLE);
                viewpager_previous.setVisibility(View.GONE);
                rl_browse.setVisibility(View.GONE);
            }
        }
    }
    private void set_up_viewpager_previous()
    {
        viewpager_previous.setClipToPadding(false);
        viewpager_previous.setPadding(50, 0, 50, 0);
        viewpager_previous.setPageMargin(15);

        adapter_previous = new ViewPagerTicketsAdapter_previous(getSupportFragmentManager());
        adapter_previous.notifyDataSetChanged();
        viewpager_previous.setAdapter(adapter_previous);

        if(getIntent().hasExtra("from_profile"))
        {
            if(jarray_previous.length()==0)
            {
                viewpager_upcoming.setVisibility(View.GONE);
                viewpager_previous.setVisibility(View.GONE);
                txt_nobook1.setText("You haven't completed anything just yet.");
                rl_browse.setVisibility(View.VISIBLE);
            }
            else
            {
                viewpager_upcoming.setVisibility(View.GONE);
                viewpager_previous.setVisibility(View.VISIBLE);
                rl_browse.setVisibility(View.GONE);
            }
        }
    }

    private void set_up_listeners()
    {
        //UPCOMING CLICK LISTENER
        txt_upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_upcoming.setBackgroundColor(getResources().getColor(R.color.selected_pink));
                txt_previous.setBackgroundColor(getResources().getColor(R.color.pink1));

                if(jarray_upcoming.length()==0)
                {
                    viewpager_upcoming.setVisibility(View.GONE);
                    viewpager_previous.setVisibility(View.GONE);
                    if(jarray_previous.length()==0)
                    {
                        txt_nobook1.setText("You haven't booked anything just yet.");
                    }
                    else
                    {
                        txt_nobook1.setText("You don’t currently have any live bookings.");
                    }
                    rl_browse.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewpager_upcoming.setVisibility(View.VISIBLE);
                    viewpager_previous.setVisibility(View.GONE);
                    rl_browse.setVisibility(View.GONE);
                }

            }
        });

        //PREVIOUS CLICK LISTENER
        txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_upcoming.setBackgroundColor(getResources().getColor(R.color.pink1));
                txt_previous.setBackgroundColor(getResources().getColor(R.color.selected_pink));

                if(jarray_previous.length()==0)
                {
                    viewpager_upcoming.setVisibility(View.GONE);
                    viewpager_previous.setVisibility(View.GONE);
                    txt_nobook1.setText("You haven't completed anything just yet.");
                    rl_browse.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewpager_upcoming.setVisibility(View.GONE);
                    viewpager_previous.setVisibility(View.VISIBLE);
                    rl_browse.setVisibility(View.GONE);
                }
            }
        });

        txt_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent ;
                new_intent = new Intent(Tickets.this, Browse.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new_intent);
                finish();
            }
        });

        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "";
                String image_url = "";
                String title = "";
                String experience_id = "";
                if(viewpager_previous.getVisibility()==View.VISIBLE)
                {
                    Log.v(TAG+"position pre",viewpager_previous.getCurrentItem() +"" );

                    try {
                        description = jarray_previous.getJSONObject(viewpager_previous.getCurrentItem()).getJSONObject("experience").getString("description");
                        image_url = Definitions.APIdomain +jarray_previous.getJSONObject(viewpager_previous.getCurrentItem()).getJSONObject("experience").getString("experience_image");
                        title = jarray_previous.getJSONObject(viewpager_previous.getCurrentItem()).getJSONObject("experience").getString("name");
                        experience_id = jarray_previous.getJSONObject(viewpager_previous.getCurrentItem()).getJSONObject("experience").getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v(TAG+"id's", experience_id +" " + title+" " +image_url+" " +description);


                    ShareInfo share_info = ShareInfo.newInstance(description, image_url, title, experience_id);
                    share_info.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                    share_info.show(getFragmentManager(), "Sample Fragment");
                }
                if(viewpager_upcoming.getVisibility()==View.VISIBLE)
                {
                    Log.v(TAG+"position up",viewpager_upcoming.getCurrentItem() +"" );

                    try {
                        description = jarray_upcoming.getJSONObject(viewpager_upcoming.getCurrentItem()).getJSONObject("experience").getString("description");
                        image_url = Definitions.APIdomain +jarray_upcoming.getJSONObject(viewpager_upcoming.getCurrentItem()).getJSONObject("experience").getString("experience_image");
                        title = jarray_upcoming.getJSONObject(viewpager_upcoming.getCurrentItem()).getJSONObject("experience").getString("name");
                        experience_id = jarray_upcoming.getJSONObject(viewpager_upcoming.getCurrentItem()).getJSONObject("experience").getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v(TAG+"id's", experience_id +" " + title+" " +image_url+" " +description);


                    ShareInfo share_info = ShareInfo.newInstance(description, image_url, title, experience_id);
                    share_info.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                    share_info.show(getFragmentManager(), "Sample Fragment");
                }

            }
        });
    }

    private void get_previous_booking()
    {
        if(!mActions.checkInternetConnection(Tickets.this))
        {
            mActions.displayToast(Tickets.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonArrayResp("members", prefs.getInt("id", 0) + "/bookings/previous", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.v(TAG + "PREV TICKETS RESPONSE ", response.toString());

                            jarray_previous = response;

                            if(getIntent().hasExtra("from_profile"))
                            {
                                txt_upcoming.setBackgroundColor(getResources().getColor(R.color.pink1));
                                txt_previous.setBackgroundColor(getResources().getColor(R.color.selected_pink));
                            }
                            set_up_viewpager_previous();

                            get_upcoming_booking();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Tickets.this, "Something went wrong");
                            finish();
                        }
                    });
        }

    }

    private void get_upcoming_booking()
    {
        if(!mActions.checkInternetConnection(Tickets.this))
        {
            mActions.displayToast(Tickets.this, "No Internet Connection");
        }
        else
        {
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonArrayResp("members", prefs.getInt("id", 0) + "/bookings/upcoming", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Tickets.this, "Upcoming Tickets Loaded !");
//                            Log.v(TAG + "UPCOMING TICKETS RESPONSE ", response.toString());

                            jarray_upcoming = response;

                            if(!getIntent().hasExtra("from_profile"))
                            {
                                txt_upcoming.setBackgroundColor(getResources().getColor(R.color.selected_pink));
                                txt_previous.setBackgroundColor(getResources().getColor(R.color.pink1));
                            }

                            set_up_viewpager_upcoming();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Tickets.this, "Something went wrong");
                            finish();
                        }
                    });
        }

    }


    class ViewPagerTicketsAdapter_upcoming extends FragmentStatePagerAdapter {

        public ViewPagerTicketsAdapter_upcoming(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            try {
                return TicketUpcoming.newInstance(jarray_upcoming.getJSONObject(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public int getCount() {
            return jarray_upcoming.length();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    class ViewPagerTicketsAdapter_previous extends FragmentStatePagerAdapter {

        public ViewPagerTicketsAdapter_previous(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            try {
                return TicketPrevious.newInstance(jarray_previous.getJSONObject(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public int getCount() {
            return jarray_previous.length();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }


    public void cancel_upcoming()
    {
        Log.v(TAG, viewpager_upcoming.getCurrentItem()+"");
        int pos = viewpager_upcoming.getCurrentItem() ;

        JSONArray new_jarray = new JSONArray();
        for(int i = 0; i<jarray_upcoming.length();i++)
        {
            if(i!=pos)
            {
                try {
                    new_jarray.put(jarray_upcoming.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        jarray_upcoming = new_jarray ;
        adapter_upcoming.notifyDataSetChanged();

    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Tickets.this))
        {
            mActions.displayToast(Tickets.this, "No Internet Connection");
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
                                new AlertDialog.Builder(Tickets.this)
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
//                            mActions.ProgressDialogHide();
                            mActions.displayToast(Tickets.this, "Error Occurred");
                        }
                    });
        }
    }

}
