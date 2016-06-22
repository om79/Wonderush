package com.programize.wonderush.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.Activities.Browsing.Badge;
import com.programize.wonderush.Activities.Browsing.Experience;
import com.programize.wonderush.Models.TimelineInfo;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.myActions;

import java.util.ArrayList;


public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MainViewHolder> {

    private String TAG = "TAG - TIMELINE ADAPTER - " ;
    private ArrayList<TimelineInfo> mDataset;
    private Context mContext ;
    private myActions mActions = new myActions();

    //SETTING UP EXTERNAL FONT
    private String fontPath_Reg = "fonts/ProximaNova-Reg.ttf";
    private String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
    private Typeface tf_Reg ;
    private Typeface tf_Bold ;


    // Provide a suitable constructor (depends on the kind of dataset)
    public TimelineAdapter(Context context ,ArrayList<TimelineInfo> dataset) {
        mDataset = dataset ;
        mContext = context ;

        tf_Reg = Typeface.createFromAsset(context.getAssets(), fontPath_Reg);
        tf_Bold = Typeface.createFromAsset(context.getAssets(), fontPath_Bold);
    }



    @Override
    public TimelineAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v ;
        MainViewHolder vh ;
        if(mDataset.size()==1)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_first_left_alone, viewGroup, false);
            vh = new ViewHolder1(v);
        }
        else if( i == 0)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_first_left, viewGroup, false);
            vh = new ViewHolder1(v);
        }
        else if( (i == mDataset.size() - 1) && (i % 2 == 0) )
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_bottom_left, viewGroup, false);
            vh = new ViewHolder2(v);
        }
        else if( (i == mDataset.size() - 1) && (i % 2 == 1) )
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_bottom_right, viewGroup, false);
            vh = new ViewHolder2(v);
        }
        else if(i % 2 == 0)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_left, viewGroup, false);
            vh = new ViewHolder1(v);
        }
        else
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_profile_timeline_right, viewGroup, false);
            vh = new ViewHolder1(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final TimelineAdapter.MainViewHolder viewHolder, int i) {
        final int finalPosition = i ;

        if( mDataset.size() == 1)
        {
            final ViewHolder1 viewHolder1 = ( ViewHolder1 ) viewHolder;

            viewHolder1.txt_name.setText(mDataset.get(i).getName().toUpperCase());
            viewHolder1.txt_date.setText(mActions.getMonthString(Integer.parseInt(mDataset.get(i).getDate().substring(5, 7))) + " " + mDataset.get(i).getDate().substring(8, 10));

//            Picasso.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).resize(150, 150).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).into(viewHolder1.img_event);
//            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).override(150, 150).into(viewHolder1.img_event);
            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_event) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder1.img_event.setImageDrawable(circularBitmapDrawable);
                }
            });

            viewHolder1.img_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("TAG", mDataset.get(finalPosition).getID());
//                    Log.v("TAG", mDataset.get(finalPosition).getmType());

                    if (mDataset.get(finalPosition).getmType().equals("experience")) {
                        Intent intent = new Intent(mContext, Experience.class);
                        intent.putExtra("experience_id", mDataset.get(finalPosition).getID());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, Badge.class);
                        intent.putExtra("id", mDataset.get(finalPosition).getID());
                        intent.putExtra("image", mDataset.get(finalPosition).getURL());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
        else if((i == mDataset.size() - 1) )
        {
            final ViewHolder2 viewHolder2 = ( ViewHolder2 ) viewHolder;

            viewHolder2.txt_name.setText(mDataset.get(i-1).getName().toUpperCase());
            viewHolder2.txt_date.setText(mActions.getMonthString(Integer.parseInt(mDataset.get(i - 1).getDate().substring(5, 7))) + " " + mDataset.get(i - 1).getDate().substring(8, 10));
            viewHolder2.txt_name2.setText(mDataset.get(i).getName().toUpperCase());
            viewHolder2.txt_date2.setText(mActions.getMonthString(Integer.parseInt(mDataset.get(i).getDate().substring(5, 7))) + " " + mDataset.get(i).getDate().substring(8, 10));

//            Picasso.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).resize(150, 150).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).into(viewHolder2.img_event);
//            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).override(150, 150).into(viewHolder2.img_event);
            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder2.img_event) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder2.img_event.setImageDrawable(circularBitmapDrawable);
                }
            });

            viewHolder2.img_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("TAG", mDataset.get(finalPosition).getID());
//                    Log.v("TAG", mDataset.get(finalPosition).getmType());
                }
            });


            viewHolder2.img_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("TAG", mDataset.get(finalPosition).getID());
//                    Log.v("TAG", mDataset.get(finalPosition).getmType());

                    if(mDataset.get(finalPosition).getmType().equals("experience"))
                    {
                        Intent intent = new Intent(mContext, Experience.class);
                        intent.putExtra("experience_id", mDataset.get(finalPosition).getID());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, Badge.class);
                        intent.putExtra("id", mDataset.get(finalPosition).getID());
                        intent.putExtra("image", mDataset.get(finalPosition).getURL());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
        else if(i != 0)
        {
            final ViewHolder1 viewHolder1 = ( ViewHolder1 ) viewHolder;

            viewHolder1.txt_name.setText(mDataset.get(i - 1).getName().toUpperCase());
            viewHolder1.txt_date.setText(mActions.getMonthString(Integer.parseInt(mDataset.get(i - 1).getDate().substring(5, 7))) + " " + mDataset.get(i - 1).getDate().substring(8, 10));

//            Picasso.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).resize(150, 150).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).into(viewHolder1.img_event);
//            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).override(150, 150).into(viewHolder1.img_event);
            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_event) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder1.img_event.setImageDrawable(circularBitmapDrawable);
                }
            });

            viewHolder1.img_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("TAG",mDataset.get(finalPosition).getID() );
//                    Log.v("TAG", mDataset.get(finalPosition).getmType());

                    if(mDataset.get(finalPosition).getmType().equals("experience"))
                    {
                        Intent intent = new Intent(mContext, Experience.class);
                        intent.putExtra("experience_id", mDataset.get(finalPosition).getID());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, Badge.class);
                        intent.putExtra("id", mDataset.get(finalPosition).getID());
                        intent.putExtra("image", mDataset.get(finalPosition).getURL());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        else
        {
            final ViewHolder1 viewHolder1 = ( ViewHolder1 ) viewHolder;
//            Picasso.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).resize(150, 150).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).into(viewHolder1.img_event);
//            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).override(150, 150).into(viewHolder1.img_event);
            Glide.with(mContext).load(Definitions.APIdomain + mDataset.get(i).getURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_event) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder1.img_event.setImageDrawable(circularBitmapDrawable);
                }
            });

            viewHolder1.img_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.v("TAG", mDataset.get(finalPosition).getID());
//                    Log.v("TAG", mDataset.get(finalPosition).getmType());

                    if(mDataset.get(finalPosition).getmType().equals("experience"))
                    {
                        Intent intent = new Intent(mContext, Experience.class);
                        intent.putExtra("experience_id", mDataset.get(finalPosition).getID());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, Badge.class);
                        intent.putExtra("id", mDataset.get(finalPosition).getID());
                        intent.putExtra("image", mDataset.get(finalPosition).getURL());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_event;
        public TextView txt_name;
        public TextView txt_date;

        public MainViewHolder(View v) {
            super(v);
            img_event = (ImageView) v.findViewById(R.id.listviewprofile_imageview_event);
            txt_name = (TextView) v.findViewById(R.id.listviewprofile_text_type);
            txt_date = (TextView) v.findViewById(R.id.listviewprofile_text_date);

            txt_name.setTypeface(tf_Bold);
            txt_date.setTypeface(tf_Reg);
        }
    }

    public class ViewHolder1 extends MainViewHolder {

        public ViewHolder1(View v) {
            super(v);

        }
    }

    public class ViewHolder2 extends MainViewHolder {
        public TextView txt_name2;
        public TextView txt_date2;

        public ViewHolder2(View v) {
            super(v);
            txt_name2 = (TextView) v.findViewById(R.id.listviewprofile_text_type2);
            txt_date2 = (TextView) v.findViewById(R.id.listviewprofile_text_date2);

            txt_name2.setTypeface(tf_Bold);
            txt_date2.setTypeface(tf_Reg);
        }
    }
}
