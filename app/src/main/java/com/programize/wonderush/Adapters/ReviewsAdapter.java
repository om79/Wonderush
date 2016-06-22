package com.programize.wonderush.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import org.json.JSONArray;
import org.json.JSONException;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MainViewHolder> {
    private Context mContext ;
    private JSONArray mJsonArray ;

    //SETTING UP EXTERNAL FONT
    private String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
    private String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
    private Typeface tf_Reg ;
    private Typeface tf_Bold ;

    public ReviewsAdapter(Context mContext, JSONArray jarray)
    {
        this.mContext = mContext ;
        this.mJsonArray = jarray ;

        this.tf_Reg = Typeface.createFromAsset(mContext.getAssets(), fontPath_Reg);
        this.tf_Bold = Typeface.createFromAsset(mContext.getAssets(), fontPath_Bold);
    }

    public void update(JSONArray jarray)
    {
        mJsonArray = jarray;
        notifyDataSetChanged();
    }


    @Override
    public ReviewsAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v ;
        MainViewHolder vh ;

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_reviews_item, viewGroup, false);
        vh = new MainViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.MainViewHolder viewHolder, int i) {

        try {
            viewHolder.txt_comment.setText(mJsonArray.getJSONObject(i).getString("review_text"));
            viewHolder.txt_name.setText(mJsonArray.getJSONObject(i).getString("user_name"));
            if(mJsonArray.getJSONObject(i).has("user_image"))
            {
//                Picasso.with(mContext).load(Definitions.APIdomain + mJsonArray.getJSONObject(i).getString("user_image")).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_STORE).into(viewHolder.img_avatar);
//                Glide.with(mContext).load(Definitions.APIdomain + mJsonArray.getJSONObject(i).getString("user_image")).centerCrop().into(viewHolder.img_avatar);

                Glide.with(mContext).load(Definitions.APIdomain + mJsonArray.getJSONObject(i).getString("user_image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.img_avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.img_avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            else
            {
//                Picasso.with(mContext).load(R.drawable.male_avatar).resize(300, 300).into(viewHolder.img_avatar);
                Glide.with(mContext).load(R.drawable.male_avatar).into(viewHolder.img_avatar);

                Glide.with(mContext).load(R.drawable.male_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.img_avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.img_avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            Log.v("TAG num", mJsonArray.getJSONObject(i).toString() + "");

            if(!mJsonArray.getJSONObject(i).isNull("review_rating"))
            {
                switch (mJsonArray.getJSONObject(i).getInt("review_rating")) {
                    case 0:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                    case 1:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                    case 2:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                    case 3:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                    case 4:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                    case 5:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_on);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_on);
                        break;
                    default:
                        viewHolder.img_star1.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star2.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star3.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                        viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
                        break;
                }
            }
            else
            {
                viewHolder.img_star1.setImageResource(R.drawable.icon_star_off);
                viewHolder.img_star2.setImageResource(R.drawable.icon_star_off);
                viewHolder.img_star3.setImageResource(R.drawable.icon_star_off);
                viewHolder.img_star4.setImageResource(R.drawable.icon_star_off);
                viewHolder.img_star5.setImageResource(R.drawable.icon_star_off);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }







    }

    @Override
    public int getItemCount() {
        return mJsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MainViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_avatar ;
        public ImageView img_star1 ;
        public ImageView img_star2 ;
        public ImageView img_star3 ;
        public ImageView img_star4 ;
        public ImageView img_star5 ;
        public TextView txt_comment ;
        public TextView txt_name ;
        public MainViewHolder(View v) {
            super(v);
            img_avatar = (ImageView)v.findViewById(R.id.listview_reviews_image_avatar);
            img_star1 = (ImageView)v.findViewById(R.id.listview_reviews_image_star1);
            img_star2 = (ImageView)v.findViewById(R.id.listview_reviews_image_star2);
            img_star3 = (ImageView)v.findViewById(R.id.listview_reviews_image_star3);
            img_star4 = (ImageView)v.findViewById(R.id.listview_reviews_image_star4);
            img_star5 = (ImageView)v.findViewById(R.id.listview_reviews_image_star5);
            txt_comment = (TextView)v.findViewById(R.id.listview_reviews_text_comment);
            txt_name = (TextView)v.findViewById(R.id.listview_reviews_text_name);

            txt_comment.setTypeface(tf_Reg);
            txt_name.setTypeface(tf_Bold);
        }
    }


}
