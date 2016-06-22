package com.programize.wonderush.Activities.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.R;

public class SignUp4 extends Activity {

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private Button btn1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4_new);


        initialize_widgets();

        set_up_font();

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        //INITIALIZE WIDGETS
        txt1 = (TextView)findViewById(R.id.signup4screen_textview_text1);
        txt2 = (TextView)findViewById(R.id.signup4screen_textview_text2);
        txt3 = (TextView)findViewById(R.id.signup4screen_textview_text3);
        btn1 = (Button)findViewById(R.id.signup4screen_button_continue);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt1.setTypeface(tf_Bold);
        txt2.setTypeface(tf);
        txt3.setTypeface(tf);
        btn1.setTypeface(tf);
    }

    private void set_up_listeners()
    {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp4.this, Browse.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }
}
