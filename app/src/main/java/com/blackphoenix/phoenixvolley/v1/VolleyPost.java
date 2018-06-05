package com.blackphoenix.phoenixvolley.v1;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praba on 7/27/2017.
 */
public abstract class VolleyPost {

    private Context _context;
    private static String TAG = VolleyPost.class.getSimpleName();
    private Response.Listener<JSONObject> volleyResponseListener;
    private VolleyErrorListener volleyErrorListener;
    private boolean isRetryPolicyEnabled = true;

    public static String PARAM_VIDEO_KEY = "video_key";
    public static String PARAM_VIDEO_FILE = "video_file";

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

    public void setRetryPolicyEnabled(boolean status){
        this.isRetryPolicyEnabled = status;
    }

    /**
     *
     * @param params
     * @param httpPostURL
     * @throws NullPointerException
     */

    public void submitRequest(Map<String, String> params, String httpPostURL) throws NullPointerException {
        submitRequest(_context,params,httpPostURL,volleyResponseListener,volleyErrorListener,-1,isRetryPolicyEnabled);
    }

    /**
     *
     * @param params
     * @param httpPostURL
     * @param retryTimeMillSec
     * @throws NullPointerException
     */

    public void submitRequest(Map<String, String> params, String httpPostURL, int retryTimeMillSec) throws NullPointerException {
        submitRequest(_context,params,httpPostURL,volleyResponseListener,volleyErrorListener,retryTimeMillSec,isRetryPolicyEnabled);
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

    public static void submitRequest(Context context, Map<String, String> params, String httpPostURL,
                              Response.Listener<JSONObject> responseListener,
                              Response.ErrorListener errorListener) throws NullPointerException {

        submitRequest(context,params,httpPostURL,responseListener,errorListener,-1,true);

    }

    /**
     *
     * @param context
     * @param params
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @param isRetryPolicyEnabled
     * @throws NullPointerException
     */

    public static void submitRequest(Context context, Map<String, String> params, String httpPostURL,
                                     Response.Listener<JSONObject> responseListener,
                                     Response.ErrorListener errorListener, boolean isRetryPolicyEnabled) throws NullPointerException {

        submitRequest(context,params,httpPostURL,responseListener,errorListener,-1,isRetryPolicyEnabled);

    }

    /**
     *
     * @param context
     * @param params
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @param retryTimeMilliSec
     * @param isRetryPolicyEnabled
     * @throws NullPointerException
     */

    public static void submitRequest(Context context, Map<String, String> params, String httpPostURL,
                              Response.Listener<JSONObject> responseListener,
                              Response.ErrorListener errorListener, int retryTimeMilliSec, boolean isRetryPolicyEnabled) throws NullPointerException {


        VolleyJsonRequest httpRequest = new VolleyJsonRequest(Request.Method.POST, httpPostURL,
                params, responseListener, errorListener);

        if(isRetryPolicyEnabled) {
            if(retryTimeMilliSec != -1 ) {

                // NOTE: Set minimum retry time to 5 seconds
                if (retryTimeMilliSec <= 5000) {
                    Log.w(TAG, "Input retry time " + retryTimeMilliSec + " is invalid. Setting retry time to 5 seconds");
                    retryTimeMilliSec = 5000;
                }

                httpRequest.setRetryPolicy(new DefaultRetryPolicy(
                        retryTimeMilliSec,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
        } else {
            httpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(httpRequest);

    }


    /*******
     *
     * ******************************** VIDEO UPLOAD ******************************************************
     *
     */

    /**
     *
     * @param params
     * @param videoKey
     * @param videoFile
     * @param httpPostURL
     * @throws NullPointerException
     */

    public void submitVideoRequest(Map<String, String> params,
                                   String videoKey,
                                   File videoFile,
                                   String httpPostURL) throws NullPointerException, IOException {

        submitVideoRequest(_context,params,videoKey,videoFile,httpPostURL,volleyResponseListener,volleyErrorListener,-1,isRetryPolicyEnabled);

    }



    /**
     *
     * @param params
     * @param videoKey
     * @param videoFile
     * @param httpPostURL
     * @param retryTimeMilliSec
     * @throws NullPointerException
     */

    public void submitVideoRequest(Map<String, String> params,
                                   String videoKey,
                                   File videoFile,
                                   String httpPostURL,
                                   int retryTimeMilliSec ) throws NullPointerException, IOException {


        submitVideoRequest(_context,params,videoKey,videoFile,httpPostURL,volleyResponseListener,volleyErrorListener,retryTimeMilliSec,isRetryPolicyEnabled);

    }

    /**
     *
     * @param context
     * @param params
     * @param videoKey
     * @param videoFile
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @throws NullPointerException
     */

    public static void submitVideoRequest(Context context, Map<String, String> params,
                                   String videoKey,
                                   File videoFile,
                                   String httpPostURL,
                              Response.Listener<JSONObject> responseListener,
                              Response.ErrorListener errorListener) throws NullPointerException, IOException {


        submitVideoRequest(context,params,videoKey,videoFile,httpPostURL,responseListener,errorListener,-1,true);

    }

    /**
     *
     * @param context
     * @param params
     * @param videoKey
     * @param videoFile
     * @param httpPostURL
     * @param responseListener
     * @param errorListener
     * @param isRetryPolicyEnabled
     * @throws NullPointerException
     * @throws IOException
     */

    public static void submitVideoRequest(Context context, Map<String, String> params,
                                          String videoKey,
                                          File videoFile,
                                          String httpPostURL,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener,
                                          boolean isRetryPolicyEnabled) throws NullPointerException, IOException {


        submitVideoRequest(context,params,videoKey,videoFile,httpPostURL,responseListener,errorListener,-1,isRetryPolicyEnabled);

    }



    /**
     *
     * @param context
     * @param parameters
     * @param videoKey
     * @param videoFile
     * @param httpPostURL
     * @param retryTimeMilliSec
     */

    private static void submitVideoRequest(Context context,
                                    final Map<String, String> parameters,
                                    final String videoKey,
                                    final File videoFile,
                                    String httpPostURL,
                                    Response.Listener<JSONObject> responseListener,
                                    Response.ErrorListener errorListener,
                                    int retryTimeMilliSec,
                                           boolean isRetryPolicyEnabled) throws NullPointerException, IOException {

        VolleyJsonMultipartRequest volleyJSONMultipartRequest = new VolleyJsonMultipartRequest(Request.Method.POST,
                httpPostURL,
                responseListener,
                errorListener) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params = parameters;
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                if(videoFile!=null && videoFile.exists()) {
                    //params.put(videoKey, new DataPart(videoFile.getName(), FileUtils.readFileToByteArray(videoFile), "video/*"));
                    //multipart/form-data
                    params.put(videoKey, new DataPart(videoFile.getName(), FileUtils.readFileToByteArray(videoFile), "multipart/form-data"));
                }
                return params;
            }
        };

/*        if(retryTimeMilliSec != -1 ) {

            // NOTE: Set minimum retry time to 5 seconds
            if(retryTimeMilliSec <= 10000){
                Log.w(TAG,"Input retry time "+ retryTimeMilliSec +" is invalid. Setting retry time to 5 seconds");
                retryTimeMilliSec = 10000;
            }

            volleyJSONMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    retryTimeMilliSec,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }*/

        if(isRetryPolicyEnabled) {

            if (retryTimeMilliSec <= 15000) {
                Log.w(TAG, "Input retry time " + retryTimeMilliSec + " is invalid. Setting retry time to 5 seconds");
                retryTimeMilliSec = 15000;
            }


            volleyJSONMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    retryTimeMilliSec,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            volleyJSONMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(volleyJSONMultipartRequest);

    }


    public abstract void onVolleyResponse(JSONObject response);
    public abstract void onVolleyErrorResponse(VolleyError volleyError, VolleyDecodedError volleyDecodedError);
}
