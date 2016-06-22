package com.programize.wonderush.Activities.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Functions.myActions;

public class SignUp2b extends Activity {

    private myActions mActions = new myActions();

    private TextView txt1 ;
    private TextView txt2 ;
    private TextView txt_skip ;
    private EditText edit_email ;
    private EditText edit_password ;
    private Button button_continue ;

    private Bundle extras ;
    private String name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2b_new);

        //TAKE VALUES FROM PREVIOUS SCREEN
        extras = getIntent().getExtras();
        name = extras.getString("name");


        //SCREEN STARTS WITH THE KEYBOARD HIDDEN
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initialize_widgets();

        set_up_font();

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        //INITIALIZE WIDGETS
        txt1 = (TextView)findViewById(R.id.signup4screen_textview_text1);
        txt2 = (TextView)findViewById(R.id.signup2screen_textview_text2);
        edit_email = (EditText)findViewById(R.id.loginscreen_edittext_email);
        edit_password = (EditText)findViewById(R.id.loginscreen_edittext_password);
        button_continue = (Button)findViewById(R.id.signup2bscreen_button_continue);
        txt_skip = (TextView)findViewById(R.id.signup2bscreen_text_skip);

        txt1.setText(Html.fromHtml("Would you like to change your <b>password</b>?"));
        txt2.setText(Html.fromHtml("Confirm the new <b>Password</b>"));
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);
        txt2.setTypeface(tf);
        txt_skip.setTypeface(tf);
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
                    mActions.displayToast(SignUp2b.this, "Please enter all values");
                } else {
                    Intent intent = new Intent(SignUp2b.this, SignUp3.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("name", name);
                    intent.putExtra("password", edit_email.getText().toString());
                    intent.putExtra("confirm_password", edit_password.getText().toString());
                    intent.putExtra("from_profile", true);
                    intent.putExtra("id", extras.getString("id"));
                    intent.putExtra("image", extras.getString("image"));

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp2b.this, SignUp3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", name);
                intent.putExtra("from_profile", true);
                intent.putExtra("id", extras.getString("id"));
                intent.putExtra("image", extras.getString("image"));

                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }
}
