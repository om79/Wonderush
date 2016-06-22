package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

public class ShareInvite extends DialogFragment {

    private String message1 ;
    private String message1_fb ;
    private String message2 ;
    private String message3 ;

    public static ShareInvite newInstance(String referal, int first_month_subscription)
    {
        ShareInvite f = new ShareInvite();

        Bundle bdl = new Bundle();

        bdl.putString("referal", referal);
        bdl.putInt("first_month_subscription", first_month_subscription);

        f.setArguments(bdl);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView facebook = (ImageView)rootView.findViewById(R.id.fragment_share_image_facebook);
        ImageView twitter = (ImageView)rootView.findViewById(R.id.fragment_share_image_twitter);
        ImageView whatsup = (ImageView)rootView.findViewById(R.id.fragment_share_image_whats_up);
        ImageView mail = (ImageView)rootView.findViewById(R.id.fragment_share_image_mail);

        message1 = "I joined Wonderush.com";
        message1_fb = "I joined @Wonderush";
        message2 = " and now get access to unlimited classes and experiences free with my membership.Try it here: ";
        message3 = Definitions.APIdomain +"/?refcode="+getArguments().getString("referal") ;


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(message1_fb + message2 + message3);
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
//                smsIntent.putExtra("sms_body", message1 + message2 + message3);
//                startActivity(smsIntent);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message1 + message2 + message3);
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
                    whatsAppIntent.putExtra(Intent.EXTRA_TEXT, message1 + message2 + message3);
                    startActivity(whatsAppIntent);
                    dismiss();

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(getActivity());

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Live More Awesome with Me!")
                        .setContentDescription(message1_fb + message2)
                        .setContentUrl(Uri.parse(message3))
                        .setImageUrl(Uri.parse(Definitions.APIdomain))
                        .build();
                shareDialog.show(linkContent);
                dismiss();
            }
        });






        return rootView;
    }
}
