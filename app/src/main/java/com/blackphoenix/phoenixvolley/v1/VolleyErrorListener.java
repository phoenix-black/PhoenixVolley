package com.blackphoenix.phoenixvolley.v1;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Praba on 7/27/2017.
 */
public abstract class VolleyErrorListener implements Response.ErrorListener {

    private Context _context;

    public VolleyErrorListener(Context context){
        this._context = context;
    }

    public VolleyErrorListener(Context context, boolean errorDialog){
        this._context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        VolleyDecodedError volleyDecodedError = new VolleyDecodedError();

        volleyDecodedError.setErrorMessage(VolleyErrorHelper.getMessage(error, _context));
        volleyDecodedError.setErrorType(VolleyErrorHelper.getErrorType(error, _context));
        volleyDecodedError.setErrorCode(VolleyErrorHelper.getErrorCode(error, _context));

        onError(error,volleyDecodedError);
    }

    public abstract void onError(VolleyError volleyError, VolleyDecodedError volleyDecodedError);

}
