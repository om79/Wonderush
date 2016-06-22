package com.programize.wonderush.Utilities.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAPI {

    private static String contentTypeJSON = "application/json" ;
    private static String acceptJSON = "application/json" ;



    /*********************************************************
     *                          POST                         *
     *********************************************************/


//*****************************************************************************************************************************
    //POST
    //REQUEST   :   HEADERS & PARAMS & JSON_BODY
    //RESPONSE  :   STRING
    public static void post_StringResp(String path,JSONObject jbody, Map <String,String> headers,Map <String,String> params,
                                      Response.Listener<String> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequestReceiveString(url, Request.Method.POST, jbody, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************


//*****************************************************************************************************************************
    //POST
    //REQUEST   :   HEADERS & PARAMS & JSON_BODY
    //RESPONSE  :   JSONObject
    public static void post_JsonResp(String path,JSONObject jbody, Map <String,String> headers,Map <String,String> params,
                                       Response.Listener<JSONObject> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequest(url, Request.Method.POST, jbody, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************



    /****************************
    *     POST                 *
    *REQUEST IS JSON OBJECT    *
    *RESPONSE IS JSON OBJECT   *
    ***************************/
    public static void post_JsonReq_JsonResp(String path, JSONObject requestObj, Response.Listener<JSONObject> responseHandler, Response.ErrorListener errorHandler) {
        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;
        //Define Headers
        Map<String,String> headers = new HashMap<String, String>();
        headers.put( "Content-Type", contentTypeJSON );
        headers.put( "Accept", acceptJSON );
        //Send Request
        VolleyWebLayer.sendJSONObjectRequest(url, Request.Method.POST, requestObj, responseHandler, errorHandler, headers, null);
    }






    /*********************************************************
     *                          GET                          *
     *********************************************************/

//*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS
    //RESPONSE  :   JSONONJECT
    public static void get_JsonObjResp(String path,Map <String,String> headers,Map <String,String> params,
                                         Response.Listener<JSONObject> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequest(url, Request.Method.GET, null, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************

//*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS
    //RESPONSE  :   JSONARRAY
    public static void get_JsonArrayResp(String path,Map <String,String> headers,Map <String,String> params,
                                                  Response.Listener<JSONArray> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONArrayRequest(url, Request.Method.GET, null, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************

//*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS
    //RESPONSE  :   JSONARRAY
    public static void get_JsonArrayResp2(String path,Map <String,String> headers,Map <String,String> params,String search,
                                         Response.Listener<JSONArray> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONArrayRequest2(url, Request.Method.GET, null, responseHandler, errorHandler, headers, params, search);
    }
//*****************************************************************************************************************************

    //*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS & PATH PARAMS
    //RESPONSE  :   JSONARRAY
    public static void get_ParamReq_JsonArrayResp(String path,String path_param,Map <String,String> headers,Map <String,String> params,
                                         Response.Listener<JSONArray> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path + "/" + path_param;

        //Send Request
        VolleyWebLayer.sendJSONArrayRequest(url, Request.Method.GET, null, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************


    //*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS & PATH PARAMS
    //RESPONSE  :   JSONOBJECT
    public static void get_ParamReq_JsonObjResp(String path,String path_param, Map <String,String> headers,Map <String,String> params,
                                         Response.Listener<JSONObject> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path + "/" + path_param;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequest(url, Request.Method.GET, null, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************

    //*****************************************************************************************************************************
    //GET
    //REQUEST   :   HEADERS & PARAMS & JSON_BODY
    //RESPONSE  :   STRING
    public static void get_StringResp(String path,JSONObject jbody, Map <String,String> headers,Map <String,String> params,
                                       Response.Listener<String> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequestReceiveString(url, Request.Method.GET, jbody, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************


    /*********************************************************
     *                          PUT                          *
     *********************************************************/

//*****************************************************************************************************************************
    //PUT
    //REQUEST   :   HEADERS & PARAMS & JSON_BODY
    //RESPONSE  :   STRING
    public static void put_StringResp(String path,JSONObject jbody, Map <String,String> headers,Map <String,String> params,
                                       Response.Listener<String> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequestReceiveString(url, Request.Method.PUT, jbody, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************



    /*********************************************************
     *                          DELETE                          *
     *********************************************************/

//*****************************************************************************************************************************
    //DELETE
    //REQUEST   :   HEADERS & PARAMS
    //RESPONSE  :   STRING
    public static void delete_StringResp(String path, Map <String,String> headers,Map <String,String> params,
                                      Response.Listener<String> responseHandler, Response.ErrorListener errorHandler) {

        //Setup URL Request
        String url = Definitions.APIdomain + "/" + path;

        //Send Request
        VolleyWebLayer.sendJSONObjectRequestReceiveString(url, Request.Method.DELETE, null, responseHandler, errorHandler, headers, params);
    }
//*****************************************************************************************************************************


















//OLD

    //Get info based on class Name and where string
//    public static void get_ParamReq_JsonArrayResp(String path,String token, String email,
//                               Response.Listener<JSONArray> responseHandler, Response.ErrorListener errorHandler) {
//
//        //Setup URL Request
//        String url = Definitions.APIdomain + "/" + path;
//
//        //Define Headers
//        Map <String,String> headers = new HashMap <String, String>();
//        headers.put( "Accept", contentTypeJSON );
//        headers.put( "X-User-Email", email );
//        headers.put( "X-User-Token", token );
//
//        //Send Request
//        VolleyWebLayer.sendJSONArrayRequest(url, Request.Method.GET, null, responseHandler, errorHandler, headers, null);
//    }

}
