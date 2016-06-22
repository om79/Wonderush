package com.programize.wonderush.Activities.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.Booking.Tickets;
import com.programize.wonderush.Activities.Browsing.Badges;
import com.programize.wonderush.Activities.Browsing.Buckets;
import com.programize.wonderush.Activities.SignUp.SignUp1;
import com.programize.wonderush.Adapters.TimelineAdapter;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Models.TimelineInfo;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile1 extends Activity {

    private String TAG = "TAG - PROFILE SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView txt_head ;
    private TextView txt_username ;
    private TextView txt_location ;
    private TextView txt_experiences ;
    private TextView txt_badges ;
    private TextView txt_bucket ;
    private TextView txt_num_xp ;
    private TextView txt_num_badges ;
    private TextView txt_num_bucket ;

    private ImageView img_big ;
    private ImageView img_small ;
    private ImageView img_small_back ;
    private ImageView img_edit ;

    private String avatar ;

    private static FloatingActionMenu mMenu = null  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_listeners();

        searchProfile();
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

        searchProfile_resume();

        get_unreviewed_events();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZES
        txt_head = (TextView)findViewById(R.id.profile1screen_textview_head);
        txt_username = (TextView)findViewById(R.id.profile1screen_textview_username);
        txt_location = (TextView)findViewById(R.id.profile1screen_textview_location);
        txt_experiences = (TextView)findViewById(R.id.profile1screen_textview_experiences);
        txt_badges = (TextView)findViewById(R.id.profile1screen_textview_badges);
        txt_bucket = (TextView)findViewById(R.id.profile1screen_textview_bucket);
        txt_num_xp = (TextView)findViewById(R.id.profile1screen_textview_number_xp);
        txt_num_badges = (TextView)findViewById(R.id.profile1screen_textview_num_badges);
        txt_num_bucket = (TextView)findViewById(R.id.profile1screen_textview_num_bucket);

        img_big = (ImageView)findViewById(R.id.profile1screen_image_bigimage);
        img_small = (ImageView)findViewById(R.id.profile1screen_image_smallimage);
        img_small_back = (ImageView)findViewById(R.id.profile1screen_image_smallimage_back);
        img_edit = (ImageView)findViewById(R.id.profile1screen_imageview_edit);

        progressDialog = MyProgressDialog.ctor(Profile1.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_username.setTypeface(tf_Bold);
        txt_location.setTypeface(tf);
        txt_experiences.setTypeface(tf_Bold);
        txt_badges.setTypeface(tf_Bold);
        txt_bucket.setTypeface(tf_Bold);
        txt_num_xp.setTypeface(tf);
        txt_num_badges.setTypeface(tf);
        txt_num_bucket.setTypeface(tf);
    }

    private void set_up_images()
    {
        img_small_back.setImageResource(R.drawable.no_pic);
    }

    private void initialize_recyclerView(JSONArray response)
    {
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        ArrayList<TimelineInfo> myDataset = new ArrayList<>();

        for(int i=0;i<response.length();i++)
        {
            try {
                myDataset.add(new TimelineInfo(response.getJSONObject(i).getString("id"),response.getJSONObject(i).getString("type"), response.getJSONObject(i).getString("image"), response.getJSONObject(i).getString("text"), response.getJSONObject(i).getString("date")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.profile1screen_recycler_timeline);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TimelineAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void set_up_listeners()
    {
        //REDIRECT TO BUCKET SCREEN
        txt_num_bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile1.this, Buckets.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                finish();
            }
        });

        //REDIRECT TO TICKETS SCREEN
        txt_num_xp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile1.this, Tickets.class);
                intent.putExtra("from_profile", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                finish();
            }
        });
    }

    private void set_up_edit_profile_listener(final JSONObject response)
    {
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile1.this, SignUp1.class);
                intent.putExtra("from_profile", true);
                try {
                    intent.putExtra("id", response.getString("id"));
                    intent.putExtra("firstname", response.getString("firstname"));
                    if(response.has("user_image"))
                    {
                        intent.putExtra("image",response.getString("user_image"));
                    }
                    else
                    {
                        intent.putExtra("image","");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //REDIRECT TO BADGES SCREEN
        txt_num_badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile1.this, Badges.class);
                try {
                    intent.putExtra("id", response.getString("id"));
                    intent.putExtra("firstname", response.getString("firstname"));
                    if(response.has("user_image"))
                    {
                        intent.putExtra("image",response.getString("user_image"));
                    }
                    else
                    {
                        intent.putExtra("image","");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void searchProfile()
    {
        if(!mActions.checkInternetConnection(Profile1.this))
        {
            mActions.displayToast(Profile1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonObjResp("members", prefs.getInt("id", 0) + "/editprofile", headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                SharedPreferences.Editor editor = prefs.edit();
                                if(response.has("user_image"))
                                {
                                    editor.putString("avatar",response.getString("user_image"));
                                }
                                editor.putString("firstname",response.getString("firstname") );

                                editor.apply();

                                set_up_edit_profile_listener(response);
                                set_up_values(response);
                                initialize_recyclerView(response.getJSONArray("timeline"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Profile1.this, "Something went wrong");

                            Log.v("TAG lalalalla", error + "");
                        }
                    });
        }
    }

    private void set_up_values(JSONObject response) throws JSONException {
        txt_head.setText(response.getString("firstname")  );
        txt_username.setText(response.getString("firstname") );
        txt_location.setText("London, UK");

        txt_num_badges.setText(response.getString("number_of_badges"));
        txt_num_bucket.setText(response.getString("number_of_bucket_listed"));
        txt_num_xp.setText(response.getString("number_of_experiences"));

        if(response.has("user_image"))
        {
//            Picasso.with(this).load(Definitions.APIdomain + response.getString("user_image")).fit().centerCrop().into(img_big);
//            Picasso.with(this).load(Definitions.APIdomain + response.getString("user_image")).fit().centerCrop().into(img_small);

            Glide.with(getApplicationContext()).load(Definitions.APIdomain + response.getString("user_image")).centerCrop().into(img_big);
            Glide.with(getApplicationContext()).load(Definitions.APIdomain + response.getString("user_image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(img_small) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img_small.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        else
        {
//            Picasso.with(this).load(R.drawable.male_avatar).resize(300,300).into(img_big);
//            Picasso.with(this).load(R.drawable.male_avatar).resize(300,300).into(img_small);

            Glide.with(getApplicationContext()).load(R.drawable.male_avatar).into(img_big);
//            Glide.with(this).load(R.drawable.male_avatar).into(img_small);
            Glide.with(getApplicationContext()).load(R.drawable.male_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(img_small) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img_small.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Profile1.this))
        {
            mActions.displayToast(Profile1.this, "No Internet Connection");
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
                                new AlertDialog.Builder(Profile1.this)
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
                            mActions.displayToast(Profile1.this, "Error Occurred");
                        }
                    });
        }
    }

    private void searchProfile_resume()
    {
        if(!mActions.checkInternetConnection(Profile1.this))
        {
            mActions.displayToast(Profile1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put("X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonObjResp("members", prefs.getInt("id", 0) + "/editprofile", headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                SharedPreferences.Editor editor = prefs.edit();
                                if (response.has("user_image")) {
                                    editor.putString("avatar", response.getString("user_image"));
                                }
                                editor.putString("firstname", response.getString("firstname"));

                                editor.apply();

                                set_up_edit_profile_listener(response);
                                set_up_values(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Profile1.this, "Something went wrong");
                        }
                    });
        }
    }

//    @Override
//    protected void onDestroy() {
//        Glide.with(getApplicationContext()).pauseRequests();
//        super.onDestroy();
//    }
}
