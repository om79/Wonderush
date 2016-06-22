package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LeaveReview extends DialogFragment {

    private String TAG = "TAG - LEAVE REVIEW FRAGMENT - " ;
    private myActions mActions = new myActions();
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    ImageView star1 ;
    ImageView star2 ;
    ImageView star3 ;
    ImageView star4 ;
    ImageView star5 ;

    int stars = 0 ;

    private Button btn_submit ;
    private Button btn_cancel ;
    private Button btn_cancel2 ;
    private EditText review;

    private TextView txt_success ;

    private RelativeLayout layout_review;
    private RelativeLayout layout_success;

    public static LeaveReview newInstance(String avatar, String experience_id, String title)
    {
        LeaveReview f = new LeaveReview();

        Bundle bdl = new Bundle();

        bdl.putString("avatar", avatar);
        bdl.putString("experience_id", experience_id);
        bdl.putString("title", title);

        f.setArguments(bdl);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_review, container, false) ;

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        //TRANSPARENT BACKGROUND
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        //SET UP SHARED PREFERENCES
        prefs = getActivity().getApplicationContext().getSharedPreferences(Definitions.sharedprefname, getActivity().MODE_PRIVATE);

        //INITIALIZE WIDGETS
        TextView header = (TextView)view.findViewById(R.id.reviewfragment_textview_header);
        review = (EditText)view.findViewById(R.id.reviewfragment_edittext_review);
        btn_submit = (Button)view.findViewById(R.id.reviewfragment_button_submit);
        btn_cancel = (Button)view.findViewById(R.id.reviewfragment_button_cancel);
        btn_cancel2 = (Button)view.findViewById(R.id.reviewfragment_button_cancel2);
        final ImageView avatar = (ImageView)view.findViewById(R.id.reviewfragment_imageview_avatar);
        ImageView avatar_back = (ImageView)view.findViewById(R.id.reviewfragment_imageview_avatar_back);

        star1 = (ImageView)view.findViewById(R.id.reviewfragment_imageview_star1);
        star2 = (ImageView)view.findViewById(R.id.reviewfragment_imageview_star2);
        star3 = (ImageView)view.findViewById(R.id.reviewfragment_imageview_star3);
        star4 = (ImageView)view.findViewById(R.id.reviewfragment_imageview_star4);
        star5 = (ImageView)view.findViewById(R.id.reviewfragment_imageview_star5);

        layout_review = (RelativeLayout)view.findViewById(R.id.reviewfragment_layout_review);
        layout_success = (RelativeLayout)view.findViewById(R.id.reviewfragment_layout_success);

        txt_success = (TextView)view.findViewById(R.id.reviewfragment_textview_success);

        progressDialog = MyProgressDialog.ctor(getActivity());

        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        String fontPath_Bold = "fonts/ProximaNova-Bold.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        Typeface tf_bold = Typeface.createFromAsset(getActivity().getAssets(), fontPath_Bold);
        header.setTypeface(tf_bold);
        review.setTypeface(tf);
        btn_submit.setTypeface(tf_bold);
        btn_cancel.setTypeface(tf_bold);
        btn_cancel2.setTypeface(tf_bold);
        txt_success.setTypeface(tf);

        //SET UP AVATAR IMAGE
        if(getArguments().getString("avatar").equals("no_avatar"))
        {
//            Picasso.with(getActivity()).load(R.drawable.male_avatar).into(avatar);
//            Glide.with(getActivity()).load(R.drawable.male_avatar).into(avatar);
            Glide.with(getActivity()).load(R.drawable.male_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    avatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        else
        {
//            Picasso.with(getActivity()).load(Definitions.APIdomain + getArguments().getString("avatar")).into(avatar);
//            Glide.with(getActivity()).load(Definitions.APIdomain + getArguments().getString("avatar")).into(avatar);
            Glide.with(getActivity()).load(Definitions.APIdomain + getArguments().getString("avatar")).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    avatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
//        Picasso.with(getActivity()).load(R.drawable.no_pic).into(avatar_back);
        avatar_back.setImageResource(R.drawable.no_pic);

        txt_success.setText("Thank you for your feedback, we really appreciate you taking the time to review " + getArguments().getString("title") + ".");

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars = 1;
                star1.setImageResource(R.drawable.icon_star_on);
                star2.setImageResource(R.drawable.icon_star_off);
                star3.setImageResource(R.drawable.icon_star_off);
                star4.setImageResource(R.drawable.icon_star_off);
                star5.setImageResource(R.drawable.icon_star_off);
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars =2 ;
                star1.setImageResource(R.drawable.icon_star_on);
                star2.setImageResource(R.drawable.icon_star_on);
                star3.setImageResource(R.drawable.icon_star_off);
                star4.setImageResource(R.drawable.icon_star_off);
                star5.setImageResource(R.drawable.icon_star_off);
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars =3 ;
                star1.setImageResource(R.drawable.icon_star_on);
                star2.setImageResource(R.drawable.icon_star_on);
                star3.setImageResource(R.drawable.icon_star_on);
                star4.setImageResource(R.drawable.icon_star_off);
                star5.setImageResource(R.drawable.icon_star_off);
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars =4 ;
                star1.setImageResource(R.drawable.icon_star_on);
                star2.setImageResource(R.drawable.icon_star_on);
                star3.setImageResource(R.drawable.icon_star_on);
                star4.setImageResource(R.drawable.icon_star_on);
                star5.setImageResource(R.drawable.icon_star_off);
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars =5 ;
                star1.setImageResource(R.drawable.icon_star_on);
                star2.setImageResource(R.drawable.icon_star_on);
                star3.setImageResource(R.drawable.icon_star_on);
                star4.setImageResource(R.drawable.icon_star_on);
                star5.setImageResource(R.drawable.icon_star_on);
            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                layout_review.setVisibility(View.GONE);
//                layout_success.setVisibility(View.VISIBLE);

            }
        });

        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent().putExtra("experience_id",getArguments().getString("experience_id")));
                dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stars!=0 && review.getText().toString().length()!=0)
                {
                    leave_review(getArguments().getString("experience_id"));
                }
                else
                {
                    mActions.displayToast(getActivity(), "Please enter all values");
                }
            }
        });





        return view;
    }


    private void leave_review(String experience_id)
    {
        if(!mActions.checkInternetConnection(getActivity()))
        {
            mActions.displayToast(getActivity(), "No Internet Connection");
        }
        else
        {
            progressDialog.show();

            JSONObject jbody = new JSONObject();
            JSONObject experience_class_review = new JSONObject();
            try {
                experience_class_review.put("review_text", review.getText().toString());
                experience_class_review.put("review_rating", stars);
                experience_class_review.put("experience_id", experience_id);
                jbody.put("experience_class_review", experience_class_review);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put( "Content-Type", "application/json" );
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            Log.v(TAG, jbody.toString());

            UserAPI.post_StringResp("experience_class_reviews", jbody, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            layout_review.setVisibility(View.GONE);
                            layout_success.setVisibility(View.VISIBLE);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(getActivity(), "Error Occurred");
                        }
                    });
        }
    }

}
