package com.programize.wonderush.Activities.Settings;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.InitializeActivity;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Billing2 extends AppCompatActivity {
    private String TAG = "TAG - BILLING2 SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private EditText edit_card_number;
    private EditText edit_card_mm;
    private EditText edit_card_cvc;
    private EditText edit_card_post;
    private EditText edit_coupon;

    private TextView txt_pay ;
    private TextView txt_head ;
    private TextView txt_stripe ;
    private TextView txt_price1 ;
    private TextView txt_price2 ;

    private TextView txt_apply;
    private TextView txt_coupon_details;
    private TextView txt_coupon_details2;

    private Boolean sign_up_completed = false ;

    private String coupon_id = "" ;
    private String referred_by_user = "";

    private ImageView img_camera ;
    private ImageView img_arrow_back ;
    private int MY_SCAN_REQUEST_CODE = 300 ;
    int previous_size = 0 ;
    private int monthly_subscription = -1 ;

    private static FloatingActionMenu mMenu = null  ;

    int permission_id_camera = 600 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing2);

        Bundle extras = getIntent().getExtras();
        sign_up_completed = extras.getBoolean("sign_up_completed");

        initialize_widgets();

        set_up_font();

        set_up_values();

        set_up_listeners();

        if(getIntent().getExtras().containsKey("monthly_subscription"))
        {
            txt_price1.setText("£"+ getIntent().getExtras().getInt("monthly_subscription") +" per month");
        }
        else
        {
            get_settings();
        }


    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        edit_card_number = (EditText)findViewById(R.id.billing2screen_edit_1);
        edit_card_mm = (EditText)findViewById(R.id.billing2screen_edit_2);
        edit_card_cvc = (EditText)findViewById(R.id.billing2screen_edit_4);
        edit_card_post = (EditText)findViewById(R.id.billing2screen_edit_5);
        edit_coupon = (EditText)findViewById(R.id.billing2screen_edit_6);

        txt_pay = (TextView)findViewById(R.id.billing2screen_text_pay);
        txt_price1 = (TextView)findViewById(R.id.billing2screen_text_price1);
        txt_price2 = (TextView)findViewById(R.id.billing2screen_text_price2);
        txt_head = (TextView)findViewById(R.id.billing2screen_textview_head);
        txt_apply = (TextView)findViewById(R.id.billing2screen_text_apply);
        txt_coupon_details = (TextView)findViewById(R.id.billing2screen_text_coupon_details);
        txt_coupon_details2 = (TextView)findViewById(R.id.billing2screen_text_coupon_details2);
        txt_stripe = (TextView)findViewById(R.id.billing2screen_text_stripe);

        img_camera = (ImageView)findViewById(R.id.billing2screen_image_camera);
        img_arrow_back = (ImageView)findViewById(R.id.billing2screen_imageview_backarrow);

        progressDialog = MyProgressDialog.ctor(Billing2.this);
        progressDialog.show();
        progressDialog.dismiss();
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        txt_head.setTypeface(tf_Bold);
        txt_pay.setTypeface(tf_Bold);
        txt_apply.setTypeface(tf_Bold);

        txt_price1.setTypeface(tf_Bold);
        txt_price2.setTypeface(tf_Reg);

        edit_card_number.setTypeface(tf_Reg);
        edit_card_mm.setTypeface(tf_Reg);
        edit_card_cvc.setTypeface(tf_Reg);
        edit_card_post.setTypeface(tf_Reg);
        edit_coupon.setTypeface(tf_Reg);

        txt_coupon_details.setTypeface(tf_Reg);
        txt_coupon_details2.setTypeface(tf_Reg);
        txt_stripe.setTypeface(tf_Reg);
    }

    private void set_up_values()
    {
        if(sign_up_completed)
        {
            txt_head.setText("Billing");
            edit_coupon.setVisibility(View.GONE);
            txt_apply.setVisibility(View.GONE);
            txt_coupon_details.setVisibility(View.GONE);
        }
        else
        {
            txt_head.setText("Billing");
            edit_coupon.setVisibility(View.VISIBLE);
            txt_apply.setVisibility(View.VISIBLE);
            txt_coupon_details.setVisibility(View.VISIBLE);
        }
    }

    private void set_up_listeners()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Billing2.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Billing2.this,
                            new String[]{Manifest.permission.CAMERA},
                            permission_id_camera);
                }
            }
        });

        txt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_coupon.getText().length() != 0) {
                    apply_coupon();
                }
            }
        });

        txt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_stripe_token();
            }
        });


        edit_card_mm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 2 && previous_size == 1) {
                    s.append("/");
                }

                previous_size = s.toString().length();
            }
        });

        edit_coupon.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void add_billing(String stripe_token)
    {
        if(!mActions.checkInternetConnection(Billing2.this))
        {
            mActions.displayToast(Billing2.this, "No Internet Connection");
        }
        else
        {
//            progressDialog.show();

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("user_id", prefs.getInt("id", -1));
                jbody.put("stripeToken", stripe_token);
                jbody.put("postcode", edit_card_post.getText().toString());

                if(!coupon_id.equals(""))
                {
                    jbody.put("coupon_id", coupon_id);
                }
                if(!referred_by_user.equals(""))
                {
                    jbody.put("referred_by_user", referred_by_user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            Log.v(TAG, jbody.toString());

            UserAPI.post_StringResp("members/sign_up_step_2", jbody, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            //Mix Panel set paid = true
                            InitializeActivity.people.set("paid", true);

                            if(getIntent().getExtras().containsKey("from_sign_up"))
                            {
                                Intent intent = new Intent(Billing2.this, Invite.class);
                                intent.putExtra("from_sign_up", getIntent().getExtras().getBoolean("from_sign_up"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else
                            {
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing2.this, "Something went wrong");
                        }
                    });
        }

    }

    private void edit_billing(String stripe_token)
    {
        if(!mActions.checkInternetConnection(Billing2.this))
        {
            mActions.displayToast(Billing2.this, "No Internet Connection");
        }
        else
        {
//            progressDialog.show();

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("stripeToken", stripe_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));

            Log.v(TAG, jbody.toString());

            Map<String,String> params = new HashMap<>();
            params.put("user_id", prefs.getInt("id", -1) + "");

            UserAPI.post_StringResp("charge_cards", jbody, headers, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Billing2.this, "Edit Billing was successful");
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing2.this, "Something went wrong");
                        }
                    });
        }

    }

    private void get_stripe_token()
    {
        if(edit_card_number.getText().toString().length() == 0 ||
                edit_card_mm.getText().toString().length() == 0 ||
                edit_card_cvc.getText().toString().length() == 0 ||
                edit_card_post.getText().toString().length() == 0)
        {
            mActions.displayToast(Billing2.this, "Fill all values");
        }
        else
        {
            progressDialog.show();
            String mm_yy = edit_card_mm.getText().toString();
            String split_date[] = mm_yy.split("/");
            int month = 0;
            int year = 0;

            try {
                if(split_date.length == 2)
                {
                    month = Integer.parseInt(split_date[0]);
                    year = Integer.parseInt("20" +split_date[1] );
                }

            } catch (NumberFormatException e) {
                System.out.println("Wrong number");
                month = 0;
                year = 0 ;
            }

            Card card = new Card(
                    edit_card_number.getText().toString(),
                    month,
                    year,
                    edit_card_cvc.getText().toString());

            Log.v(TAG, card.toString());

            boolean validation = card.validateCard();


            if (validation) {
                new Stripe().createToken(
                        card,
                        Definitions.stripe_id,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                Log.v(TAG, "Token " + token.getId() + "");

                                if(sign_up_completed)
                                {
//                                    progressDialog.dismiss();
                                    edit_billing(token.getId());
                                }
                                else
                                {
//                                    progressDialog.dismiss();
                                    add_billing(token.getId());
                                }

                            }
                            public void onError(Exception error) {
                                Log.v(TAG, "Token " + error.getLocalizedMessage());
                            }
                        });
            } else if (!card.validateNumber()) {
                progressDialog.dismiss();
                mActions.displayToast(Billing2.this, "The card number that you entered is invalid");
            } else if (!card.validateExpiryDate()) {
                progressDialog.dismiss();
                mActions.displayToast(Billing2.this, "The expiration date that you entered is invalid");
            } else if (!card.validateCVC()) {
                progressDialog.dismiss();
                mActions.displayToast(Billing2.this, "The CVC code that you entered is invalid");
            } else {
                progressDialog.dismiss();
                mActions.displayToast(Billing2.this, "The card details that you entered are invalid");
            }
        }
    }

    private void apply_coupon()
    {
        if(!mActions.checkInternetConnection(Billing2.this))
        {
            mActions.displayToast(Billing2.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("invitation_code", edit_coupon.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "Content-Type", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            Log.v(TAG, jbody.toString());

            UserAPI.post_JsonResp("members/apply_coupon", jbody, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Log.v(TAG, response.toString());
//                            mActions.displayToast(Billing2.this, "Applying Coupon was successful");

                            try {
                                coupon_id = response.getString("coupon_id");
                                if(response.has("referred_by_user"))
                                {
                                    referred_by_user = response.getString("referred_by_user");
                                }
                                txt_coupon_details.setText(response.getString("message"));
                                txt_price1.setText(response.getString("line1") );
                                txt_price2.setText(response.getString("line2"));
                                txt_price2.setVisibility(View.VISIBLE);
//                                txt_pay.setText("Join now");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            get_stripe_token();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            coupon_id = "" ;
                            referred_by_user = "";
                            txt_coupon_details.setText("Invalid Coupon");
                            txt_coupon_details2.setText("");
                            txt_price2.setText("");
                            txt_price2.setVisibility(View.GONE);
                            if(getIntent().getExtras().containsKey("monthly_subscription"))
                            {
//                                txt_pay.setText("Pay £"+ getIntent().getExtras().getInt("monthly_subscription") +" per month");
                                txt_price1.setText("£"+ getIntent().getExtras().getInt("monthly_subscription") +" per month");
                            }
                            else
                            {
//                                txt_pay.setText("Pay £"+ monthly_subscription +" per month");
                                txt_price1.setText("£"+ monthly_subscription +" per month");
                            }
                            mActions.displayToast(Billing2.this, "Something went wrong");
                            mActions.displayToast(Billing2.this, "Invalid Coupon");
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        startService(new Intent(getApplication(), ChatHeadService.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                edit_card_number.setText(scanResult.cardNumber);
            }
            else {
                Log.v(TAG,"Scan was canceled." );
            }
        }
    }

    private void get_settings()
    {
        if(!mActions.checkInternetConnection(Billing2.this))
        {
            mActions.displayToast(Billing2.this, "No Internet Connection");
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

                            try {
                                monthly_subscription = response.getInt("monthly_subscription");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            txt_pay.setText("Pay £"+ monthly_subscription +" per month");
                            txt_price1.setText("£"+ monthly_subscription +" per month");


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Billing2.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 600: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent scanIntent = new Intent(Billing2.this, CardIOActivity.class);
                    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
