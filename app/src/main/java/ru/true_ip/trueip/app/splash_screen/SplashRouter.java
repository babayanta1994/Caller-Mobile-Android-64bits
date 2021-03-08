package ru.true_ip.trueip.app.splash_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.app.login_screen.LoginActivity;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by user on 07-Sep-17.
 */

public class SplashRouter extends BaseRouter {

    private Activity activity;

    public SplashRouter(Activity activity) {
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
            case LOGIN_SCREEN:
                LoginActivity.start(activity);
                activity.finish();
                break;
        }
    }
}
