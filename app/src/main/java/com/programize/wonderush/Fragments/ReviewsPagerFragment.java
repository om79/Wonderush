package com.programize.wonderush.Fragments;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.R;

public class ReviewsPagerFragment extends Fragment{;

    public static ReviewsPagerFragment newInstance(String name, String comment, int rating, String avatar, int size, int position)
    {
        ReviewsPagerFragment f = new ReviewsPagerFragment();

        Bundle bdl = new Bundle(1);

        bdl.putString("name", name);
        bdl.putString("comment", comment);
        bdl.putString("avatar", avatar);
        bdl.putInt("rating", rating);
        bdl.putInt("size", size);
        bdl.putInt("position", position);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewHolderPagerItem viewHolder ;
        View view = inflater.inflate(R.layout.listview_flipper_reviews_item, container, false);

        viewHolder = new ViewHolderPagerItem();

        viewHolder.txt_flipper_name = (TextView)view.findViewById(R.id.listview_flipper_reviews_text_name);
        viewHolder.txt_flipper_comment = (TextView)view.findViewById(R.id.listview_flipper_reviews_text_comment);
        viewHolder.img_flipper_avatar = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_avatar);
        viewHolder.img_flipper_star1 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_star1);
        viewHolder.img_flipper_star2 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_star2);
        viewHolder.img_flipper_star3 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_star3);
        viewHolder.img_flipper_star4 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_star4);
        viewHolder.img_flipper_star5 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_star5);
        viewHolder.img_flipper_dot1 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_dot1);
        viewHolder.img_flipper_dot2 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_dot2);
        viewHolder.img_flipper_dot3 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_dot3);
        viewHolder.img_flipper_dot4 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_dot4);
        viewHolder.img_flipper_dot5 = (ImageView)view.findViewById(R.id.listview_flipper_reviews_image_dot5);

        //SETTING UP EXTERNAL FONT
        String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf_Reg = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Reg);
        Typeface tf_Bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);
        viewHolder.txt_flipper_name.setTypeface(tf_Bold);
        viewHolder.txt_flipper_comment.setTypeface(tf_Reg);


        viewHolder.txt_flipper_name.setText(getArguments().getString("name"));
        viewHolder.txt_flipper_comment.setText(getArguments().getString("comment"));

        //SET UP RATING STARS
        switch (getArguments().getInt("rating"))
        {
            case 0:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 1:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 2:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 3:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_off2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 4:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_off2);
                break;
            case 5:
                viewHolder.img_flipper_star1.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star2.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star3.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star4.setImageResource(R.drawable.icon_star_on2);
                viewHolder.img_flipper_star5.setImageResource(R.drawable.icon_star_on2);
                break;
        }

        //SET UP UNFILLED DOTS
        switch (getArguments().getInt("size"))
        {
            case 1 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_unfilled1);
                break;
            case 2 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot2.setImageResource(R.drawable.icon_dot_unfilled1);
                break;
            case 3 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot2.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot3.setImageResource(R.drawable.icon_dot_unfilled1);
                break;
            case 4 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot2.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot3.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot4.setImageResource(R.drawable.icon_dot_unfilled1);
                break;
            case 5 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot2.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot3.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot4.setImageResource(R.drawable.icon_dot_unfilled1);
                viewHolder.img_flipper_dot5.setImageResource(R.drawable.icon_dot_unfilled1);
                break;
        }

        //SET UP FILLED DOT
        switch (getArguments().getInt("position"))
        {
            case 0 :
                viewHolder.img_flipper_dot1.setImageResource(R.drawable.icon_dot_filled1);
                break;
            case 1 :
                viewHolder.img_flipper_dot2.setImageResource(R.drawable.icon_dot_filled1);
                break;
            case 2 :
                viewHolder.img_flipper_dot3.setImageResource(R.drawable.icon_dot_filled1);
                break;
            case 3 :
                viewHolder.img_flipper_dot4.setImageResource(R.drawable.icon_dot_filled1);
                break;
            case 4 :
                viewHolder.img_flipper_dot5.setImageResource(R.drawable.icon_dot_filled1);
                break;
        }

        if(getArguments().getString("avatar").equals(""))
        {
//            Picasso.with(getActivity()).load(R.drawable.male_avatar).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_STORE).into(viewHolder.img_flipper_avatar);
//            Glide.with(getActivity()).load(R.drawable.male_avatar).centerCrop().into(viewHolder.img_flipper_avatar);
            Glide.with(getActivity()).load(R.drawable.male_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.img_flipper_avatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder.img_flipper_avatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        else
        {
//            Picasso.with(getActivity()).load(getArguments().getString("avatar")).resize(300, 300).memoryPolicy(MemoryPolicy.NO_STORE).into(viewHolder.img_flipper_avatar);
//            Glide.with(getActivity()).load(getArguments().getString("avatar")).into(viewHolder.img_flipper_avatar);
            Glide.with(getActivity()).load(getArguments().getString("avatar")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.img_flipper_avatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder.img_flipper_avatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        return view;

    }

    static class ViewHolderPagerItem {
        TextView txt_flipper_name;
        TextView txt_flipper_comment ;
        ImageView img_flipper_avatar ;
        ImageView img_flipper_star1 ;
        ImageView img_flipper_star2 ;
        ImageView img_flipper_star3 ;
        ImageView img_flipper_star4 ;
        ImageView img_flipper_star5 ;
        ImageView img_flipper_dot1 ;
        ImageView img_flipper_dot2 ;
        ImageView img_flipper_dot3 ;
        ImageView img_flipper_dot4 ;
        ImageView img_flipper_dot5 ;
    }

}
