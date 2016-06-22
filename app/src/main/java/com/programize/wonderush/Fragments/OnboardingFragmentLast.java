package com.programize.wonderush.Fragments;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.programize.wonderush.R;

public class OnboardingFragmentLast extends Fragment {

    private TextView  txt2;

    public static OnboardingFragmentLast newInstance(Boolean old_user, String code, String discount)
    {
        OnboardingFragmentLast f = new OnboardingFragmentLast();

        Bundle bdl = new Bundle(1);

        bdl.putBoolean("old_user", old_user);
        bdl.putString("code", code);
        bdl.putString("discount", discount);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding_last, container, false);

        txt2 = (TextView) v.findViewById(R.id.fragment_onboarding_last_text2);


        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        txt2.setTypeface(tf);


        if (getArguments().getBoolean("old_user"))
        {
            txt2.setVisibility(View.INVISIBLE);
        }
        else
        {
            txt2.setVisibility(View.VISIBLE);
            txt2.setText(Html.fromHtml("Get your first month for £" +getArguments().getString("discount") +"<br>when you use code <b>" + getArguments().getString("code") + "</b>"));
        }


        return v;

    }

}
