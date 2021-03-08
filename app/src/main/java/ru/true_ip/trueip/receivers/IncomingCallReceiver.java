package ru.true_ip.trueip.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.true_ip.trueip.app.device_screen.DeviceActivity;


/**
 *
 * Created by Andrey Filimonov on 05.12.2017.
 */

public class IncomingCallReceiver extends BroadcastReceiver {
    //private final static String TAG ="IncomingCallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if ( extras!=null) {
            DeviceActivity.start(context, extras);
        }
    }
}
