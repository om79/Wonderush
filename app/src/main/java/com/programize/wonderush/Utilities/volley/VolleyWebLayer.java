package com.programize.wonderush.Utilities.volley;


import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyWebLayer {

    private static final int reqQueueCacheSize = 1024*1024; // 1MB capacity
    private static RequestQueue mRequestQueue;
    private static int TIMEOUT = 15000; // 15 seconds

    /**
     * Initialize network for request
     */
    public static synchronized void initialize() {
        //--- Instantiate the cache
        Cache cache = new DiskBasedCache(Definitions.appContext.getCacheDir(), reqQueueCacheSize); // 1MB cap
        //--- Set up the network to use HttpURLConnection as the HTTP client.
        HttpStack stack = new HurlStack(); // ...use HttpURLConnection for stack.
        Network network = new BasicNetwork(stack);
        //--- Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        //--- Start the queue
        mRequestQueue.start();
    }


    /**
     * Receive JSON Object
     */
    public static synchronized void sendJSONObjectRequest(String url, final int type, JSONObject jsonBody,
                                                          Response.Listener <JSONObject> responseHandler, Response.ErrorListener errorHandler,
                                                          final Map <String,String> headers, final Map<String,String> params) {

        url = fixUrl(url, type, params);

        // Request a json response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(type, url, jsonBody, responseHandler, errorHandler) {
            @Override
            protected Map <String,String> getParams() throws AuthFailureError {
                if(params==null) return new HashMap<String, String>();
                Map <String,String> totalParams = super.getParams();
                if (totalParams==null || totalParams.isEmpty()) {
                    totalParams = new HashMap <String, String>();
                }
                if(params!=null) totalParams.putAll(params);
                return totalParams;
            }
            @Override
            public Map <String,String> getHeaders() throws AuthFailureError {
                Map <String,String> totalHeaders = super.getHeaders();
                if (totalHeaders==null || totalHeaders.isEmpty()) {
                    totalHeaders = new HashMap <String, String>();
                }
                if(headers!=null) totalHeaders.putAll(headers);
                return totalHeaders;
            }
        };

        // Add the request to the RequestQueue.
        int socketTimeout = TIMEOUT;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        mRequestQueue.add(jsObjRequest);
    }

    /**
     * Receive JSON Array
     */
    public static synchronized void sendJSONArrayRequest(String url, final int type, JSONObject jsonBody,
                                                          Response.Listener <JSONArray> responseHandler, Response.ErrorListener errorHandler,
                                                          final Map <String,String> headers, final Map<String,String> params) {

        url = fixUrl(url, type, params);

//        Log.v("TAG url", url);

        // Request a json response from the provided URL.
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(type, url, jsonBody, responseHandler, errorHandler) {
            @Override
            protected Map <String,String> getParams() throws AuthFailureError {
                if(params==null) return new HashMap<String, String>();
                Map <String,String> totalParams = super.getParams();
                if (totalParams==null || totalParams.isEmpty()) {
                    totalParams = new HashMap <String, String>();
                }
                if(params!=null) totalParams.putAll(params);
                return totalParams;
            }
            @Override
            public Map <String,String> getHeaders() throws AuthFailureError {
                Map <String,String> totalHeaders = super.getHeaders();
                if (totalHeaders==null || totalHeaders.isEmpty()) {
                    totalHeaders = new HashMap <String, String>();
                }
                if(headers!=null) totalHeaders.putAll(headers);
                return totalHeaders;
            }
        };

        // Add the request to the RequestQueue.
        int socketTimeout = TIMEOUT;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        mRequestQueue.add(jsObjRequest);
    }


    public static synchronized void sendJSONArrayRequest2(String url, final int type, JSONObject jsonBody,
                                                         Response.Listener <JSONArray> responseHandler, Response.ErrorListener errorHandler,
                                                         final Map <String,String> headers, final Map<String,String> params,final String search) {

        url = fixUrl2(url, type, params, search);

//        Log.v("TAG", url);

        // Request a json response from the provided URL.
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(type, url, jsonBody, responseHandler, errorHandler) {
            @Override
            protected Map <String,String> getParams() throws AuthFailureError {
                if(params==null) return new HashMap<String, String>();
                Map <String,String> totalParams = super.getParams();
                if (totalParams==null || totalParams.isEmpty()) {
                    totalParams = new HashMap <String, String>();
                }
                if(params!=null) totalParams.putAll(params);
                return totalParams;
            }
            @Override
            public Map <String,String> getHeaders() throws AuthFailureError {
                Map <String,String> totalHeaders = super.getHeaders();
                if (totalHeaders==null || totalHeaders.isEmpty()) {
                    totalHeaders = new HashMap <String, String>();
                }
                if(headers!=null) totalHeaders.putAll(headers);
                return totalHeaders;
            }
        };

        // Add the request to the RequestQueue.
        int socketTimeout = TIMEOUT;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        mRequestQueue.add(jsObjRequest);
    }

    /**
     * Receive String
     */
    public static synchronized void sendJSONObjectRequestReceiveString(String url, final int type, JSONObject jsonBody,
                                                                       Response.Listener <String> responseHandler, Response.ErrorListener errorHandler,
                                                                       final Map <String,String> headers, final Map <String,String> params) {

        url = fixUrl(url, type, params);

        // Request a json response from the provided URL.
        JsonObjectRequestWithStringResponse jsObjRequest =
                new JsonObjectRequestWithStringResponse(type, url, jsonBody, responseHandler, errorHandler) {
                    @Override
                    protected Map <String,String> getParams() throws AuthFailureError {
                        if(params==null) return new HashMap <String, String>();
                        Map <String,String> totalParams = super.getParams();
                        if (totalParams==null || totalParams.isEmpty()) {
                            totalParams = new HashMap <String, String>();
                        }
                        if(params!=null) totalParams.putAll(params);
                        return totalParams;
                    }
                    @Override
                    public Map <String,String> getHeaders() throws AuthFailureError {
                        Map <String,String> totalHeaders = super.getHeaders();
                        if (totalHeaders==null || totalHeaders.isEmpty()) {
                            totalHeaders = new HashMap <String, String>();
                        }
                        if(headers!=null) totalHeaders.putAll(headers);
                        return totalHeaders;
                    }
                };
    // Add the request to the RequestQueue.
            int socketTimeout = TIMEOUT;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            mRequestQueue.add(jsObjRequest);
        }


    public static synchronized void imageRequest(String url,final Response.Listener<Bitmap> listener){
        ImageRequest request=new ImageRequest(url,new Response.Listener<Bitmap>(){
            @Override public void onResponse(    Bitmap bitmap){
                listener.onResponse(bitmap);
            }
        }
                ,0,0,null,new Response.ErrorListener(){
            public void onErrorResponse(    VolleyError error){
                Log.d("Volley Error",error.toString());
            }
        }
        );
        mRequestQueue.add(request);
    }



    /*********************
     * PRIVATE FUNCTIONS *
     *********************/

    private static String fixUrl(String url, int type, Map <String,String> params) {
        if( url.endsWith("/") ) url = url.substring(0, url.length()-1);
//        if( params!=null && type != Request.Method.POST && !url.contains("?")) {
        if( params!=null && !url.contains("?")) {
            url += "?";
            for( String key : params.keySet() ) {
                try { url += URLEncoder.encode(key,"UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            url = url.substring(0, url.length()-1);
        }
        return url;
    }

    private static String fixUrl2(String url, int type, Map <String,String> params, String search) {
        if( url.endsWith("/") ) url = url.substring(0, url.length()-1);
        if( params!=null && type != Request.Method.POST && !url.contains("?")) {
            url += "?";
            for( String key : params.keySet() ) {
                try { url += URLEncoder.encode(key,"UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if(!search.equals(""))
            {
                String[] parts = search.split(" ");
                for(int i = 0 ;i<parts.length;i++)
                {
                    try { url += URLEncoder.encode("q[name_cont_all][]","UTF-8") + "=" + URLEncoder.encode(parts[i], "UTF-8") + "&";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }


            url = url.substring(0, url.length()-1);
        }
        return url;
    }

}
