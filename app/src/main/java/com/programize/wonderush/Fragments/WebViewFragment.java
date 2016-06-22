package com.programize.wonderush.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.programize.wonderush.R;




public class WebViewFragment extends DialogFragment {

    private static Context mContext ;

    public static WebViewFragment newInstance(String url_path)
    {
        WebViewFragment f = new WebViewFragment();

        Bundle bdl = new Bundle(1);

        bdl.putString("url_path", url_path);

        f.setArguments(bdl);

        return f;
    }
    public  WebViewFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        mContext = getActivity();

        Log.v("TAG", "eimai mesa");

        WebView webView = (WebView)view.findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getArguments().getString("url_path"));
//        webView.loadUrl("www.google.gr");

        return view;
    }
}