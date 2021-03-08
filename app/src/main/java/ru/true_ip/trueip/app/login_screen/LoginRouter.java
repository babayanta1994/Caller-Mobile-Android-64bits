package ru.true_ip.trueip.app.login_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by user on 07-Sep-17.
 */

public class LoginRouter extends BaseRouter {

    private Activity activity;

    public LoginRouter(Activity activity) {
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
                MainActivity.start(activity, bundle);
                activity.finish();
                break;
        }
    }
}
