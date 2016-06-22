package com.programize.wonderush.Activities.Browsing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.SignUp.SignUp1;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Fragments.TabFragment;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Badges extends FragmentActivity {

    private String TAG = "TAG - BADGES SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private ViewPager viewPager ;
    private TabLayout tabLayout ;

    private TextView txt_username ;
    private TextView txt_location ;
    private TextView txt_head ;

    private ImageView img_arrow_back ;
    private ImageView img_edit ;

    private Bundle extras ;
    private String avatar ;
    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        extras = getIntent().getExtras();

        initialize_widgets();

        set_up_font();

        set_up_values();

        set_up_listeners();

        search_badges();
    }

    private void initialize_widgets()
    {
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        txt_username = (TextView)findViewById(R.id.badges1screen_textview_username);
        txt_location = (TextView)findViewById(R.id.badges1screen_textview_location);
        txt_head = (TextView)findViewById(R.id.badges1screen_textview_head);

        img_arrow_back = (ImageView)findViewById(R.id.badges1screen_imageview_backarrow);
        img_edit = (ImageView)findViewById(R.id.badges1screen_imageview_edit);

        progressDialog = MyProgressDialog.ctor(Badges.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_username.setTypeface(tf_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_location.setTypeface(tf_Reg);
    }

    private void set_up_viewpager(JSONArray response)
    {
        JSONArray unlocked_array = new JSONArray();
        JSONArray locked_array = new JSONArray();
        for(int i = 0 ;i <response.length();i++)
        {
            try {
                if(response.getJSONObject(i).getBoolean("locked"))
                {
                    locked_array.put(response.getJSONObject(i));
                }
                else
                {
                    unlocked_array.put(response.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(TabFragment.newInstance(response.toString()), "ALL");
        adapter.addFrag(TabFragment.newInstance(unlocked_array.toString()), "UNLOCKED");
        adapter.addFrag(TabFragment.newInstance(locked_array.toString()), "LOCKED");

        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tf_Bold);
                }
            }
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }

        mMenu = mActions.set_up_menu(this);
        get_unreviewed_events();
    }

    private void search_badges()
    {
        if(!mActions.checkInternetConnection(Badges.this))
        {
            mActions.displayToast(Badges.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_JsonArrayResp("badges", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.dismiss();
                            set_up_viewpager(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Badges.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void set_up_values()
    {
        txt_username.setText(prefs.getString("firstname", null));

        txt_location.setText("London, UK");

        txt_head.setText("Badges");
    }

    private void set_up_listeners()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Badges.this, SignUp1.class);
                intent.putExtra("from_profile", true);
                intent.putExtra("id", extras.getString("id"));
                intent.putExtra("firstname", extras.getString("firstname"));
                intent.putExtra("image", extras.getString("image"));

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void get_unreviewed_events()
    {
        if(!mActions.checkInternetConnection(Badges.this))
        {
            mActions.displayToast(Badges.this, "No Internet Connection");
        }
        else
        {
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            //Define Params
            Map<String,String> params = new HashMap<>();

            params.put("since", prefs.getString("last_login", null));

            UserAPI.get_JsonArrayResp("members/"+prefs.getInt("id", -1)+"/completed_unreviewed_classes", headers, params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String my_time ;
                            my_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            my_time = my_time + "T" + new SimpleDateFormat("hh:mmZ").format(new Date());

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("last_login", my_time);
                            editor.apply();

                            if(response.length()!=0)
                            {
                                String event_name = "";
                                String event_id = "";
                                try {
                                    event_name= response.getJSONObject(0).getJSONObject("experience").getString("name");
                                    event_id= response.getJSONObject(0).getJSONObject("experience").getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if(prefs.contains("avatar"))
                                {
                                    avatar = prefs.getString("avatar", null);
                                }
                                else
                                {
                                    avatar = "no_avatar" ;
                                }

                                final String finalEvent_id = event_id;
                                final String finalEvent_name = event_name;
                                new AlertDialog.Builder(Badges.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("You did it!")
                                        .setMessage("How was "+ event_name +"\nWe want to hear all about it so please write a quick review and let us know how you got on.")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                LeaveReview dialogFragment = LeaveReview.newInstance(avatar, finalEvent_id, finalEvent_name);
                                                dialogFragment.show(getFragmentManager(), "Sample Fragment");
                                            }
                                        })
                                        .setNegativeButton("NO", null)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mActions.displayToast(Badges.this, "Error Occurred");
                        }
                    });
        }
    }

}
