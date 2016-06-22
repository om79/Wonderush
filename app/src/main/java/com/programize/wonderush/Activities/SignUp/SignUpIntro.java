package com.programize.wonderush.Activities.SignUp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.programize.wonderush.Activities.LoginScreen;
import com.programize.wonderush.Fragments.OnboardingFragment;
import com.programize.wonderush.Fragments.OnboardingFragmentLast;
import com.programize.wonderush.R;

import java.util.ArrayList;
import java.util.List;

public class SignUpIntro extends AppCompatActivity {

    private TextView txt_login ;
    private TextView txt_signup ;
    private ViewPager viewPager_onboarding;
    private RelativeLayout layout_rl ;
    private Context _this;
    private ImageView dot1, dot2, dot3, dot4 ;

    private boolean old_user ;
    private String coupon, discount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_intro);

        _this = this ;

        if(!getIntent().getExtras().isEmpty())
        {
            old_user = getIntent().getExtras().getBoolean("old_user");
            coupon = getIntent().getExtras().getString("coupon");
            discount = getIntent().getExtras().getString("discount");
        }

        initialize_widgets();

        set_up_fonts();

        show_viewpager();

        txt_login.setText(Html.fromHtml("Already a member ? <b>Login</b>"));

        change_colors(0);

        set_up_listeners();
    }

    private void initialize_widgets()
    {
        txt_login               = (TextView) findViewById(R.id.signup_intro_text_login);
        txt_signup              = (TextView) findViewById(R.id.signup_intro_text_signup);
        viewPager_onboarding    = (ViewPager) findViewById(R.id.signup_intro_viewpager);
        layout_rl               = (RelativeLayout) findViewById(R.id.signup_intro_layout_rl);
        dot1                    = (ImageView) findViewById(R.id.splashscreen_image_dot1);
        dot2                    = (ImageView) findViewById(R.id.splashscreen_image_dot2);
        dot3                    = (ImageView) findViewById(R.id.splashscreen_image_dot3);
        dot4                    = (ImageView) findViewById(R.id.splashscreen_image_dot4);
    }

    private void set_up_fonts()
    {
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt_login.setTypeface(tf);
        txt_signup.setTypeface(tf);
    }

    private void show_viewpager()
    {
        ViewPagerOnboardinfAdapter adapter = new ViewPagerOnboardinfAdapter(getSupportFragmentManager());

        adapter.addFrag(OnboardingFragment.newInstance(R.raw.discover, "Discover", "things to do near you,", "whenever you want"));
        adapter.addFrag(OnboardingFragment.newInstance(R.raw.book, "Book", "as many as you like", "every day for \u00A3 29/mo"));
        adapter.addFrag(OnboardingFragment.newInstance(R.raw.enjoy, "Enjoy", "a variety of experiences","and classes at no extra cost"));
        adapter.addFrag(OnboardingFragmentLast.newInstance(old_user, coupon, discount));

        viewPager_onboarding.setAdapter(adapter);

        viewPager_onboarding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Log.v(TAG + "position", position + "");

                if (position == 0) {
                    dot1.setImageResource(R.drawable.icon_dot_white_on);
                    dot2.setImageResource(R.drawable.icon_dot_white_off);
                    dot3.setImageResource(R.drawable.icon_dot_white_off);
                    dot4.setImageResource(R.drawable.icon_dot_white_off);

                    change_colors(0);
                } else if (position == 1) {
                    dot1.setImageResource(R.drawable.icon_dot_white_off);
                    dot2.setImageResource(R.drawable.icon_dot_white_on);
                    dot3.setImageResource(R.drawable.icon_dot_white_off);
                    dot4.setImageResource(R.drawable.icon_dot_white_off);

                    change_colors(0);
                } else if (position == 2){
                    dot1.setImageResource(R.drawable.icon_dot_white_off);
                    dot2.setImageResource(R.drawable.icon_dot_white_off);
                    dot3.setImageResource(R.drawable.icon_dot_white_on);
                    dot4.setImageResource(R.drawable.icon_dot_white_off);

                    change_colors(0);
                }
                else if(position == 3)
                {
                    dot1.setImageResource(R.drawable.icon_dot_pink_off);
                    dot2.setImageResource(R.drawable.icon_dot_pink_off);
                    dot3.setImageResource(R.drawable.icon_dot_pink_off);
                    dot4.setImageResource(R.drawable.icon_dot_pink_on);

                    change_colors(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void change_colors(int state)
    {
        switch (state)
        {
            case 0:
                layout_rl.setBackgroundColor(ContextCompat.getColor(_this, R.color.white));
                txt_signup.setTextColor(ContextCompat.getColor(_this, R.color.white));
                txt_signup.setBackgroundColor(ContextCompat.getColor(_this, R.color.pink2));
                txt_login.setTextColor(Color.BLACK);
                break;
            case 1:
                layout_rl.setBackgroundColor(ContextCompat.getColor(_this, R.color.pink2));
                txt_signup.setTextColor(ContextCompat.getColor(_this, R.color.pink2));
                txt_signup.setBackgroundColor(ContextCompat.getColor(_this, R.color.white));
                txt_login.setTextColor(ContextCompat.getColor(_this, R.color.white));
                break;
            default:
                break;
        }
    }

    private void set_up_listeners()
    {
        //LOGIN BUTTON CLICK LISTENER
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpIntro.this, LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        //SIGN UP BUTTON CLICK LISTENER
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpIntro.this, SignUp1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }


    static class ViewPagerOnboardinfAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerOnboardinfAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

}
