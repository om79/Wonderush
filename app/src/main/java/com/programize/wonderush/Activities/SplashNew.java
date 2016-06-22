package com.programize.wonderush.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.Activities.SignUp.SignUpIntro;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashNew extends AppCompatActivity {

    private TextView txt_login, txt1, txt2, txt_get_started;
    private VideoView videoView ;
    private SharedPreferences prefs_app ;
    private Boolean old_user ;
    private String coupon ;
    private String discount ;

    private String experience_from_URI = "" ;

    private myActions mActions = new myActions();
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();
        Uri data = getIntent().getData();
        if(Intent.ACTION_VIEW.equals(action) && data != null)
        {
            Log.v("TAG data", data.toString());

            try {
                Log.v("TAG data 2 ", data.getQueryParameter("experience"));
                experience_from_URI = data.getQueryParameter("experience") ;
            }catch (NullPointerException e)
            {
                Log.e("TAG error",e.toString());
            }
        }


        pre_initialiazes();

        setContentView(R.layout.activity_splash_new);

        initialize_widgets();

        show_video();

        set_up_font();

        set_up_listeners();

        txt_login.setText(Html.fromHtml("Already a member ? <b>Login</b>"));

    }


    private void initialize_widgets()
    {
        prefs_app = getApplicationContext().getSharedPreferences(Definitions.sharedprefname_app, MODE_PRIVATE);
        old_user = true ;
        coupon = "TEST_COUPON" ;
        discount = "XX";

        progressDialog = MyProgressDialog.ctor(SplashNew.this);
        progressDialog.show();
        progressDialog.dismiss();

        txt_login = (TextView) findViewById(R.id.splashscreen_txt_login);
        txt1 = (TextView) findViewById(R.id.splashscreen_txt1);
        txt2 = (TextView) findViewById(R.id.splashscreen_txt2);
        txt_get_started = (TextView) findViewById(R.id.splashscreen_txt_getstarted);
        videoView = (VideoView) findViewById(R.id.splashscreen_video);
    }

    private void set_up_font()
    {
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        String fontPath_bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf_bold = Typeface.createFromAsset(getAssets(), fontPath_bold);
        txt_login.setTypeface(tf);
        txt1.setTypeface(tf);
        txt2.setTypeface(tf_bold);
        txt_get_started.setTypeface(tf_bold);
    }

    private void set_up_listeners()
    {
        //LOGIN BUTTON CLICK LISTENER
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashNew.this, LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        //SIGN UP BUTTON CLICK LISTENER
        txt_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashNew.this, SignUpIntro.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("old_user", old_user);
                intent.putExtra("coupon", coupon);
                intent.putExtra("discount", discount);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void pre_initialiazes()
    {
//        Definitions.initializeApplication(getApplicationContext());
        //SET UP SHARED PREFERENCES
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);
        if(prefs.contains("token"))
        {
            Intent intent = new Intent(getApplicationContext(), Browse.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if(!experience_from_URI.equals(""))
            {
                intent.putExtra("experience_from_URI", experience_from_URI);
            }
            startActivity(intent);
            finish();
        }
    }

    private void show_video()
    {
        String uriPath = "android.resource://com.programize.wonderush/"+R.raw.intro_video_big;

        Uri uri = Uri.parse(uriPath);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();

//        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(videoView);
//        Glide.with(this).load(R.raw.intro_video).into(imageViewTarget);
    }

    private void check_for_coupon()
    {
        if(prefs_app.contains("old_user"))
        {
            old_user = true ;
            txt1.setVisibility(View.INVISIBLE);
        }
        else
        {
            old_user = false ;
            txt1.setVisibility(View.VISIBLE);
            coupon_request();
        }

    }

    private void coupon_request()
    {
        if(!mActions.checkInternetConnection(SplashNew.this))
        {
            old_user = true ;
            mActions.displayToast(SplashNew.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );

            UserAPI.get_JsonObjResp("plans/onboard", headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("TAG response", response.toString());
                            try {
                                coupon = response.getString("coupon_id");
                                discount = response.getString("amount");
                                discount = discount.replace(".0", "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            txt1.setText(Html.fromHtml("Get your first month for £" + discount +"<br>with code <b>" + coupon + "</b>"));
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(SplashNew.this, "Something went wrong");
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();

        check_for_coupon();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        progressDialog.dismiss();
    }



}
