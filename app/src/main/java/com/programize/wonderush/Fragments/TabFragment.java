package com.programize.wonderush.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programize.wonderush.Adapters.BadgesAdapter;
import com.programize.wonderush.R;

import org.json.JSONArray;
import org.json.JSONException;


public class TabFragment extends Fragment {

    private static Context mContext ;

    public static final TabFragment newInstance(String message)
    {
        TabFragment f = new TabFragment();
        Bundle bdl = new Bundle();
        bdl.putString("json", message);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badges, container, false);
        mContext = getActivity();

        String str = getArguments().getString("json");
        JSONArray mArray = null;
        try {
            mArray = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_badges_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        BadgesAdapter mAdapter = new BadgesAdapter(mContext, mArray);
        recyclerView.setAdapter(mAdapter);


        return view;
    }
}