package com.blackphoenix.phoenixvolley.v1;

/**
 * Created by Praba on 10-02-2017.
 */

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class VolleyJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;
    public static final String LOG_TITLE = VolleyJsonRequest.class.getSimpleName();

    public VolleyJsonRequest(String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public VolleyJsonRequest(int method, String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e(LOG_TITLE,"Success Response 1 "+jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TITLE,"Error Response 1 " +e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            Log.e(LOG_TITLE,"Error Response 2 "+je.getMessage());
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        Log.e(LOG_TITLE,"Deliver Response");
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}