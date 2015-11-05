package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JSONDownloadReceiver extends BroadcastReceiver {
    public static final String ACTION_MENU_BACKGROUND_DOWNLOAD = "com.wafflestudio.siksha.JSONDownloader.ACTION_MENU_BACKGROUND_DOWNLOAD";
    public static final String ACTION_MENU_FOREGROUND_DOWNLOAD = "com.wafflestudio.siksha.JSONDownloader.ACTION_MENU_FOREGROUND_DOWNLOAD";
    public static final String ACTION_INFORMATION_DOWNLOAD = "com.wafflestudio.siksha.JSONDownloader.INFORMATION_DOWNLOAD";
    public static final String ACTION_LATEST_APP_VERSION_CHECK = "com.wafflestudio.siksha.JSONDownloader.LATEST_APP_VERSION_CHECK";

    public static final int TYPE_ON_SUCCESS = 0;
    public static final int TYPE_ON_FAILURE = 1;

    private OnDownloadListener onDownloadListener;

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int callbackType = intent.getIntExtra("callback_type", TYPE_ON_SUCCESS);

        switch (callbackType) {
            case TYPE_ON_SUCCESS:
                onDownloadListener.onSuccess(action);
                break;
            case TYPE_ON_FAILURE:
                onDownloadListener.onFailure(action);
                break;
        }
    }

    public interface OnDownloadListener {
        void onSuccess(String action);

        void onFailure(String action);
    }
}