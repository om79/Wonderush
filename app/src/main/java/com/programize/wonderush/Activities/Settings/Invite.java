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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.SignUp.SignUp4;
import com.programize.wonderush.Fragments.ShareInvite;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Invite extends AppCompatActivity {

    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private TextView txt_head ;
    private TextView txt_1;
    private TextView txt_2;
    private TextView txt_3;
    private TextView txt_referal;
    private TextView txt_share;

    private RelativeLayout rl_head;
    private TextView txt_skip ;

    private ImageView img_arrow_back ;

    private String referal ="";
    private int discount = 10;
    private int first_month_subscription = 5;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initialize_widgets();

        set_up_font();

        set_up_listeneres();

        if(getIntent().getExtras().containsKey("from_sign_up"))
        {
            txt_skip.setVisibility(View.VISIBLE);
            rl_head.setVisibility(View.INVISIBLE);

            get_settings();
        }
        else
        {
            txt_referal.setText(getIntent().getExtras().getString("referal"));
            txt_2.setText("Discount-loving social type? Get £" + getIntent().getExtras().getInt("discount") + " off for every friend who uses your referral code, plus they get first month for just £" + getIntent().getExtras().getInt("first_month_subscription") +"!");
        }

    }

    private void initialize_widgets()
    {
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        progressDialog = MyProgressDialog.ctor(Invite.this);
        progressDialog.show();
        progressDialog.dismiss();

        txt_head = (TextView)findViewById(R.id.invitescreen_textview_head);
        txt_1 = (TextView)findViewById(R.id.invitescreen_text_1);
        txt_2 = (TextView)findViewById(R.id.invitescreen_text_2);
        txt_3 = (TextView)findViewById(R.id.invitescreen_text_3);
        txt_referal = (TextView)findViewById(R.id.invitescreen_text_referal);
        txt_share = (TextView)findViewById(R.id.invitescreen_text_share);

        rl_head = (RelativeLayout)findViewById(R.id.invitescreen_linear_head);
        txt_skip = (TextView)findViewById(R.id.invitescreen_text_skip) ;

        img_arrow_back = (ImageView)findViewById(R.id.invitescreen_imageview_backarrow);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_1.setTypeface(tf_Bold);
        txt_2.setTypeface(tf_Reg);
        txt_3.setTypeface(tf_Reg);
        txt_referal.setTypeface(tf_Bold);
        txt_share.setTypeface(tf_Reg);
        txt_skip.setTypeface(tf_Reg);
    }

    private void set_up_listeneres()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getExtras().containsKey("from_sign_up")) {
                    ShareInvite share_info = ShareInvite.newInstance(referal, first_month_subscription);
                    share_info.show(getFragmentManager(), "Sample Fragment");
                } else
                {
                    ShareInvite share_info = ShareInvite.newInstance(getIntent().getExtras().getString("referal"), getIntent().getExtras().getInt("first_month_subscription"));
                    share_info.show(getFragmentManager(), "Sample Fragment");
                }
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Invite.this, SignUp4.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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


    @Override
    public void onBackPressed() {

        if(getIntent().getExtras().containsKey("from_sign_up"))
        {
            Intent intent = new Intent(Invite.this, SignUp4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void get_settings()
    {
        if(!mActions.checkInternetConnection(Invite.this))
        {
            mActions.displayToast(Invite.this, "No Internet Connection");
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


                            try {
                                if(response.has("invitation_code"))
                                {
                                    referal = response.getString("invitation_code");
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

                            txt_referal.setText(referal);
                            txt_2.setText("Discount-loving social type? Get £" + discount + " off for every friend who uses your referral code, plus they get first month for just £" + first_month_subscription + "!");


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Invite.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

}
