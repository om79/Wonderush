package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

public class ShareInfo extends DialogFragment {

    private String message ;


    public static ShareInfo newInstance(String description, String image_url, String title, String experience_id)
    {
        ShareInfo f = new ShareInfo();

        Bundle bdl = new Bundle();

        bdl.putString("description", description);
        bdl.putString("image_url", image_url);
        bdl.putString("title", title);
        bdl.putString("experience_id", experience_id);

        f.setArguments(bdl);

        return f;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share, container, false);

//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        ImageView facebook = (ImageView)rootView.findViewById(R.id.fragment_share_image_facebook);
        ImageView twitter = (ImageView)rootView.findViewById(R.id.fragment_share_image_twitter);
        ImageView whatsup = (ImageView)rootView.findViewById(R.id.fragment_share_image_whats_up);
        ImageView mail = (ImageView)rootView.findViewById(R.id.fragment_share_image_mail);

        message = "Check out this awesome experience! \n" + Definitions.APIdomain + "/experiences/" +getArguments().getString("experience_id");

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(message);
                Uri uri = Uri.parse(tweetUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                dismiss();
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                smsIntent.setData(Uri.parse("sms:"));
//                smsIntent.putExtra("sms_body", message);
//                startActivity(smsIntent);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(sharingIntent);
                dismiss();
            }
        });

        whatsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getActivity().getApplicationContext().getPackageManager();

                //Check if package exists or not. If not then code in catch block will be called
                try {

                    Intent whatsAppIntent = new Intent();
                    whatsAppIntent.setAction(Intent.ACTION_SEND);

                    whatsAppIntent.setType("text/plain");
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    whatsAppIntent.setPackage("com.whatsapp");
                    whatsAppIntent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(whatsAppIntent);

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(getActivity());

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(getArguments().getString("title"))
                            .setContentDescription(message)
                            .setContentUrl(Uri.parse(Definitions.APIdomain+"/experiences/" + getArguments().getString("experience_id")))
                            .setImageUrl(Uri.parse(getArguments().getString("image_url")))
                            .build();
                    shareDialog.show(linkContent);
                dismiss();
            }
        });




        setDialogPosition();

        return rootView;
    }


    private void setDialogPosition() {
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP | Gravity.END);

        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dpToPx(45);
        window.setAttributes(params);
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
