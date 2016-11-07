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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jooh on 2016-11-02.
 */
public class RatingRequestManager {

    private String url;
    private Context context;

    public RatingRequestManager(Context context) {
        this.context = context;
    }

    public void postRating(final String restaurant, final String food, final double rating) {

        RequestQueue queue = RatingRequestQueueManager.getInstance(context.getApplicationContext()).getRequestQueue();
        url = "localhost:8230/rate/" + restaurant + "/" + food;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("rating", String.valueOf(rating));

                return params;
            }
        };
        queue.add(postRequest);
    }
}

