package com.blackphoenix.phoenixvolley.v1;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.blackphoenix.phoenixvolley.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praba on 7/28/2017.
 */
public class VolleyErrorHelper {
    private VolleyErrorHelper() {}

    /**
     *
     * @param error
     * @param context
     * @return Returns appropriate message which is to be displayed to the user against
     * the specified error object.
     */
    public static String getMessage(Object error, Context context) {
        if(context == null){
            return "Error Null Context";
        }else if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet);
        }
        //return context.getResources().getString(R.string.no_internet);
        return context.getResources().getString(R.string.generic_error);
    }

    /**
     *
     * @param error
     * @param context
     * @return Return generic message for errors
     */
    public static String getErrorType(Object error, Context context) {
        if(context == null){
            return "Error Null Context";
        } else if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_timeout);
        } else if (error instanceof ServerError) {
            return context.getResources().getString(R.string.generic_server_down);
        } else if (error instanceof AuthFailureError) {
            return context.getResources().getString(R.string.auth_failed);
        } else if (error instanceof NoConnectionError) {
            return context.getResources().getString(R.string.no_network_connection);
        } else if (error instanceof NetworkError) {
            return context.getResources().getString(R.string.no_internet);
        } else if (error instanceof ParseError) {
            return context.getResources().getString(R.string.parsing_failed);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    public static String getErrorCode(Object error, Context context) {
        if(context == null){
            return "Error Null Context";
        } else if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_timeout_code);
        } else if (error instanceof ServerError) {
            return context.getResources().getString(R.string.generic_server_down_code);
        } else if (error instanceof AuthFailureError) {
            return context.getResources().getString(R.string.auth_failed_code);
        } else if (error instanceof NoConnectionError) {
            return context.getResources().getString(R.string.no_network_connection_code);
        } else if (error instanceof NetworkError) {
            return context.getResources().getString(R.string.no_internet_code);
        } else if (error instanceof ParseError) {
            return context.getResources().getString(R.string.parsing_failed_code);
        }
        return context.getResources().getString(R.string.generic_error_code);
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock
     * message or to show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error":
                        // "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() { }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();

                // PRABA Added
                case 500:
                    String json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) {
                        return json;
                    } else {
                        return "500 Error";
                    }

                default:
                 //   return context.getResources().getString(R.string.generic_server_down);
                    // PRABA UPDATED
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) {
                       return json;
                    } else {
                        return context.getResources().getString(R.string.generic_server_down);
                    }
            }
        }
        return context.getResources().getString(R.string.generic_error);
    }


    public static String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
}
