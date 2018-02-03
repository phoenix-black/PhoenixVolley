package com.blackphoenix.phoenixvolley.v1;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Praba on 7/27/2017.
 */
public abstract class VolleyPost {

    private Context _context;
    private static String TAG = VolleyPost.class.getSimpleName();
    private Response.Listener<JSONObject> volleyResponseListener;
    private VolleyErrorListener volleyErrorListener;

    public VolleyPost(Context context){
        this._context = context;

        volleyResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onVolleyResponse(response);
            }
        };

        volleyErrorListener = new VolleyErrorListener(_context) {
            @Override
            public void onError(VolleyError volleyError, VolleyDecodedError volleyDecodedError) {
                onVolleyErrorResponse(volleyError,volleyDecodedError);
            }
        };

    }

    /**
     *
     * @param params
     * @param httpPostURL
     * @throws NullPointerException
     */

    public void submitRequest(Map<String, String> params, String httpPostURL) throws NullPointerException {
        submitRequest(_context,params,httpPostURL,volleyResponseListener,volleyErrorListener,-1);
    }

    /**
     *
     * @param params
     * @param httpPostURL
     * @param retryTimeMillSec
     * @throws NullPointerException
     */

    public void submitRequest(Map<String, String> params, String httpPostURL, int retryTimeMillSec) throws NullPointerException {
        submitRequest(_context,params,httpPostURL,volleyResponseListener,volleyErrorListener,retryTimeMillSec);
    }

    /**
     *
     * @param context
     * @param params
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @throws NullPointerException
     */

    public void submitRequest(Context context, Map<String, String> params, String httpPostURL,
                              Response.Listener<JSONObject> responseListener,
                              Response.ErrorListener errorListener) throws NullPointerException {

        submitRequest(context,params,httpPostURL,responseListener,errorListener,-1);

    }

    /**
     *
     * @param context
     * @param params
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @param retryTimeMilliSec
     * @throws NullPointerException
     */

    public static void submitRequest(Context context, Map<String, String> params, String httpPostURL,
                              Response.Listener<JSONObject> responseListener,
                              Response.ErrorListener errorListener, int retryTimeMilliSec) throws NullPointerException {


        VolleyJsonRequest httpRequest = new VolleyJsonRequest(Request.Method.POST, httpPostURL,
                params, responseListener, errorListener);

        if(retryTimeMilliSec != -1 ) {

            // NOTE: Set minimum retry time to 5 seconds
            if(retryTimeMilliSec <= 5000){
                Log.w(TAG,"Input retry time "+ retryTimeMilliSec +" is invalid. Setting retry time to 5 seconds");
                retryTimeMilliSec = 5000;
            }

            httpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    retryTimeMilliSec,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(httpRequest);

    }


    public abstract void onVolleyResponse(JSONObject response);
    public abstract void onVolleyErrorResponse(VolleyError volleyError, VolleyDecodedError volleyDecodedError);
}
