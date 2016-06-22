package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.programize.wonderush.Activities.Settings.Billing2;
import com.programize.wonderush.R;

public class BookingMissing extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking_missing, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView txt_head = (TextView)rootView.findViewById(R.id.fragment_booking_missing_text_head);
        TextView txt_par1 = (TextView)rootView.findViewById(R.id.fragment_booking_missing_text_paragraph1);
        TextView txt_par2 = (TextView)rootView.findViewById(R.id.fragment_booking_missing_text_paragraph2);
        TextView txt_billing = (TextView)rootView.findViewById(R.id.fragment_booking_missing_text_billing);
        TextView txt_cancel = (TextView)rootView.findViewById(R.id.fragment_booking_missing_text_cancel);

        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_par1.setTypeface(tf_Reg);
        txt_par2.setTypeface(tf_Reg);
        txt_billing.setTypeface(tf_Bold);
        txt_cancel.setTypeface(tf_Bold);


        //"BILLING" CLICK LISTENER
        txt_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG click", "billing");
                Intent intent = new Intent(getActivity(), Billing2.class);
                intent.putExtra("sign_up_completed", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });

        //"CANCEL" CLICK LISTENER
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG click", "cancel");
                dismiss();
            }
        });


        return rootView;
    }

}

