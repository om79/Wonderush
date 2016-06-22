package com.programize.wonderush.Utilities.volley;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class JsonObjectRequestWithStringResponse extends JsonRequest <String> {

	/**
	 * Creates a new request.
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param requestBody A {@link String} to post with the request. Null is allowed and
	 *   indicates no parameters will be posted along with request.
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public JsonObjectRequestWithStringResponse(int method, String url, String requestBody,
			Listener <String> listener, ErrorListener errorListener) {
		super(method, url, requestBody, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * @param url URL to fetch the JSON from
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public JsonObjectRequestWithStringResponse(String url, Listener <String> listener, ErrorListener errorListener) {
	    super(Method.GET, url, null, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public JsonObjectRequestWithStringResponse(int method, String url, Listener <String> listener, ErrorListener errorListener) {
	    super(method, url, null, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param jsonRequest A {@link String} to post with the request. Null is allowed and
	 *   indicates no parameters will be posted along with request.
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
//	public JsonObjectRequestWithStringResponse(int method, String url, String jsonRequest, Listener <String> listener, ErrorListener errorListener) {
//	    super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
//	}

	/**
	 * Creates a new request.
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
	 *   indicates no parameters will be posted along with request.
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public JsonObjectRequestWithStringResponse(int method, String url, JSONObject jsonRequest, Listener <String> listener, ErrorListener errorListener) {
	    super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
	}

	/**
	 * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
	 * <code>null</code>, <code>POST</code> otherwise.
	 *
	 * @see #MyjsonPostRequest(int, String, String, Listener, ErrorListener)
	 */
	public JsonObjectRequestWithStringResponse(String url, String jsonRequest, Listener <String> listener, ErrorListener errorListener) {
	    this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, listener, errorListener);
	}

	/**
	 * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
	 * <code>null</code>, <code>POST</code> otherwise.
	 *
	 * @see #MyjsonPostRequest(int, String, JSONObject, Listener, ErrorListener)
	 */
	public JsonObjectRequestWithStringResponse(String url, JSONObject jsonRequest, Listener <String> listener, ErrorListener errorListener) {
	    this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, listener, errorListener);
	}

	@Override
	protected Response <String> parseNetworkResponse(NetworkResponse response) {
		try {
	        String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	        return Response.success(new String(jsonString), HttpHeaderParser.parseCacheHeaders(response));
	    } catch (UnsupportedEncodingException e) {
	        return Response.error(new ParseError(e));
	    }
	}

}
