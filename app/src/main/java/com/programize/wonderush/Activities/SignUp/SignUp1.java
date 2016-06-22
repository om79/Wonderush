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

public class SignUp1 extends Activity {

    private myActions mActions = new myActions();

    private TextView txt1 ;
    private TextView txt2 ;
    private EditText edit_name ;
    private Button button_continue ;

    Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1_new);

        extras = getIntent().getExtras();

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
        txt2 = (TextView)findViewById(R.id.signup4screen_textview_text2);
        edit_name = (EditText)findViewById(R.id.loginscreen_edittext_email);
        button_continue = (Button)findViewById(R.id.signup1screen_button_continue);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);
        txt2.setTypeface(tf);
        button_continue.setTypeface(tf);
        edit_name.setTypeface(tf);
    }

    private void set_up_values()
    {
        txt2.setText(Html.fromHtml("Tell us, what's your <b>name</b>?"));

        if(getIntent().hasExtra("from_profile"))
        {
            edit_name.setText(extras.getString("firstname"));
        }
    }

    private void set_up_listeners()
    {
        //CONTINUE BUTTON CLICK LISTENER
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_name.getText().length() == 0) {
                    mActions.displayToast(SignUp1.this, "Please enter your name");
                } else {

                    //for edit profile
                    if(getIntent().hasExtra("from_profile"))
                    {
                        Intent intent = new Intent(SignUp1.this, SignUp2b.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("name", edit_name.getText().toString());
                        intent.putExtra("from_profile", true);
                        intent.putExtra("id", extras.getString("id"));
                        intent.putExtra("image", extras.getString("image"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                    else
                    {
                        Intent intent = new Intent(SignUp1.this, SignUp2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("name", edit_name.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                }
            }
        });
    }
}
