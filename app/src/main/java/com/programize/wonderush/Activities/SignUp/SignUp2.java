package com.programize.wonderush.Activities.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Functions.myActions;

public class SignUp2 extends Activity {

    private myActions mActions = new myActions();

    private TextView txt1 ;
    private TextView txt2 ;
    private TextView txt3 ;
    private EditText edit_email ;
    private EditText edit_password ;
    private Button button_continue ;

    private String name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2_new);

        //TAKE VALUES FROM PREVIOUS SCREEN
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");


        //SCREEN STARTS WITH THE KEYBOARD HIDDEN
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initialize_widgets();

        set_up_font();

        set_up_values();

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        //INITIALIZE WIDGETS
        txt1 = (TextView)findViewById(R.id.signup4screen_textview_text1);
        txt2 = (TextView)findViewById(R.id.signup2screen_textview_text2);
        txt3 = (TextView)findViewById(R.id.signup2screen_textview_text3);
        edit_email = (EditText)findViewById(R.id.loginscreen_edittext_email);
        edit_password = (EditText)findViewById(R.id.loginscreen_edittext_password);
        button_continue = (Button)findViewById(R.id.signup2screen_button_continue);
    }

    private void set_up_values()
    {
        txt1.setText(Html.fromHtml("What's your <b>e-mail address</b>?"));
        txt2.setText(Html.fromHtml("You should probably pick"));
        txt3.setText(Html.fromHtml("a <b>password</b> too"));
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);
        txt2.setTypeface(tf);
        button_continue.setTypeface(tf);
        edit_email.setTypeface(tf);
        edit_password.setTypeface(tf);
    }

    private void set_up_listeners()
    {
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_email.getText().length() == 0 || edit_password.getText().length() == 0) {
                    mActions.displayToast(SignUp2.this, "Please enter all values");
                } else if (!mActions.isEmailValid(edit_email.getText().toString())) {
                    mActions.displayToast(SignUp2.this, "No valid Email");
                } else {
                    Intent intent = new Intent(SignUp2.this, SignUp3.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("name", name);
                    intent.putExtra("email", edit_email.getText().toString());
                    intent.putExtra("password", edit_password.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

            }
        });
    }

}
