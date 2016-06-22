package com.programize.wonderush.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreen extends Activity{

    private myActions mActions = new myActions();
    private ProgressDialog progressDialog ;

    private EditText edit_email ;
    private EditText edit_password ;
    private Button btn_login;
    private TextView txt_head ;
    private TextView txt_forgot;

    private SharedPreferences prefs, prefs_app ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initialize_widgets();

        set_up_font();

        set_up_listeners();

        SharedPreferences.Editor editor = prefs_app.edit();
        editor.putBoolean("old_user", true);
        editor.apply();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);
        prefs_app = getApplicationContext().getSharedPreferences(Definitions.sharedprefname_app, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        btn_login = (Button)findViewById(R.id.loginscreen_button_login);
        edit_email = (EditText)findViewById(R.id.loginscreen_edittext_email);
        edit_password = (EditText)findViewById(R.id.loginscreen_edittext_password);
        txt_head = (TextView)findViewById(R.id.signup4screen_textview_text1);
        txt_forgot = (TextView)findViewById(R.id.loginscreen_text_forget);

        progressDialog = MyProgressDialog.ctor(LoginScreen.this);
        progressDialog.show();
        progressDialog.dismiss();
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        btn_login.setTypeface(tf_Reg);
        edit_email.setTypeface(tf_Reg);
        edit_password.setTypeface(tf_Reg);
        txt_head.setTypeface(tf_Reg);
        txt_forgot.setTypeface(tf_Reg);
    }

    private void set_up_listeners()
    {
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        //CLICK LISTENER
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if ((email.length() == 0) || (password.length() == 0)) {
                    mActions.displayToast(LoginScreen.this, "Please complete all fields");
                } else if (!mActions.isEmailValid(email)) {
                    mActions.displayToast(LoginScreen.this, "No Valid Email");
                } else if (!mActions.checkInternetConnection(LoginScreen.this)) {
                    mActions.displayToast(LoginScreen.this, "No Internet Connection");
                } else {
                    progressDialog.show();

                    JSONObject request = new JSONObject();
                    JSONObject user_info = new JSONObject();

                    try {
                        user_info.put("email", email);
                        user_info.put("password", password);
                        request.put("user", user_info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Log.v(TAG + "LOGIN REQUEST ", request.toString());

                    UserAPI.post_JsonReq_JsonResp("users/sign_in", request,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
//                                    Log.v(TAG + "LOGIN RESPONSE ", response.toString());
                                    progressDialog.dismiss();

                                    store_values(response);

                                    Intent intent = new Intent(LoginScreen.this, Browse.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    mActions.displayToast(LoginScreen.this, "Unsuccessful login");
                                }
                            });
                }
            }
        });
    }

    private void store_values(JSONObject response)
    {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", response.getString("authentication_token"));
            editor.putString("email", response.getString("email"));
            editor.putInt("id", response.getInt("id"));
            if(response.has("user_image"))
            {
                editor.putString("avatar", response.getString("user_image"));
            }
            editor.putString("firstname",response.getString("firstname") );
            editor.putString("last_login", response.getString("created_at"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    public void test()
    {
        // Set up the large red button on the center right side
        // With custom button and content sizes and margins
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_icon, null));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
        lCSubBuilder.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_action_red_selector, null));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        ImageView lcIcon1 = new ImageView(this);
        ImageView lcIcon2 = new ImageView(this);
        ImageView lcIcon3 = new ImageView(this);
        ImageView lcIcon4 = new ImageView(this);
        ImageView lcIcon5 = new ImageView(this);

        lcIcon1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_option1, null));
        lcIcon2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_option2, null));
        lcIcon3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_option3, null));
        lcIcon4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_option4, null));
        lcIcon5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_option5, null));

        // Build another menu with custom options
        final FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(lCSubBuilder.setContentView(lcIcon5, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon4, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon3, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon2, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon1, blueContentParams).build())
                .setRadius(redActionMenuRadius)
                .setStartAngle(10)
                .setEndAngle(-100)
                .attachTo(leftCenterButton)
                .build();



        // Listen menu open and close events to animate the button content view
        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_close, null));
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.menu_icon, null));
            }
        });


        lcIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "TAG");

                leftCenterMenu.close(true);
            }
        });
    }
}
