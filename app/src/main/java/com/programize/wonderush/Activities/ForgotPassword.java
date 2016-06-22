package com.programize.wonderush.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    private myActions mActions = new myActions();
    private ProgressDialog progressDialog ;

    private EditText edit_email ;
    private TextView txt_head ;
    private Button  btn_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialize_widgets();

        set_up_font();

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        //INITIALIZE WIDGETS
        edit_email = (EditText)findViewById(R.id.forgotscreen_edittext_1);
        txt_head = (TextView)findViewById(R.id.forgotscreen_textview_text1);
        btn_forgot = (Button)findViewById(R.id.forgotscreen_button_1);

        progressDialog = MyProgressDialog.ctor(ForgotPassword.this);
        progressDialog.show();
        progressDialog.dismiss();
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);

        edit_email.setTypeface(tf_Reg);
        txt_head.setTypeface(tf_Bold);
        btn_forgot.setTypeface(tf_Reg);
    }

    private void set_up_listeners()
    {
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_email.getText().length() == 0) {
                    mActions.displayToast(ForgotPassword.this, "Enter your email");
                } else if (!mActions.isEmailValid(edit_email.getText().toString())) {
                    mActions.displayToast(ForgotPassword.this, "No valid Email");
                } else {
                    send_email(edit_email.getText().toString());
                }
            }
        });
    }

    private void send_email(String email)
    {
        if(!mActions.checkInternetConnection(ForgotPassword.this))
        {
            mActions.displayToast(ForgotPassword.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put("Content-Type", "application/json");

            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();
            try {
                user.put("email", email);
                request.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserAPI.post_JsonResp("users/password", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            mActions.displayToast(ForgotPassword.this, "Email with instructions sent");
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(ForgotPassword.this, "Something went wrong");
                        }
                    });
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }
}
