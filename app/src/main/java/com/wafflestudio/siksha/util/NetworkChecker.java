package com.wafflestudio.siksha.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {
    private static NetworkChecker instance;

    private NetworkChecker() {
    }

    public static synchronized NetworkChecker getInstance() {
        if (instance == null)
            instance = new NetworkChecker();

        return instance;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // getNetworkInfo(int networkType) is deprecated. But getNetworkInfo(Network network) as replacement method requires API 21.
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mobileNetwork.isConnected() || wifi.isConnected();
    }
}
