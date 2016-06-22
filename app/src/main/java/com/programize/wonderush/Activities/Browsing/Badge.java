package com.programize.wonderush.Activities.Browsing;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.myActions;

public class Badge extends AppCompatActivity {

    private ImageView img_head ;
    private ImageView img_arrow_back;
    private TextView txt_head ;
    private TextView txt_title ;
    private TextView txt_description ;

    private String id = null ;
    private String image = null ;

    private myActions mActions = new myActions();
    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        image = extras.getString("image");

        initialize_widgets();

        set_up_font();

        set_up_values();

        set_up_listeners();

    }

    private void initialize_widgets()
    {
        img_arrow_back = (ImageView)findViewById(R.id.badgescreen_imageview_back);
        img_head = (ImageView)findViewById(R.id.badgescreen_image_large);

        txt_head = (TextView)findViewById(R.id.badgescreen_textview_head);
        txt_title = (TextView)findViewById(R.id.badgescreen_text_title);
        txt_description = (TextView)findViewById(R.id.badgescreen_text_description);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_title.setTypeface(tf_Bold);
        txt_description.setTypeface(tf_Reg);
    }

    private void set_up_values()
    {
//        Picasso.with(this).load(Definitions.APIdomain +image).resize(300, 300).onlyScaleDown().memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).into(img_head);
//        Glide.with(this).load(Definitions.APIdomain + image).into(img_head);

        Glide.with(this).load(Definitions.APIdomain + image).asBitmap().centerCrop().into(new BitmapImageViewTarget(img_head) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img_head.setImageDrawable(circularBitmapDrawable);
            }
        });

        switch (id)
        {
            case "1":
                txt_title.setText("Well, hello!".toUpperCase());
                txt_head.setText("Well, hello!");
                txt_description.setText("Signed up to Wonderush");
                break ;
            case "2":
                txt_title.setText("The First Step".toUpperCase());
                txt_head.setText("The First Step");
                txt_description.setText("Make first booking");
                break ;
            case "3":
                txt_title.setText("The Second Step".toUpperCase());
                txt_head.setText("The Second Step");
                txt_description.setText("Completed your first booking");
                break ;
            case "4":
                txt_title.setText("Cardio Crusher".toUpperCase());
                txt_head.setText("Cardio Crusher");
                txt_description.setText("You’ve booked three exercise classes. Amazing.");
                break ;
            case "5":
                txt_title.setText("Like a polaroid picture".toUpperCase());
                txt_head.setText("Like a polaroid picture");
                txt_description.setText("You've booked three Dance experiences!");
                break ;
            case "6":
                txt_title.setText("Mmmmm".toUpperCase());
                txt_head.setText("Mmmmm");
                txt_description.setText("You've booked three Food experiences!");
                break ;
            case "7":
                txt_title.setText("Like A French Girl".toUpperCase());
                txt_head.setText("Like A French Girl");
                txt_description.setText("You've booked three Arts experiences!");
                break ;
            case "8":
                txt_title.setText("On Song".toUpperCase());
                txt_head.setText("On Song");
                txt_description.setText("You've booked three Music experiences!");
                break ;
            case "9":
                txt_title.setText("Member of The Brain Trust".toUpperCase());
                txt_head.setText("Member of The Brain Trust");
                txt_description.setText("You just booked an educational experience and automatically became 10% cleverer.");
                break ;
            case "10":
                txt_title.setText("Goosfraba".toUpperCase());
                txt_head.setText("Goosfraba");
                txt_description.setText("You've booked your first Wellbeing experience!");
                break ;
            case "11":
                txt_title.setText("Are You Not Entertained?!".toUpperCase());
                txt_head.setText("Are You Not Entertained?!");
                txt_description.setText("You've booked your first Entertainment experience!");
                break ;
            case "12":
                txt_title.setText("Veni, Vidi, Vici".toUpperCase());
                txt_head.setText("Veni, Vidi, Vici");
                txt_description.setText("You've tried every category!");
                break ;
            case "13":
                txt_title.setText("Friend In Me".toUpperCase());
                txt_head.setText("Friend In Me");
                txt_description.setText("Invite a friend to Wonderush");
                break ;
            case "14":
                txt_title.setText("Challenge Accepted".toUpperCase());
                txt_head.setText("Challenge Accepted");
                txt_description.setText("Five days. Five experiences. Go you.");
                break ;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }

        mMenu = mActions.set_up_menu(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }
    }
}
