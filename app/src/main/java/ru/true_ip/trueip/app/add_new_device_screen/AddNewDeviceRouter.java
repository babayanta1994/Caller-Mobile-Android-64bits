package ru.true_ip.trueip.app.add_new_device_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by user on 07-Sep-17.
 */

public class AddNewDeviceRouter extends BaseRouter {

    private Activity activity;

    public AddNewDeviceRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case MAIN_SCREEN:
                MainActivity.start(activity);
                activity.finish();
                break;
        }
    }
}
