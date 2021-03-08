package ru.true_ip.trueip.app.messages_screen.adboard_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by Andrey Filimonov on 29.12.2017.
 */

public class AdBoardRouter extends BaseRouter {

    private Activity activity;

    public AdBoardRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case ADBOARD_SCREEN:
                AdBoardActivity.start(activity, bundle);
                break;
        }
    }
}