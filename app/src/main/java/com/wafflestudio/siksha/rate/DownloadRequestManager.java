package com.wafflestudio.siksha.rate;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DownloadRequestManager {
    private static DownloadRequestManager instance;
    private RequestQueue requestQueue;
    private Context context;

    private DownloadRequestManager(Context context) {
        this.context = context;
    }

    public static synchronized DownloadRequestManager getInstance(Context context) {
        if (instance == null)
            instance = new DownloadRequestManager(context);

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }
}
