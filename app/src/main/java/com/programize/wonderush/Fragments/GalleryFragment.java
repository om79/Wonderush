package com.programize.wonderush.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.programize.wonderush.R;

public class GalleryFragment extends Fragment{

    public static GalleryFragment newInstance(String image)
    {
        GalleryFragment f = new GalleryFragment();

        Bundle bdl = new Bundle(1);
        bdl.putString("image", image);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView img_image ;
        String image ;

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        img_image = (ImageView)v.findViewById(R.id.fragment_gallery_image);
        image = getArguments().getString("image");

//        Picasso.with(getActivity()).load(image).resize(600, 400).onlyScaleDown().memoryPolicy(MemoryPolicy.NO_STORE).into(img_image);
//        Picasso.with(getActivity()).load(image).fit().centerCrop().into(img_image);
        Glide.with(getActivity()).load(image).centerCrop().into(img_image);
        return v;

    }

}
