package com.wafflestudio.siksha.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016-11-02.
 */
public class RatingRequestQueueManager {

    private static RatingRequestQueueManager instance;
    private RequestQueue requestQueue;
    private Context context;

    private RatingRequestQueueManager (Context context) {
        this.context = context;
    }

    public static synchronized RatingRequestQueueManager getInstance(Context context) {
        if (instance == null)
            instance = new RatingRequestQueueManager (context);

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }
}
