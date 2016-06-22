package com.programize.wonderush.Activities.Settings;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.InitializeActivity;
import com.programize.wonderush.Activities.SplashNew;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Fragments.WebViewFragment;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Settings1 extends Activity {

//    private String TAG = "TAG - SETTINGS SCREEN - " ;
    private myActions mActions = new myActions();
    private ProgressDialog progressDialog ;

    TextView settings ;
    TextView notifications ;
    TextView receive_notifications ;
    TextView billing ;
    TextView about ;
    TextView terms ;
    TextView policy ;
    TextView support ;
    TextView bug ;
    TextView version ;
    TextView invite ;
    TextView billingDetails;

    Button btn_logout ;

    private String avatar ;

    ToggleButton toggle_push_notification ;

    ImageView img_invite ;
    ImageView img_billingDetails;

    private String referal ="";
    private int discount = 10;
    private int first_month_subscription = 5;
    private int monthly_subscription = -1;

    private SharedPreferences prefs ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings1);

        initialize_widgets();

        set_up_font();

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        settings = (TextView)findViewById(R.id.settings1screen_textview_head);
        notifications = (TextView)findViewById(R.id.settings1screen_textview_notifications);
        receive_notifications = (TextView)findViewById(R.id.settings1screen_textview_receivenotifications);
        billing = (TextView)findViewById(R.id.settings1screen_textview_billing);
        billingDetails = (TextView)findViewById(R.id.settings1screen_text_billingDetails);
        about = (TextView)findViewById(R.id.settings1screen_textview_about);
        terms = (TextView)findViewById(R.id.settings1screen_textview_terms);
        policy = (TextView)findViewById(R.id.settings1screen_textview_policy);
        support = (TextView)findViewById(R.id.settings1screen_textview_support);
        bug = (TextView)findViewById(R.id.settings1screen_textview_bug);
        version = (TextView)findViewById(R.id.settings1screen_textview_version);
        invite = (TextView)findViewById(R.id.settings1screen_text_invite);

        btn_logout = (Button)findViewById(R.id.settings1screen_button_logout);

        toggle_push_notification = (ToggleButton)findViewById(R.id.settings1screen_swich_notifications);

        img_invite = (ImageView)findViewById(R.id.settings1screen_image_invite);
        img_billingDetails = (ImageView)findViewById(R.id.settings1screen_image_billingDetails);

        progressDialog = MyProgressDialog.ctor(Settings1.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        settings.setTypeface(tf_Bold);
        notifications.setTypeface(tf_Bold);
        receive_notifications.setTypeface(tf_Reg);
        billing.setTypeface(tf_Bold);
        billingDetails.setTypeface(tf_Reg);
        about.setTypeface(tf_Bold);
        terms.setTypeface(tf_Reg);
        policy.setTypeface(tf_Reg);
        support.setTypeface(tf_Reg);
        bug.setTypeface(tf_Reg);
        version.setTypeface(tf_Reg);
        invite.setTypeface(tf_Reg);
        btn_logout.setTypeface(tf_Reg);
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

        if(!mActions.checkInternetConnection(Settings1.this))
        {
            mActions.displayToast(Settings1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_JsonObjResp("user_settings_set", headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Log.v("TAG settings", response.toString());

                            try {
                                if(response.has("invitation_code"))
                                {
                                    referal = response.getString("invitation_code");
                                }

                                if(!response.isNull("monthly_subscription"))
                                {
                                    monthly_subscription = response.getInt("monthly_subscription");
                                }

                                if(!response.isNull("discount"))
                                {
                                    discount = response.getInt("discount");
                                }

                                if(!response.isNull("first_month_subscription"))
                                {
                                    first_month_subscription = response.getInt("first_month_subscription");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            //SETTING VALUES
                            try {
                                if (response.getBoolean("receive_push_notifications"))
                                {
                                    toggle_push_notification.setChecked(true);
                                }
                                else
                                {
                                    toggle_push_notification.setChecked(false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            //TOGGLE LISTENER FOR PUSH NOTIFICATIONS
                            toggle_push_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    JSONObject settings = new JSONObject();
                                    try {
                                        settings.put("receive_push_notifications", isChecked);
                                        change_settings(1, settings);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Settings1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void change_settings(final int type, final JSONObject settings)
    {
        if(!mActions.checkInternetConnection(Settings1.this))
        {
            mActions.displayToast(Settings1.this, "No Internet Connection");

            if(type == 1 )
            {
                toggle_push_notification.toggle();
            }
        }
        else
        {
            progressDialog.show();

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("user_settings_set", settings);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "Content-Type", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));


            UserAPI.put_StringResp("user_settings_set", jbody, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            //Mix Panel change notification status
                            try {
                                InitializeActivity.people.set("notifications", settings.getBoolean("receive_push_notifications"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Settings1.this, "Something went wrong");

                            if (type == 1) {
                                toggle_push_notification.toggle();
                            }
                        }
                    });
        }
    }

    private void logout()
    {
        if(!mActions.checkInternetConnection(this))
        {
            mActions.displayToast(this, "No Internet Connection");
        }
        else {
            progressDialog.show();

            //Define Headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));


            UserAPI.delete_StringResp("users/sign_out", headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            InitializeActivity.people.clearPushRegistrationId();

                            prefs.edit().clear().apply();
                            Intent intent = new Intent(Settings1.this, SplashNew.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Settings1.this, "Something went wrong");
                        }
                    });
        }
    }

    private void set_up_listeners()
    {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        billingDetails.setOnClickListener(listener_billing);
        img_billingDetails.setOnClickListener(listener_billing);

        invite.setOnClickListener(listener_invite);
        img_invite.setOnClickListener(listener_invite);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                WebViewFragment newFragment = WebViewFragment.newInstance("https://wonderush.com/terms");
                newFragment.setStyle(WebViewFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
                newFragment.show(ft, "Terms");
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                WebViewFragment newFragment = WebViewFragment.newInstance("https://wonderush.com/privacy");
                newFragment.setStyle(WebViewFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
                newFragment.show(ft, "Terms");
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "hello@wonderush.com", null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "hello@wonderush.com", null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report a bug");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Settings1.this))
        {
            mActions.displayToast(Settings1.this, "No Internet Connection");
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

//            Log.v(TAG, prefs.getString("last_login", null));
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
                                new AlertDialog.Builder(Settings1.this)
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
                            mActions.displayToast(Settings1.this, "Something went wrong");
                        }
                    });
        }
    }

    View.OnClickListener listener_invite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(referal.length()==0)
            {
                mActions.displayToast(Settings1.this, "No active subscription");
            }
            else
            {
                Intent intent = new Intent(Settings1.this, Invite.class);
                intent.putExtra("referal",referal);
                intent.putExtra("first_month_subscription",first_month_subscription);
                intent.putExtra("discount",discount);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
    };

    View.OnClickListener listener_billing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings1.this, Billing1.class);
            intent.putExtra("monthly_subscription",monthly_subscription);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
}
