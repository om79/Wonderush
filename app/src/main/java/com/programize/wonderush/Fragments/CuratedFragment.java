package com.programize.wonderush.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.programize.wonderush.Activities.Browsing.Listings;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import java.util.ArrayList;

public class CuratedFragment extends Fragment{
    private int id ;

    public static CuratedFragment newInstance(int id, String title, String image, ArrayList<String> categories_array)
    {
        CuratedFragment f = new CuratedFragment();

        Bundle bdl = new Bundle(1);

        bdl.putInt("id", id);
        bdl.putString("title", title);
        bdl.putString("image", image);
        bdl.putStringArrayList("categories",categories_array );

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView img_image ;
        String image ;

        View v = inflater.inflate(R.layout.fragment_curated, container, false);

        img_image = (ImageView)v.findViewById(R.id.fragment_curated_image);

        id = getArguments().getInt("id");
        image = getArguments().getString("image");

//        Picasso.with(getActivity()).load(Definitions.APIdomain + image).fit().centerCrop().into(img_image);
        Glide.with(getActivity()).load(Definitions.APIdomain + image).centerCrop().into(img_image);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG curated", id + "");

                Intent intent = new Intent(getActivity(), Listings.class);
                intent.putExtra("curated_list", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        return v;

    }

}
