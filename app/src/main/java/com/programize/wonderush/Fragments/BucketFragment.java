package com.programize.wonderush.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programize.wonderush.Adapters.BadgesAdapter;
import com.programize.wonderush.Adapters.BucketsAdapter;
import com.programize.wonderush.R;

import org.json.JSONArray;
import org.json.JSONException;


public class BucketFragment extends Fragment {

    private static Context mContext ;

    private String mResponse ;
    private String mDay ;
    private int mDay_int ;
    private String mLocation ;
    private Boolean mLocation_soring ;
    private BucketsAdapter mAdapter ;
    private RecyclerView mRecyclerView;
    private TextView txt_message ;

    public static final BucketFragment newInstance(String response, String day, String location, Boolean location_sorting)
    {
        int day_int = -1;
        if(day.equals("all"))
        {
            day_int = -1 ;
        }
        else if(day.equals("SUN"))
        {
            day_int = 0 ;
        }
        else if(day.equals("MON"))
        {
            day_int = 1 ;
        }
        else if(day.equals("TUE"))
        {
            day_int = 2 ;
        }
        else if(day.equals("WED"))
        {
            day_int = 3 ;
        }
        else if(day.equals("THU"))
        {
            day_int = 4 ;
        }
        else if(day.equals("FRI"))
        {
            day_int = 5 ;
        }
        else if(day.equals("SAT"))
        {
            day_int = 6 ;
        }

        BucketFragment f = new BucketFragment();
        Bundle bdl = new Bundle();
        bdl.putString("response", response);
        bdl.putInt("day", day_int);
        bdl.putString("location", location);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucket, container, false);
        mContext = getActivity();

        mResponse = getArguments().getString("response");
        mLocation = getArguments().getString("location").toLowerCase() ;
        mDay_int = getArguments().getInt("day");

        Log.v("TAG loc", mLocation);

        txt_message = (TextView)view.findViewById(R.id.bucketscreen_textview_message);

        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.bucketscreen_recycler_bucket);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        filter();

        return view;
    }

    private void filter()
    {
        JSONArray mArray = null;
        JSONArray filtered_Array = new JSONArray();
        try {
            mArray = new JSONArray(mResponse);
            if(getArguments().getInt("day")==-1)
            {
                for(int i = 0 ;i <mArray.length();i++)
                {
                    if(mArray.getJSONObject(i).getJSONObject("experience").getString("area").toLowerCase().contains(mLocation))
                    {
                        filtered_Array.put(mArray.getJSONObject(i));
                    }
                }
            }
            else
            {
                for(int i = 0 ;i <mArray.length();i++)
                {
                    if(mArray.getJSONObject(i).getJSONObject("experience").getString("area").toLowerCase().contains(mLocation))
                    {
                        for(int j = 0;j<mArray.getJSONObject(i).getJSONObject("experience").getJSONArray("days_of_week").length();j++)
                        {
                            if(mArray.getJSONObject(i).getJSONObject("experience").getJSONArray("days_of_week").getInt(j) == mDay_int)
                            {
                                filtered_Array.put(mArray.getJSONObject(i));
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(filtered_Array.length()==0)
        {
            Log.v("TAG bucket", "empty");
            txt_message.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            Log.v("TAG bucket", "NOT empty");
            txt_message.setVisibility(View.GONE);
            mAdapter = new BucketsAdapter(mContext, filtered_Array);
            mRecyclerView.setAdapter(mAdapter);
        }


    }
}