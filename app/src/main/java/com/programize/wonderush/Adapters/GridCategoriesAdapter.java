package com.programize.wonderush.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Functions.myActions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridCategoriesAdapter extends BaseAdapter {
    private JSONArray mCategories ;
    private LayoutInflater mInflater;
    private Context mContext ;
    private myActions mActions ;

    public GridCategoriesAdapter(Context context, JSONArray categories) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mActions = new myActions();
        JSONObject first_cat = new JSONObject();
        try {
            first_cat.put("id", 0);
            first_cat.put("name", "All");
            mCategories = new JSONArray();
            mCategories.put(first_cat);
            for(int i = 0 ; i <categories.length();i++)
            {
                mCategories.put(categories.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mCategories.length();
    }

    @Override
    public String getItem(int i) {
        String name = "" ;
        try {
            name = mCategories.getJSONObject(i).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public long getItemId(int i) {
        int id = 0;
        try {
            id = mCategories.getJSONObject(i).getInt("id") ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        int id = 0;
        String name = "";

        if (v == null) {
            v = mInflater.inflate(R.layout.gridview_categories_item, viewGroup, false);
            v.setTag(R.id.gridview_imageview_image1, v.findViewById(R.id.gridview_imageview_image1));
        }

        picture = (ImageView) v.getTag(R.id.gridview_imageview_image1);

        try {
            id = mCategories.getJSONObject(i).getInt("id") ;
            name = mCategories.getJSONObject(i).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Picasso.with(mContext).load(mActions.getCategoryLargeImage(id)).resize(300,300).into(picture);
//        Picasso.with(mContext).load(mActions.getCategoryLargeImage2(name)).resize(300, 300).into(picture);
        Glide.with(mContext).load(mActions.getCategoryLargeImage2(name)).into(picture);

        return v;
    }
}