package ru.true_ip.trueip.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipService;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * Created by Andrey Filimonov on 2019-07-30.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.error(TAG,"Alarm Receiver Starts Service");
        Intent i = new Intent(context, SipService.class);
        context.startService(i);
    }
}
