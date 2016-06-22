package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.programize.wonderush.Activities.Booking.Tickets;
import com.programize.wonderush.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingSuccess extends DialogFragment {

    public static BookingSuccess newInstance(String class_id,String experience_name, int day_indicator, String time_selected, String description, String image_url, String experience_id, String address, String start_time_selected)
    {
        BookingSuccess f = new BookingSuccess();

        Bundle bdl = new Bundle();

        bdl.putString("class_id", class_id);
        bdl.putString("experience_name", experience_name);
        bdl.putInt("day_indicator", day_indicator);
        bdl.putString("time_selected", time_selected);
        bdl.putString("start_time_selected", start_time_selected);
        bdl.putString("description", description);
        bdl.putString("image_url", image_url);
        bdl.putString("experience_id", experience_id);
        bdl.putString("address", address);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking_success, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView txt_head = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_head);
        TextView txt_par1 = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_paragraph1);
        TextView txt_par2 = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_paragraph2);
        TextView txt_par3 = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_paragraph3);
        TextView txt_ticket = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_ticket);
        TextView txt_calendar = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_calendar);
        TextView txt_share = (TextView)rootView.findViewById(R.id.fragment_booking_success_text_share);

        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_par1.setTypeface(tf_Reg);
        txt_par2.setTypeface(tf_Reg);
        txt_par3.setTypeface(tf_Reg);
        txt_ticket.setTypeface(tf_Bold);
        txt_calendar.setTypeface(tf_Bold);
        txt_share.setTypeface(tf_Bold);

        Calendar calendar = Calendar.getInstance();
//        Log.v("TAG " +"day2", new SimpleDateFormat("EEEE dd MMMM").format(calendar.getTime()));
//Log.v("TAG " + "ind", getArguments().getInt("day_indicator") + "");
        calendar.add(Calendar.DAY_OF_YEAR, getArguments().getInt("day_indicator"));

        String date = new SimpleDateFormat("EEEE dd MMMM").format(calendar.getTime());
        String day_print = new SimpleDateFormat("dd").format(calendar.getTime());
        String day_print2 = new SimpleDateFormat("EEEE ").format(calendar.getTime());
        String month_print = new SimpleDateFormat("  MMMM").format(calendar.getTime());
        if(day_print.equals("01"))
        {
            day_print = day_print + "st";
        }
        else if(day_print.equals("02"))
        {
            day_print = day_print + "nd";
        }
        else if(day_print.equals("03"))
        {
            day_print = day_print + "rd";
        }
        else
        {
            day_print = day_print + "th";
        }
        calendar.set(Calendar.HOUR, Integer.parseInt(getArguments().getString("time_selected").substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(getArguments().getString("time_selected").substring(3)));
        final long millies = calendar.getTimeInMillis() ;
        calendar.add(Calendar.DAY_OF_YEAR, -getArguments().getInt("day_indicator"));

//        Log.v("TAG start_time_selected", getArguments().getString("start_time_selected"));
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            cal.setTime(sdf.parse(getArguments().getString("start_time_selected")));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        Log.v("TAG calendaer after set",cal.toString() );


        txt_par1.setText("You've just booked " + getArguments().getString("experience_name") + " for " + day_print2 + day_print + month_print + " at " + getArguments().getString("time_selected") + "!");

        txt_par2.setText("Are you excited? Because we are!");

        txt_par3.setText("Check out My Bookings to view your ticket and don't forget to add it to your calendar and share with your friends!");


        //"VIEW YOUR TICKET" CLICK LISTENER
        txt_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG click", "ticket");
                Intent intent = new Intent(getActivity(), Tickets.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });

        //"SEND TO CALENDAR" CLICK LISTENER
        txt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG click", "calendar");

                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, getArguments().getString("experience_name"));
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, getArguments().getString("address"));
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                startActivity(calIntent);
                dismiss();
            }
        });

        //"SHARE YOUR TICKET" CLICK LISTENER
        txt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG click", "share");
                ShareInfo share_info = ShareInfo.newInstance(getArguments().getString("description"), getArguments().getString("image_url"), getArguments().getString("experience_name"), getArguments().getString("experience_id"));
                share_info.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                share_info.show(getFragmentManager(), "Sample Fragment");
                dismiss();
            }
        });


        return rootView;
    }

}

