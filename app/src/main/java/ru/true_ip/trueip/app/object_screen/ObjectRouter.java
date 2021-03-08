package ru.true_ip.trueip.app.object_screen;

import android.os.Bundle;

import ru.true_ip.trueip.app.add_new_device_screen.AddNewDeviceActivity;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by user on 19-Sep-17.
 */

public class ObjectRouter extends BaseRouter {

    BaseActivity activity;

    public ObjectRouter(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case ADD_NEW_DEVICE_SCREEN:
                AddNewDeviceActivity.start(activity, bundle);
                break;
            case DEVICE_SCREEN:
                DeviceActivity.start(activity, bundle);
                break;
        }
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }
}
