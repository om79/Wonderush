package com.programize.wonderush.Activities.Browsing;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.programize.wonderush.Activities.Booking.Booking;
import com.programize.wonderush.Activities.Reviews.Reviews1;
import com.programize.wonderush.Fragments.BookingMissing;
import com.programize.wonderush.Fragments.BookingSuccess;
import com.programize.wonderush.Fragments.GalleryFragment;
import com.programize.wonderush.Fragments.LeaveReview;
import com.programize.wonderush.Fragments.ReviewsPagerFragment;
import com.programize.wonderush.Fragments.ShareInfo;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Experience extends FragmentActivity {

    private String TAG = "TAG - EXPERIENCE SCREEN - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private ImageView img_social ;
    private ImageView img_arrow_back ;
    private ImageView img_type ;
    private ImageView img_bucket ;
    private ImageView img_star1;
    private ImageView img_star2;
    private ImageView img_star3;
    private ImageView img_star4;
    private ImageView img_star5;
    private ImageView img_booking ;
    private ImageView img_review ;
    private ImageView img_expand ;

    private TextView txt_head ;
    private TextView txt_eventname ;
    private TextView txt_locationname ;
    private TextView txt_calendar ;
    private TextView txt_counter ;
    private TextView txt_rating ;
    private TextView txt_description ;
    private TextView txt_booking ;
    private TextView txt_review ;
    private TextView txt_review2 ;

    private RelativeLayout rl_booking ;
    private RelativeLayout rl_review ;
    private RelativeLayout rl_rating ;

    private View view_map ;

    private ViewPager gallery_experience ;
    private com.programize.wonderush.Utilities.Functions.myViewpager pager_reviews ;
    private ArrayList<String> images ;

    private String experience_id ;
    private Boolean bucketed = false ;
    private Boolean bookable = false ;
    private Boolean can_book = false ;
    private int reviews = 0 ;

    public String avatar ;
    private String title_name = "";

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMap;
    private Double lat ;
    private Double lng ;
    private RelativeLayout map_outer ;

    private ScrollView main_content ;

    private static FloatingActionMenu mMenu = null  ;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        Bundle extras = getIntent().getExtras();
        experience_id = extras.getString("experience_id");

        //IF PREVIOUS SCREEN WAS BOOKING WITH SUCCESSFUL BOOK, THEN WE MUST SHOW A DIALOG FRAGMENT WITH SOME OPTIONS
        if(getIntent().hasExtra("from_booking"))
        {
            BookingSuccess book_fragment = BookingSuccess.newInstance(extras.getString("class_id"),extras.getString("experience_name"), extras.getInt("day_indicator"),extras.getString("time_selected"), extras.getString("description"), extras.getString("image_url"), extras.getString("experience_id"), extras.getString("address"), extras.getString("start_time_selected"));
            book_fragment.show(getFragmentManager(), "asd");
        }

        callbackManager = CallbackManager.Factory.create();

        initialize_widgets();

        set_up_font();

        set_up_images();

        set_up_listeners();

        if(prefs.contains("avatar"))
        {
            avatar = prefs.getString("avatar", null);
        }
        else
        {
            avatar = "no_avatar" ;
        }



//        mMap.onCreate(savedInstanceState);
//        mMap.onResume();// needed to get the map to display immediately
//        try {
//            MapsInitializer.initialize(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mGoogleMap = mMap.getMap();
        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
                mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
            }
        });

        get_experience();
    }

    private void initialize_widgets()
    {
        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt_head = (TextView)findViewById(R.id.experiencescreen_textview_head);
        txt_eventname = (TextView)findViewById(R.id.experiencescreen_textview_eventname);
        txt_locationname = (TextView)findViewById(R.id.experiencescreen_textview_locationname);
        txt_calendar = (TextView)findViewById(R.id.experiencescreen_textview_calendar);
        txt_counter = (TextView)findViewById(R.id.experiencescreen_text_count);
        txt_rating = (TextView)findViewById(R.id.experiencescreen_text_rating);
        txt_description = (TextView)findViewById(R.id.experiencescreen_text_description);
        txt_booking = (TextView)findViewById(R.id.experiencescreen_text_booking);
        txt_review = (TextView)findViewById(R.id.experiencescreen_text_review);
        txt_review2 = (TextView)findViewById(R.id.experiencescreen_text_review2);

        img_social = (ImageView)findViewById(R.id.experiencescreen_imageview_social);
        img_arrow_back = (ImageView)findViewById(R.id.experiencescreen_imageview_backarrow);
        img_type = (ImageView)findViewById(R.id.experiencescreen_image_type);
        img_bucket = (ImageView)findViewById(R.id.experiencescreen_image_bucket);
        img_star1 = (ImageView)findViewById(R.id.experiencescreen_image_star1);
        img_star2 = (ImageView)findViewById(R.id.experiencescreen_image_star2);
        img_star3 = (ImageView)findViewById(R.id.experiencescreen_image_star3);
        img_star4 = (ImageView)findViewById(R.id.experiencescreen_image_star4);
        img_star5 = (ImageView)findViewById(R.id.experiencescreen_image_star5);
//        img_booking = (ImageView)findViewById(R.id.experiencescreen_image_booking);
        img_review = (ImageView)findViewById(R.id.experiencescreen_image_review);
        img_expand = (ImageView)findViewById(R.id.experiencescreen_image_expand);

        rl_booking = (RelativeLayout)findViewById(R.id.experiencescreen_relative_booking);
        rl_review = (RelativeLayout)findViewById(R.id.experiencescreen_relative_review);
        rl_rating = (RelativeLayout)findViewById(R.id.experiencescreen_linear_rating);

        main_content = (ScrollView) findViewById(R.id.experiencescreen_scrollview);


        view_map = findViewById(R.id.experiencescreen_view_map);

        gallery_experience = (ViewPager)findViewById(R.id.experiencescreen_gallery);
        pager_reviews = (com.programize.wonderush.Utilities.Functions.myViewpager)findViewById(R.id.experiencescreen_viewpager_reviews);

        mMap = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.experiencescreen_map_small);

        map_outer = (RelativeLayout)findViewById(R.id.experiencescreen_map_outer);

        progressDialog = MyProgressDialog.ctor(Experience.this);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
//        String fontPath_RegItalic = "fonts/ProximaNova-RegItalic.otf";
        Typeface tf_Reg = Typeface.createFromAsset(getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_eventname.setTypeface(tf_Bold);
        txt_locationname.setTypeface(tf_Reg);
        txt_calendar.setTypeface(tf_Reg);
        txt_counter.setTypeface(tf_Reg);
        txt_rating.setTypeface(tf_Bold);
        txt_description.setTypeface(tf_Reg);
        txt_booking.setTypeface(tf_Bold);
        txt_review.setTypeface(tf_Bold);
        txt_review2.setTypeface(tf_Reg);
    }

    private void set_up_images()
    {

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
        get_experience2();
        if(mMenu!=null) {
            mMenu.myTerminateFunc(true);
        }

        mMenu = mActions.set_up_menu(this);
    }

    private void get_experience()
    {
        if(!mActions.checkInternetConnection(Experience.this))
        {
            mActions.displayToast(Experience.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonObjResp("experiences", experience_id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Experience.this, "Experience Info Loaded !");
//                            Log.v(TAG + "EXPERIENCE RESPONSE ", response.toString());

                            main_content.setVisibility(View.VISIBLE);

                            setValues(response);
                            setGalleryValues2(response);
                            setBucket(response);
                            set_up_social(response);


                            try {
//                                set_up_flipper(response.getJSONArray("featured_reviews"));
                                set_up_viewpager(response.getJSONArray("featured_reviews"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Experience.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void get_experience2()
    {
        if(!mActions.checkInternetConnection(Experience.this))
        {
            mActions.displayToast(Experience.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token",  prefs.getString("token", null));

            UserAPI.get_ParamReq_JsonObjResp("experiences", experience_id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Experience.this, "Experience Info Loaded !");
                            Log.v(TAG + "EXPERIENCE RESPONSE ", response.toString());

                            setValues(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Experience.this, "Something went wrong");
                            finish();
                        }
                    });
        }
    }

    private void setRatingStars(double rating)
    {

        switch ((int)Math.round(rating))
        {
            case 0:
                img_star1.setImageResource(R.drawable.icon_star_off2);
                img_star2.setImageResource(R.drawable.icon_star_off2);
                img_star3.setImageResource(R.drawable.icon_star_off2);
                img_star4.setImageResource(R.drawable.icon_star_off2);
                img_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 1:
                img_star1.setImageResource(R.drawable.icon_star_on2);
                img_star2.setImageResource(R.drawable.icon_star_off2);
                img_star3.setImageResource(R.drawable.icon_star_off2);
                img_star4.setImageResource(R.drawable.icon_star_off2);
                img_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 2:
                img_star1.setImageResource(R.drawable.icon_star_on2);
                img_star2.setImageResource(R.drawable.icon_star_on2);
                img_star3.setImageResource(R.drawable.icon_star_off2);
                img_star4.setImageResource(R.drawable.icon_star_off2);
                img_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 3:
                img_star1.setImageResource(R.drawable.icon_star_on2);
                img_star2.setImageResource(R.drawable.icon_star_on2);
                img_star3.setImageResource(R.drawable.icon_star_on2);
                img_star4.setImageResource(R.drawable.icon_star_off2);
                img_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 4:
                img_star1.setImageResource(R.drawable.icon_star_on2);
                img_star2.setImageResource(R.drawable.icon_star_on2);
                img_star3.setImageResource(R.drawable.icon_star_on2);
                img_star4.setImageResource(R.drawable.icon_star_on2);
                img_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 5:
                img_star1.setImageResource(R.drawable.icon_star_on2);
                img_star2.setImageResource(R.drawable.icon_star_on2);
                img_star3.setImageResource(R.drawable.icon_star_on2);
                img_star4.setImageResource(R.drawable.icon_star_on2);
                img_star5.setImageResource(R.drawable.icon_star_on2);
                break;
        }
    }

    private void setValues(JSONObject response)
    {
        try {
            title_name = response.getString("name");
            txt_head.setText(title_name);
            txt_eventname.setText(response.getString("name").toUpperCase());
            txt_locationname.setText(
                    response.getString("address_line_1") + ", "
                    + response.getString("address_line_2") + ", "
                    + response.getString("city")
            );
            txt_calendar.setText(mActions.getDays(response.getJSONArray("days_of_week")));

            reviews = response.getInt("number_of_reviews") ;
            if(reviews == 1)
            {
                txt_rating.setText(reviews + " REVIEW");
            }
            else
            {
                txt_rating.setText(reviews + " REVIEWS");
            }


//            Picasso.with(Experience.this).load(mActions.getCategorySmallImage2(response.getJSONArray("experience_categories").getJSONObject(0).getString("name"))).resize(40, 40).into(img_type);
            img_type.setImageResource(mActions.getCategorySmallImage2(response.getJSONArray("experience_categories").getJSONObject(0).getString("name")));
            setRatingStars(response.getDouble("average_rating"));

            txt_description.setText(Html.fromHtml(response.getString("detailed_description")));


//            mMap.setVisibility(View.VISIBLE);

            //SET UP MARKER ON MAP
            if(!response.isNull("latitude"))
            {
                lat = response.getDouble("latitude");
            }
            else
            {
                lat = 0.0 ;
            }
            if(!response.isNull("longitude"))
            {
                lng = response.getDouble("longitude");
            }
            else
            {
                lng = 0.0 ;
            }

            mMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title_name));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    map_outer.setVisibility(View.VISIBLE);
                }
            });



            bookable = response.getBoolean("bookable");
            can_book = response.getBoolean("can_book");
            if(bookable && can_book)
            {
                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(R.color.white));
                rl_booking.setBackgroundResource(R.drawable.style_gradientcolor_profile);
            }
            else
            {
                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
//                txt_booking.setShadowLayer(1.5f, -2, 2, Color.LTGRAY);
//                txt_booking.setTextColor(Color.parseColor("#E8E8E8"));
                txt_booking.setTextColor(Color.parseColor("#50ffffff"));
//                rl_booking.setBackgroundResource(R.drawable.style_gradientcolor_transparent);
            }

            if(!can_book)
            {
                txt_booking.setTextColor(getResources().getColor(R.color.white));
            }

            if(getIntent().hasExtra("from_booking")) {
                txt_booking.setText("BOOKED");
                txt_booking.setTextColor(Color.parseColor("#ffffff"));
            }


//            if(response.getBoolean("bookable"))
//            {
//                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(R.color.white));
//                bookable = true ;
//            }
//            else
//            {
//                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
//                bookable = false ;
//            }
//
//            if(response.getBoolean("can_book"))
//            {
//                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(R.color.white));
//                can_book = true ;
//            }
//            else
//            {
//                txt_booking.setText("BOOK NOW");
//                txt_booking.setTextColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
//                can_book = false ;
//            }


            Log.v(TAG +"Review", response.getBoolean("can_review")+ "");
            if(response.getBoolean("can_review"))
            {
//                rl_review.setVisibility(View.VISIBLE);
                rl_booking.setVisibility(View.GONE);
            }
            else
            {
//                rl_review.setVisibility(View.GONE);
                rl_booking.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setGalleryValues2(JSONObject response)
    {
        try {
            images = new ArrayList<>();
            if(response.has("experience_gallery_image_1"))
            {
                images.add(Definitions.APIdomain + "/" + response.getString("experience_gallery_image_1"));
            }
            if(response.has("experience_gallery_image_2"))
            {
                images.add(Definitions.APIdomain + "/" + response.getString("experience_gallery_image_2"));
            }
            if(response.has("experience_gallery_image_3"))
            {
                images.add(Definitions.APIdomain + "/" + response.getString("experience_gallery_image_3"));
            }
            if(response.has("experience_gallery_image_4"))
            {
                images.add(Definitions.APIdomain + "/" + response.getString("experience_gallery_image_4"));
            }
            if(response.has("experience_gallery_image_5"))
            {
                images.add(Definitions.APIdomain + "/" + response.getString("experience_gallery_image_5"));
            }

            ViewPagerGalleryAdapter gallery_adapter = new ViewPagerGalleryAdapter(getSupportFragmentManager());
            gallery_adapter.notifyDataSetChanged();
            gallery_experience.setAdapter(gallery_adapter);

            txt_counter.setText((1) + "/" + images.size() + "");
            gallery_experience.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    txt_counter.setText((position + 1) + "/" + images.size() + "");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

//            txt_counter.setText((gallery_experience.getCurrentItem() + 1) + "/" + images.size() + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBucket(JSONObject response)
    {
        try {
            bucketed = response.getBoolean("bucketed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bucketed)
        {
            img_bucket.setImageResource(R.drawable.icon_bucket_on);
        }
        else
        {img_bucket.setImageResource(R.drawable.icon_bucket_off);
        }

        img_bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventbucket();
            }
        });
    }

    private void eventbucket()
    {
        String path;
        if(bucketed)
        {
            path = "bucket_list_items/remove_from_bucket_list";
        }
        else
        {
            path = "bucket_list_items/add_to_bucket_list";
        }


        if(!mActions.checkInternetConnection(Experience.this))
        {
            mActions.displayToast(Experience.this, "No Internet Connection");
        }
        else {

           progressDialog.show();

            //Define Headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("experience_id", experience_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserAPI.post_StringResp(path, jbody, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(Experience.this, "Bucket change for id: " + experience_id + " was successful !");
                            if (bucketed) {
                                img_bucket.setImageResource(R.drawable.icon_bucket_off);
                                bucketed = false;
                            } else {
                                img_bucket.setImageResource(R.drawable.icon_bucket_on);
                                bucketed = true;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(Experience.this, "Bucket change for id: " + experience_id + " was unsuccessful !");
                        }
                    });
        }


    }

    private void set_up_listeners()
    {
        //BACK ARROW CLICK LISTENER
        img_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        rl_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LeaveReview dialogFragment = LeaveReview.newInstance(avatar, experience_id, title_name);
                    dialogFragment.show(getFragmentManager(), "Sample Fragment");
            }
        });

        //BOOKING-REVIEW CLICK LISTENER
        rl_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, bookable +" "  +can_book);
                if (bookable && can_book) {
                    Intent intent = new Intent(Experience.this, Booking.class);
                    intent.putExtra("experience_id", experience_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (!can_book){
                    BookingMissing book_fragment = new BookingMissing();
                    book_fragment.show(getFragmentManager(), "asd");

                }
            }
        });

        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Experience.this, MapsActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("name", title_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Experience.this, MapsActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void set_up_viewpager(JSONArray reviews_array)
    {
        ViewPagerReviewsAdapter Reviews_adapter = new ViewPagerReviewsAdapter(getSupportFragmentManager());

        if(reviews_array.length() == 0)
        {
            pager_reviews.setVisibility(View.GONE);
            txt_review2.setVisibility(View.VISIBLE);
        }
        else
        {
            pager_reviews.setVisibility(View.VISIBLE);
            txt_review2.setVisibility(View.GONE);
        }
        Log.v(TAG + "size ", reviews_array.length() + "") ;

        int featured_review_rating ;
        String featured_review_avatar;
        for(int i = 0 ; i < reviews_array.length() ; i++)
        {
            try {
                if(reviews_array.getJSONObject(i).has("user_image"))
                {
                    featured_review_avatar =Definitions.APIdomain + reviews_array.getJSONObject(i).getString("user_image");
                }
                else
                {
                    featured_review_avatar = "";
                }

                if(reviews_array.getJSONObject(i).isNull("review_rating"))
                {
                    featured_review_rating = 0 ;
                }
                else
                {
                    featured_review_rating = reviews_array.getJSONObject(i).getInt("review_rating") ;
                }
                Reviews_adapter.addFrag(ReviewsPagerFragment.newInstance(reviews_array.getJSONObject(i).getString("user_name"), reviews_array.getJSONObject(i).getString("review_text"), featured_review_rating, featured_review_avatar,reviews_array.length(), i ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pager_reviews.setAdapter(Reviews_adapter);

    }

    private void set_up_social(final JSONObject response)
    {
        img_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "";
                String image_url = "";
                String title = "";
                try {
                    description = response.getString("description");
                    image_url = Definitions.APIdomain + response.getString("experience_image");
                    title = response.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareInfo share_info = ShareInfo.newInstance(description, image_url, title, experience_id);
//                share_info.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
                share_info.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                share_info.show(getFragmentManager(), "ShareInfo");
            }
        });

        rl_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviews!=0)
                {
                    String description = "";
                    String image_url = "";
                    String title = "";
                    try {
                        description = response.getString("description");
                        image_url = Definitions.APIdomain + response.getString("experience_image");
                        title = response.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Experience.this, Reviews1.class);
                    intent.putExtra("experience_id",experience_id);
                    intent.putExtra("description",description);
                    intent.putExtra("image_url",image_url);
                    intent.putExtra("title",title);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
//                else
//                {
//                    mActions.displayToast(Experience.this, "No reviews to see");
//                }
            }
        });
    }

    static class ViewPagerReviewsAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mReviewsFragmentList = new ArrayList<>();

        public ViewPagerReviewsAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mReviewsFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mReviewsFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mReviewsFragmentList.add(fragment);
        }
    }

    class ViewPagerGalleryAdapter extends FragmentStatePagerAdapter {

        public ViewPagerGalleryAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return GalleryFragment.newInstance(images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
