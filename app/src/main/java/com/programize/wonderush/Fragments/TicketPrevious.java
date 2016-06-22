package com.programize.wonderush.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.programize.wonderush.Activities.Browsing.Experience;
import com.programize.wonderush.Activities.Browsing.MapsActivity;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.UI.DrawView;

import org.json.JSONException;
import org.json.JSONObject;

public class TicketPrevious extends Fragment {

    private static myActions mActions = new myActions();

    private ScrollView mScrollview ;





    public static TicketPrevious newInstance(JSONObject jobj)
    {
        TicketPrevious f = new TicketPrevious();

        Bundle bdl = new Bundle();

        try {
            Log.v("TAG", jobj.toString());
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

        ImageView image_map ;



        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg ;
        Typeface tf_Bold ;

        View v = inflater.inflate(R.layout.listview_booking_previous, container, false);

        date = (TextView)v.findViewById(R.id.listviewbookingprevious_text_date);
        month = (TextView)v.findViewById(R.id.listviewbookingprevious_text_month);
        time = (TextView)v.findViewById(R.id.listviewbookingprevious_text_time);
        name = (TextView)v.findViewById(R.id.listviewbookingprevious_text_name);
        street = (TextView)v.findViewById(R.id.listviewbookingprevious_text_street);
        address = (TextView)v.findViewById(R.id.listviewbookingprevious_text_address);
        city = (TextView)v.findViewById(R.id.listviewbookingprevious_text_city);
        hosted = (TextView)v.findViewById(R.id.listviewbookingprevious_text_hosted);
        description = (TextView)v.findViewById(R.id.listviewbookingprevious_text_description);

        img_type = (ImageView)v.findViewById(R.id.listviewbookingprevious_image_type);
        img_location = (ImageView)v.findViewById(R.id.listviewbookingprevious_image_location);

        image_map = (ImageView)v.findViewById(R.id.listviewbookingprevious_map_small);
        mScrollview = (ScrollView)v.findViewById(R.id.listviewbookingprevious_scrollView);
        LinearLayout home_rl = (LinearLayout)v.findViewById(R.id.listviewbookingprevious_linear);

        tf_Reg = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Reg);
        tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);

        date.setTypeface(tf_Reg);
        month.setTypeface(tf_Bold);
        time.setTypeface(tf_Reg);
        name.setTypeface(tf_Bold);
        street.setTypeface(tf_Reg);
        address.setTypeface(tf_Reg);
        city.setTypeface(tf_Reg);
        hosted.setTypeface(tf_Reg);
        description.setTypeface(tf_Reg);

        img_type.setImageResource(mActions.getCategoryPinkImage(getArguments().getString("experience_category")));
        img_location.setImageResource(R.drawable.pink_location);

        name.setText(getArguments().getString("name").toUpperCase());
        date.setText(getArguments().getString("date"));
        month.setText(getArguments().getString("month").toUpperCase());
        time.setText(getArguments().getString("time"));
        street.setText(getArguments().getString("street"));
        address.setText(getArguments().getString("address"));
        city.setText(getArguments().getString("city") + ", " + getArguments().getString("zipcode"));
        hosted.setText(Html.fromHtml("<b>HOSTED BY</b>: " + getArguments().getString("hosted_by")));
        description.setText(Html.fromHtml(getArguments().getString("detailed_description")));

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

        date.setOnClickListener(TicketListener);
        month.setOnClickListener(TicketListener);
        time.setOnClickListener(TicketListener);
        name.setOnClickListener(TicketListener);
        street.setOnClickListener(TicketListener);
        address.setOnClickListener(TicketListener);
        city.setOnClickListener(TicketListener);
        hosted.setOnClickListener(TicketListener);
        description.setOnClickListener(TicketListener);

//        RelativeLayout.LayoutParams rLParams =
//                new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.MATCH_PARENT, dpToPx(30));
//        rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//
//        DrawView test = new DrawView(getActivity());
//        home_rl.addView(test, rLParams);


        LinearLayout.LayoutParams lRParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(30));

        DrawView test = new DrawView(getActivity());
        home_rl.addView(test, lRParams);

        return v;
    }

    View.OnClickListener TicketListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Experience.class);
            intent.putExtra("experience_id",getArguments().getString("experience_id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mScrollview.post(new Runnable() {
            public void run() {
                mScrollview.fullScroll(View.FOCUS_UP);
            }
        });
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

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
