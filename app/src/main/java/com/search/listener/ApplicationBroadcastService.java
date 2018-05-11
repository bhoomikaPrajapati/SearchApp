package com.search.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class ApplicationBroadcastService extends BroadcastReceiver {
    public void onReceive(Context mContext, Intent intent) {
        /**p2p stanza*/
        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(mContext);
        Intent intent1 = new Intent("MESSAGE");
        broadcaster.sendBroadcast(intent1);

        /*  Intent intent = new Intent(Constant.XMPP_EVENT_MESSAGE);
        intent.putExtra(Constant.KEY_EXTRA_TYPE, Constant.XMPP_START_TYPING);
        intent.putExtra("_message", Constant.XMPP_START_TYPING);
        intent.putExtra("_messageFrom", getTypingStatusOfParticularID(mMessage.toString()));
        intent.putExtra("_messageBody", mMessage.getBody());*/


        Log.e("change", "app install");
    }
}