package com.wafflestudio.siksha.rate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Preference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jooh on 2016-11-02.
 */
public class RatingRequestManager {


    private static final String SERVER_URL = "http://siksha.kr:8230";
    private static final String REDIRECT_SERVER_URL = "http://dev.wafflestudio.com:8230";
    private static final String ROUTE_RATE = "/rate";

    private static final String PRIMARY_REQUEST = "PRIMARY_REQUEST";
    private static final String RETRY_REQUEST = "RETRY_REQUEST";

    private Context context;
    private RequestQueue queue;

    public RatingRequestManager(Context context) {
        this.context = context;

        queue = RatingRequestQueueManager.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void ratingPost(final String restaurant, final String food, final double rating, final RatingDialog ratingDialog) {

        ratingPostStart(getFullURL(true), restaurant, food, rating, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                ratingDialog.ratingSuccess();
            }

            @Override
            public void onFailure() {
                sendSignalToApp("POST_RATING", JSONDownloadReceiver.TYPE_ON_FAILURE);
            }

        });
    }

    private void ratingPostStart(String url, final String restaurant, final String food, final double rating, final VolleyCallback volleyCallBack) {

        StringRequest primaryRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        volleyCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestWithRedirectServer(restaurant, food, rating, volleyCallBack);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("key","siksha1996");
                params.put("restaurant",restaurant);
                params.put("meal",food);
                params.put("rating", String.valueOf(rating));

                return params;
            }
        };
        primaryRequest.setTag(PRIMARY_REQUEST);
        queue.add(primaryRequest);
    }

    private void requestWithRedirectServer(final String restaurant, final String food, final double rating, final VolleyCallback volleyCallBack) {

        queue.cancelAll(PRIMARY_REQUEST);

        StringRequest retryRequest = new StringRequest(Request.Method.POST, getFullURL(false),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleyCallBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                queue.cancelAll(RETRY_REQUEST);
                volleyCallBack.onFailure();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("key","siksha1996");
                params.put("restaurant",restaurant);
                params.put("meal",food);
                params.put("rating", String.valueOf(rating));

                return params;
            }
        };
        retryRequest.setTag(RETRY_REQUEST);
        queue.add(retryRequest);
    }

    private String getFullURL(boolean isAlive) {
        if (isAlive) {
            return SERVER_URL + ROUTE_RATE;
        }
        else {
            return REDIRECT_SERVER_URL + ROUTE_RATE;
        }
    }

    private void sendSignalToApp(String action, int callbackType) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("callback_type", callbackType);
        context.sendBroadcast(intent);
    }

    public interface VolleyCallback {
        void onSuccess(String response);
        void onFailure();
    }
}

