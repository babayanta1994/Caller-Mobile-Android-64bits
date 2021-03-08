package ru.true_ip.trueip.app.profile_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class ProfileRouter extends BaseRouter {

    private Activity activity;

    public ProfileRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case PROFILE_SCREEN:
                ProfileActivity.start(activity, bundle);
                break;
        }
    }
}
