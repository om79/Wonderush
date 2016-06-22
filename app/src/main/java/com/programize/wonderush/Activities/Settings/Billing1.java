package com.programize.wonderush.Activities.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.SignUp.SignUp4;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Billing1 extends AppCompatActivity {

    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView txt_head;
    private TextView txt_cancel;
    private TextView txt_skip;
    private TextView txt_1;
    private TextView txt_2;
    private TextView txt_3;

    private RelativeLayout rl_head ;
    private View view_divider ;

    private ImageView img_arrow_back ;

    private LinearLayout layout_credit ;

    private Boolean sign_up_completed = false ;
    private Boolean can_cancel = false ;
    private Boolean can_reactivate = false ;
    private Boolean can_renew = false ;

    private int monthly_price = -1 ;

    private static FloatingActionMenu mMenu = null  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing1);

        initialize_widgets();

        set_up_font();

        set_up_listeners();

        if(getIntent().getExtras().containsKey("from_sign_up"))
        {
            txt_skip.setVisibility(View.VISIBLE);
            rl_head.setVisibility(View.INVISIBLE);
            view_divider.setVisibility(View.INVISIBLE);
        }
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt_head = (TextView)findViewById(R.id.billing1screen_textview_head);
//        txt_credit = (TextView)findViewById(R.id.billing1screen_text_credit);
        txt_cancel = (TextView)findViewById(R.id.billing1screen_text_cancel);
        txt_skip = (TextView)findViewById(R.id.billing1screen_text_skip);
        txt_1 = (TextView)findViewById(R.id.billing1screen_textview_1);
        txt_2 = (TextView)findViewById(R.id.billing1screen_textview_2);
        txt_3 = (TextView)findViewById(R.id.billing1screen_textview_3);

        img_arrow_back = (ImageView)findViewById(R.id.billing1screen_imageview_backarrow);

        layout_credit = (LinearLayout)findViewById(R.id.billing1screen_linear_credit);

        rl_head = (RelativeLayout) findViewById(R.id.billing1screen_linear_head);
        view_divider = findViewById(R.id.billing1screen_view_divider1) ;

        progressDialog = MyProgressDialog.ctor(Billing1.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_1.setTypeface(tf_Reg);
        txt_2.setTypeface(tf_Reg);
        txt_3.setTypeface(tf_Reg);
        txt_cancel.setTypeface(tf_Reg);
        txt_skip.setTypeface(tf_Reg);
    }

    private void set_up_listeners()
    {
        layout_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Billing1.this, Billing2.class);
                intent.putExtra("sign_up_completed", sign_up_completed);
                if(getIntent().getExtras().containsKey("from_sign_up"))
                {
                    intent.putExtra("from_sign_up", getIntent().getExtras().getBoolean("from_sign_up"));
                }
                intent.putExtra("monthly_subscription", monthly_price);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("billing", "add");
                startActivity(intent);
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (can_cancel)
                {
                    cancel_billing();
                }
                else if (can_reactivate)
                {
                    reactivate_billing();
                }
                else if (can_renew)
                {
                    renew_billing();
                }

            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Billing1.this, SignUp4.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getExtras().containsKey("from_sign_up"))
                {
//                    Intent intent = new Intent(Billing1.this, Browse.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();

                    Intent intent = new Intent(Billing1.this, SignUp4.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!getIntent().getExtras().containsKey("from_sign_up"))
        {
            if(mMenu!=null) {
                mMenu.myTerminateFunc(true);
            }

            mMenu = mActions.set_up_menu(this);
        }

        get_settings();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!getIntent().getExtras().containsKey("from_sign_up"))
        {
            if(mMenu!=null) {
                mMenu.myTerminateFunc(true);
            }
        }
        progressDialog.dismiss();
    }

    private void get_settings()
    {
        if(!mActions.checkInternetConnection(Billing1.this))
        {
            mActions.displayToast(Billing1.this, "No Internet Connection");
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
                            Log.v("TAG asd", response.toString());

                            set_up_values(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void set_up_values(JSONObject response)
    {
        try {
            sign_up_completed = response.getBoolean("has_completed_sign_up");
            can_cancel = response.getBoolean("can_cancel");
            can_reactivate = response.getBoolean("can_reactivate");

            monthly_price = response.getInt("monthly_subscription");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (can_cancel)
        {
            txt_cancel.setVisibility(View.VISIBLE);
            txt_cancel.setText("CANCEL SUBSCRIPTION");
        }
        else if(can_reactivate)
        {
            txt_cancel.setVisibility(View.VISIBLE);
            txt_cancel.setText("REACTIVATE SUBSCRIPTION");
        }
        else if(can_renew)
        {
            txt_cancel.setVisibility(View.VISIBLE);
            txt_cancel.setText("RENEW SUBSCRIPTION");
        }
        else
        {
            txt_cancel.setVisibility(View.GONE);
        }
    }

    private void cancel_billing()
    {
        if(!mActions.checkInternetConnection(Billing1.this))
        {
            mActions.displayToast(Billing1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_StringResp("members/" + prefs.getInt("id", -1) + "/cancel_subscription",null,  headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Billing1.this, "Cancel Subscription was successful!");

                            get_settings();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void reactivate_billing()
    {
        if(!mActions.checkInternetConnection(Billing1.this))
        {
            mActions.displayToast(Billing1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_StringResp("members/" + prefs.getInt("id", -1) + "/reactivate_subscription", null, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Billing1.this, "Reactivate Subscription was successful!");

                            get_settings();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void renew_billing()
    {
        if(!mActions.checkInternetConnection(Billing1.this))
        {
            mActions.displayToast(Billing1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_StringResp("members/" + prefs.getInt("id", -1) + "/renew_subscription", null, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            get_settings();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {

        if(getIntent().getExtras().containsKey("from_sign_up"))
        {
            Intent intent = new Intent(Billing1.this, SignUp4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            super.onBackPressed();
        }

    }
}
