package com.programize.wonderush.Activities.Reviews;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Adapters.ReviewsAdapter;
import com.programize.wonderush.Fragments.ShareInfo;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.UI.SpacesItemDecoration;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Reviews1 extends Activity {

    private String TAG = "TAG - REVIEWS SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private ReviewsAdapter mAdapter;

    private TextView txt_head ;
    private ImageView img_arrow_back ;
    private ImageView img_social ;

    private String experience_id ;

    private static FloatingActionMenu mMenu = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews1);

        Bundle extras = getIntent().getExtras();
        experience_id = extras.getString("experience_id");

        initialize_widgets();

        set_up_font();

        set_up_images();

        initialize_recyclerView();

        set_up_listeners();

        get_reviews();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt_head = (TextView)findViewById(R.id.reviewscreen_textview_head);
        img_arrow_back = (ImageView)findViewById(R.id.reviewscreen_imageview_backarrow);
        img_social = (ImageView)findViewById(R.id.reviewscreen_imageview_social);

        progressDialog = MyProgressDialog.ctor(Reviews1.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
    }

    private void set_up_images()
    {
        //SETTING UP IMAGES
        img_arrow_back.setImageResource(R.drawable.icon_arrow_back);
        img_social.setImageResource(R.drawable.icon_share);
    }

    private void initialize_recyclerView()
    {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        //SETTING UP RECYCLER VIEW
        mRecyclerView = (RecyclerView) findViewById(R.id.reviewscreen_recycler_reviews);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewsAdapter(this, new JSONArray());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void set_up_listeners()
    {
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareInfo share_info = ShareInfo.newInstance(getIntent().getExtras().getString("description"), getIntent().getExtras().getString("image_url"), getIntent().getExtras().getString("title"), getIntent().getExtras().getString("experience_id"));
                share_info.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                share_info.show(getFragmentManager(), "Sample Fragment");
            }
        });
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
    }

    private void get_reviews()
    {
        if(!mActions.checkInternetConnection(Reviews1.this))
        {
            mActions.displayToast(Reviews1.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonArrayResp("experiences", experience_id + "/reviews", headers, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Reviews1.this, "Reviews Loaded !");
                            Log.v(TAG + "GET REVIEWS RESPONSE ", response.toString());

                            mAdapter.update(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Reviews1.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

}
