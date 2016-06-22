package com.programize.wonderush.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.programize.wonderush.R;

public class OnboardingFragment extends Fragment{

    private ImageView videoView;

    public static OnboardingFragment newInstance(int raw_video, String title, String description, String description2)
    {
        OnboardingFragment f = new OnboardingFragment();

        Bundle bdl = new Bundle(1);

        bdl.putInt("raw_video", raw_video);
        bdl.putString("title", title);
        bdl.putString("description", description);
        bdl.putString("description2", description2);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding, container, false);

        videoView = (ImageView) v.findViewById(R.id.fragment_onboarding_video);

        TextView txt_head = (TextView)v.findViewById(R.id.fragment_onboarding_text_head);
        TextView txt_desription = (TextView)v.findViewById(R.id.fragment_onboarding_text_description);
        TextView txt_desription2 = (TextView)v.findViewById(R.id.fragment_onboarding_text_description2);

        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        Typeface tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);
        txt_head.setTypeface(tf_Bold);
        txt_desription.setTypeface(tf);
        txt_desription2.setTypeface(tf);

        txt_head.setText(getArguments().getString("title"));
        txt_desription.setText(getArguments().getString("description"));
        txt_desription2.setText(getArguments().getString("description2"));

        show_video();

        return v;
    }

    private void show_video()
    {
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(videoView);
        Glide.with(this).load(getArguments().getInt("raw_video")).into(imageViewTarget);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
