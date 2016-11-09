package com.wafflestudio.siksha.service;

import android.content.Context;
import android.media.Rating;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wafflestudio.siksha.dialog.RatingDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jooh on 2016-11-02.
 */
public class RatingRequestManager {

    private static final String SERVER_URL = "http://siksha.kr:8230";
    private static final String ROUTE_RATE = "/rate";

    private String url;
    private Context context;

    public RatingRequestManager(Context context) {
        this.context = context;
    }

    public void postRating(final String restaurant, final String food, final double rating, final RatingDialog.VolleyCallback volleyCallBack) {

        RequestQueue queue = RatingRequestQueueManager.getInstance(context.getApplicationContext()).getRequestQueue();
//        url = SERVER_URL+ROUTE_RATE+"/"+restaurant+"/"+food;
        url = "http://dev.wafflestudio.com:8230/rate";
        url = url.replaceAll(" ","%20");

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        volleyCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack.onFailure();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("restaurant",restaurant);
                params.put("meal",food);
                params.put("rating", String.valueOf(rating));

                return params;
            }
        };
        queue.add(postRequest);
    }
}

