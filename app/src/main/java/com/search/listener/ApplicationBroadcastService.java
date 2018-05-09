package com.search.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ApplicationBroadcastService extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "app install", Toast.LENGTH_SHORT).show();
        Log.e("change","app install");
    }
}