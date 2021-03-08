package ru.true_ip.trueip.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ru.true_ip.trueip.service.service.SipService;

/**
 *
 * Created by Andrey Filimonov on 24.11.2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    //private static final String TAG = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {

            Intent i = new Intent(context, SipService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
        }
    }
}
