package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.programize.wonderush.Activities.Browsing.Listings;
import com.programize.wonderush.R;

public class SearchFragment extends DialogFragment {

    public static SearchFragment newInstance()
    {
        SearchFragment f = new SearchFragment();

        Bundle bdl = new Bundle();

//        bdl.putString("avatar", avatar);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.listview_search_item, container, false) ;

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setCancelable(false);
//        getDialog().setCanceledOnTouchOutside(false);
//
//        //TRANSPARENT BACKGROUND
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));


        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        final EditText mEdittext = (EditText)view.findViewById(R.id.listview_search_edit);
        mEdittext.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/ProximaNova-Reg.ttf"));
        mEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Intent new_intent = new Intent(getActivity().getApplicationContext(), Listings.class);
                    new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    new_intent.putExtra("search", mEdittext.getText().toString());
                    getActivity().startActivity(new_intent);

                    return true;
                }
                return false;
            }
        });





        return view;
    }


}
