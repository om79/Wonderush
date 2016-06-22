package com.programize.wonderush.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.programize.wonderush.Activities.Booking.Tickets;
import com.programize.wonderush.Activities.Browsing.Experience;
import com.programize.wonderush.Activities.Browsing.MapsActivity;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TicketUpcoming extends Fragment {

    private static myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private ScrollView mScrollview ;

    public static TicketUpcoming newInstance(JSONObject jobj)
    {
        TicketUpcoming f = new TicketUpcoming();

        Bundle bdl = new Bundle();

        try {
            bdl.putString("id", jobj.getString("id"));
            bdl.putString("experience_id", jobj.getJSONObject("experience").getString("id"));
            bdl.putString("name",jobj.getJSONObject("experience").getString("name"));
            bdl.putString("date",jobj.getJSONObject("experience_class").getString("start_time").substring(8, 10));
            bdl.putString("month",mActions.getMonthString(Integer.parseInt(jobj.getJSONObject("experience_class").getString("start_time").substring(5, 7))));
            bdl.putString("time",jobj.getJSONObject("experience_class").getString("start_time").substring(11, 16) + " - " +
                    jobj.getJSONObject("experience_class").getString("end_time").substring(11, 16));
            bdl.putString("street",jobj.getJSONObject("experience").getString("address_line_1"));
            bdl.putString("address",jobj.getJSONObject("experience").getString("address_line_2"));
            bdl.putString("city",jobj.getJSONObject("experience").getString("city"));
            bdl.putString("zipcode",jobj.getJSONObject("experience").getString("zipcode"));
            bdl.putString("hosted_by",jobj.getJSONObject("experience").getString("hosted_by"));
            bdl.putString("detailed_description",jobj.getJSONObject("experience").getString("detailed_description"));
            bdl.putString("start_time", jobj.getJSONObject("experience_class").getString("start_time")  );

            if(!jobj.getJSONObject("experience").isNull("latitude")) {
                bdl.putDouble("latitude", jobj.getJSONObject("experience").getDouble("latitude"));
            }
            else
            {
                bdl.putDouble("latitude", 0.0);
            }
            if(!jobj.getJSONObject("experience").isNull("longitude"))
            {
                bdl.putDouble("longitude", jobj.getJSONObject("experience").getDouble("longitude"));
            }
            else
            {
                bdl.putDouble("longitude", 0.0);
            }
            bdl.putBoolean("cancelled", jobj.getJSONObject("experience_class").getBoolean("cancelled"));

            bdl.putString("experience_category", jobj.getJSONObject("experience").getJSONArray("experience_categories").getJSONObject(0).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView date ;
        TextView month ;
        TextView time ;
        TextView name ;
        TextView street ;
        TextView address ;
        TextView city ;
        TextView hosted;
        TextView description ;
        ImageView img_type ;
        ImageView img_location;
        ImageView img_citymapper ;
        ImageView img_uber ;
        ImageView image_map ;
        TextView citymapper ;
        TextView uber ;
        TextView txt_cancel;
        ImageView img_cancelled;

        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg ;
        Typeface tf_Bold ;

        View v = inflater.inflate(R.layout.listview_booking_upcoming, container, false);

        prefs = getActivity().getApplicationContext().getSharedPreferences(Definitions.sharedprefname, getActivity().MODE_PRIVATE);
        progressDialog = MyProgressDialog.ctor(getActivity());

        date = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_date);
        month = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_month);
        time = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_time);
        name = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_name);
        street = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_street);
        address = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_address);
        city = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_city);
        citymapper = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_citymapper);
        uber = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_uber);
        hosted = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_hosted);
        description = (TextView)v.findViewById(R.id.listviewbookingupcoming_text_description);

        img_type = (ImageView)v.findViewById(R.id.listviewbookingupcoming_image_type);
        img_location = (ImageView)v.findViewById(R.id.listviewbookingupcoming_image_location);
        img_citymapper = (ImageView)v.findViewById(R.id.listviewbookingupcoming_image_citymapper);
        img_uber = (ImageView)v.findViewById(R.id.listviewbookingupcoming_image_uber);

        image_map = (ImageView)v.findViewById(R.id.listviewbookingupcoming_map_small);
        txt_cancel = (TextView)v.findViewById(R.id.listviewbookingupcoming_textview_cancelaction);

        img_cancelled = (ImageView)v.findViewById(R.id.listviewbookingupcoming_img_cancelled) ;

        mScrollview = (ScrollView)v.findViewById(R.id.listviewbookingupcoming_scrollView);


        tf_Reg = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Reg);
        tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);

        date.setTypeface(tf_Reg);
        month.setTypeface(tf_Bold);
        time.setTypeface(tf_Reg);
        name.setTypeface(tf_Bold);
        street.setTypeface(tf_Reg);
        address.setTypeface(tf_Reg);
        city.setTypeface(tf_Reg);
        citymapper.setTypeface(tf_Bold);
        uber.setTypeface(tf_Bold);
        hosted.setTypeface(tf_Reg);
        description.setTypeface(tf_Reg);
        txt_cancel.setTypeface(tf_Bold);

        name.setText(getArguments().getString("name").toUpperCase());
        date.setText(getArguments().getString("date"));
        month.setText(getArguments().getString("month").toUpperCase());
        time.setText(getArguments().getString("time"));
        street.setText(getArguments().getString("street"));
        address.setText(getArguments().getString("address"));
        city.setText(getArguments().getString("city") + ", " + getArguments().getString("zipcode"));
        hosted.setText(Html.fromHtml("<b>HOSTED BY</b>: " + getArguments().getString("hosted_by")));
        description.setText(Html.fromHtml(getArguments().getString("detailed_description")));

        img_citymapper.setImageResource(R.drawable.icon_citymapper);
        img_uber.setImageResource(R.drawable.icon_uber);
        img_location.setImageResource(R.drawable.pink_location);

        img_type.setImageResource(mActions.getCategoryPinkImage(getArguments().getString("experience_category")));


        String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?center="
                + getArguments().getDouble("latitude")
                + ","
                + getArguments().getDouble("longitude")
                + "&zoom=15&size=200x200&sensor=false" ;

//        Picasso.with(getActivity()).load(getMapURL).memoryPolicy(MemoryPolicy.NO_STORE).into(image_map);
        Glide.with(getActivity()).load(getMapURL).into(image_map);

        image_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat", getArguments().getDouble("latitude"));
                intent.putExtra("lng", getArguments().getDouble("longitude"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        citymapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "citymapper://directions?endcoord=" + getArguments().getDouble("latitude") + "," + getArguments().getDouble("longitude");
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

//                Log.v("TAG city", uri);
//                PackageManager packageManager = getActivity().getPackageManager();
//                List activities =
//                        packageManager.queryIntentActivities(
//                                intent,
//                                PackageManager.MATCH_DEFAULT_ONLY
//                        );
//                if (activities.size() > 0)
                getActivity().startActivity(Intent.createChooser(intent, "Select an application"));

            }
        });


        uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri =
                            "uber://?action=setPickup" +
                                    "&client_id=JfJFIbwI01JSi0i635FJNJFmMKsioHvs" +
                                    "&dropoff[latitude]=" + getArguments().getDouble("latitude") +
                                    "&dropoff[longitude]=" + getArguments().getDouble("longitude");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    getActivity().startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    // No Uber app! Open mobile website.
                    String url = "https://m.uber.com/sign-up" +
                            "?client_id=JfJFIbwI01JSi0i635FJNJFmMKsioHvs" +
                            "&dropoff[latitude]=" + getArguments().getDouble("latitude") +
                            "&dropoff[longitude]=" + getArguments().getDouble("longitude");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    getActivity().startActivity(i);
                }
            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();

                try {
                    calendar2.setTime(sdf.parse(getArguments().getString("start_time")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Log.v("TAG", getArguments().getString("start_time")) ;
//                Log.v("TAG 1 ", calendar1.getTime().toString()) ;
//                Log.v("TAG 2", calendar2.getTime().toString()) ;


                long diff = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

//                Log.v("TAG 3", hours + "") ;

                String msg ;
                if(hours>24)
                {
                    msg = "ARE YOU SURE YOU WANT TO CANCEL ?" ;
                }
                else
                {
                    msg = "You 're cancelling your booking within 24 hours of your booking." +
                            " Cancellations made within 24 hours of the activity are subject to a £2.50 cancellation charge." +
                            " Are you sure you want to cancel " +
                            getArguments().getString("name") + " ?";
                }

                new android.support.v7.app.AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("CANCEL BOOKING")
                        .setMessage(msg)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel_booking(getArguments().getString("id"));
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        name.setOnClickListener(TicketListener);
        date.setOnClickListener(TicketListener);
        month.setOnClickListener(TicketListener);
        time.setOnClickListener(TicketListener);
        street.setOnClickListener(TicketListener);
        address.setOnClickListener(TicketListener);
        city.setOnClickListener(TicketListener);
        hosted.setOnClickListener(TicketListener);
        description.setOnClickListener(TicketListener);

        if(getArguments().getBoolean("cancelled"))
        {
            img_cancelled.setVisibility(View.VISIBLE);
        }
        else
        {
            img_cancelled.setVisibility(View.GONE);
        }

        return v;
    }

    View.OnClickListener TicketListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            mActions.displayToast(getActivity(), "click");
            Intent intent = new Intent(getActivity(), Experience.class);
            intent.putExtra("experience_id",getArguments().getString("experience_id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(intent);
        }
    };


    private void cancel_booking(String id)
    {
        if(!mActions.checkInternetConnection(getActivity()))
        {
            mActions.displayToast(getActivity(), "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            UserAPI.get_StringResp("experience_class_bookings/cancel_booking_main/" + id, null, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            mActions.displayToast(getActivity(), "Cancel Booking was successful");

                            ((Tickets)getActivity()).cancel_upcoming();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(getActivity(), "Something went wrong");
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScrollview.post(new Runnable() {
            public void run() {
                mScrollview.fullScroll(View.FOCUS_UP);
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        progressDialog.dismiss();
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser)
        {
            if(mScrollview!=null) {
                mScrollview.post(new Runnable() {
                    public void run() {
                        mScrollview.fullScroll(View.FOCUS_UP);
                    }
                });
            }
        }
    }
}
