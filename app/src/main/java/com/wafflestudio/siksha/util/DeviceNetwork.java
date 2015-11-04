package com.wafflestudio.siksha.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DeviceNetwork {
    private static DeviceNetwork instance;
    private ConnectivityManager connectivityManager;

    private DeviceNetwork() { }

    public static synchronized DeviceNetwork getInstance() {
        if (instance == null)
            instance = new DeviceNetwork();

        return instance;
    }

    public void initialize(Context context) {
        if (connectivityManager == null)
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isOnline() {
        // getNetworkInfo(int networkType) is deprecated. But getNetworkInfo(Network network) as replacement method requires API 21.
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mobileNetwork.isConnected() || wifi.isConnected();
    }
}
