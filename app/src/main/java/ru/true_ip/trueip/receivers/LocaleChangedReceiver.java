package ru.true_ip.trueip.receivers;

import android.content.Context;
import android.content.Intent;

import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.receivers.BaseReceiver;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 12.09.2018.
 */

public class LocaleChangedReceiver extends BaseReceiver {

    //private static final String TAG = LocaleChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_LOCALE_CHANGED)) {
            if (App.getStartedActivityNumber() == 0) {
                App.setLocaleChanged(true);
            } else {
                Intent localeChangedIntent = new Intent(Constants.ACTION_NOTIFY_LOCALE_CHANGED);
                context.sendBroadcast(localeChangedIntent);
            }
        }
    }
}
