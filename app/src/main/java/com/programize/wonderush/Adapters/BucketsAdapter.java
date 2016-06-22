package com.programize.wonderush.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.programize.wonderush.Activities.Browsing.Experience;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.MainViewHolder> {
    private JSONArray mDataset;
    private Context mContext ;
    private myActions mActions ;
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    //CONSTRUCTOR
    public BucketsAdapter(Context mContext, JSONArray dataset) {
        this.mContext = mContext ;
        mDataset = dataset ;
        mActions = new myActions() ;
        //SET UP SHARED PREFERENCES
        prefs = mContext.getSharedPreferences(Definitions.sharedprefname, mContext.MODE_PRIVATE);
        progressDialog = MyProgressDialog.ctor(mContext);
    }

    //FUNCTION TO UPDATE VALUES
    public void update(JSONArray dataset) {
        mDataset = dataset ;
    }

    @Override
    public BucketsAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_listings_item, viewGroup, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BucketsAdapter.MainViewHolder viewHolder, int i) {
        String id ="";
        String img_url ;
        int category_id ;
        String experience_category ;

        try {
            id = mDataset.getJSONObject(i).getJSONObject("experience").getString("id") ;

            img_url = Definitions.APIdomain + mDataset.getJSONObject(i).getJSONObject("experience").getString("experience_image") ;
//            category_id = mDataset.getJSONObject(i).getJSONObject("experience").getJSONArray("category_ids").getInt(0);
            experience_category = mDataset.getJSONObject(i).getJSONObject("experience").getJSONArray("experience_categories").getJSONObject(0).getString("name");

            viewHolder.txt_locationname.setText(mDataset.getJSONObject(i).getJSONObject("experience").getString("address_line_2"));
            viewHolder.txt_name.setText(mDataset.getJSONObject(i).getJSONObject("experience").getString("name"));
            viewHolder.txt_calendar.setText(mActions.getDays(mDataset.getJSONObject(i).getJSONObject("experience").getJSONArray("days_of_week")));

            if(mDataset.getJSONObject(i).getJSONObject("experience").getBoolean("bucketed"))
            {
                viewHolder.img_bucket.setImageResource(R.drawable.icon_bucket_on);
            }
            else
            {
                viewHolder.img_bucket.setImageResource(R.drawable.icon_bucket_off);
            }

//            Picasso.with(mContext).load(img_url).resize(600, 400).onlyScaleDown().into(viewHolder.img_background);
//            Picasso.with(mContext).load(img_url).fit().centerCrop().into(viewHolder.img_background);
            Glide.with(mContext).load(img_url).centerCrop().into(viewHolder.img_background);
//            Picasso.with(mContext).load(mActions.getCategorySmallImage2(experience_category)).resize(40, 40).into(viewHolder.img_type);
            Glide.with(mContext).load(mActions.getCategorySmallImage2(experience_category)).override(40, 40).into(viewHolder.img_type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalID = id;
        final int finalPosition = i;

        //BUCKET CLICK LISTENER
        viewHolder.img_bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "ID: " + finalID + "", Toast.LENGTH_SHORT).show();
                eventbucket(finalID, finalPosition, viewHolder);
            }
        });

        //ITEM CLICK LISTENER
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Position: " + finalPosition + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, Experience.class);
                intent.putExtra("experience_id",finalID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_type;
        public ImageView img_bucket;
        public ImageView img_background;
        public TextView txt_name;
        public TextView txt_locationname;
        public TextView txt_calendar;

        public MainViewHolder(View v) {
            super(v);
            img_type = (ImageView) v.findViewById(R.id.listview_listings_image_type);
            img_bucket = (ImageView) v.findViewById(R.id.listview_listings_image_bucket);
            img_background = (ImageView) v.findViewById(R.id.listview_listings_image_background);
            txt_name = (TextView) v.findViewById(R.id.listview_listings_textview_eventname);
            txt_locationname = (TextView) v.findViewById(R.id.listview_listings_textview_locationname);
            txt_calendar = (TextView) v.findViewById(R.id.listview_listings_textview_calendar);

            //SETTING UP EXTERNAL FONT
            String fontPath = "fonts/ProximaNova-Reg.ttf";
            String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
            String fontPath_RegItalic = "fonts/ProximaNova-RegItalic.otf";
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            Typeface tf_Bold = Typeface.createFromAsset(mContext.getAssets(), fontPath_Bold);
            Typeface tf_RegItalic = Typeface.createFromAsset(mContext.getAssets(), fontPath_RegItalic);
            txt_locationname.setTypeface(tf);
            txt_calendar.setTypeface(tf);
            txt_name.setTypeface(tf_Bold);
        }
    }

    private void eventbucket(final String id, final int position, final BucketsAdapter.MainViewHolder viewHolder)
    {
        String path = "";
        try {
            if(mDataset.getJSONObject(position).getJSONObject("experience").getBoolean("bucketed"))
            {
                path = "bucket_list_items/remove_from_bucket_list";
            }
            else
            {
                path = "bucket_list_items/add_to_bucket_list";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!mActions.checkInternetConnection(mContext))
        {
            mActions.displayToast(mContext, "No Internet Connection");
        }
        else {

            progressDialog.show();

            //Define Headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("X-User-Email", prefs.getString("email", null));
            headers.put("X-User-Token", prefs.getString("token", null));

            JSONObject jbody = new JSONObject();
            try {
                jbody.put("experience_id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserAPI.post_StringResp(path, jbody, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(mContext, "Bucket change for id: "+id+" was successful !");

//                            PUT CHANGED VALUES AFTER SUCCESSFUL REQUEST
                            try {
                                if(mDataset.getJSONObject(position).getJSONObject("experience").getBoolean("bucketed"))
                                {
                                    mDataset.getJSONObject(position).getJSONObject("experience").put("bucketed", false);
                                    viewHolder.img_bucket.setImageResource(R.drawable.icon_bucket_off);
                                }
                                else
                                {
                                    viewHolder.img_bucket.setImageResource(R.drawable.icon_bucket_on);
                                    mDataset.getJSONObject(position).getJSONObject("experience").put("bucketed", true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                            mActions.displayToast(mContext, "Bucket change for id: "+ id +" was unsuccessful !");
                        }
                    });
        }


    }
}
