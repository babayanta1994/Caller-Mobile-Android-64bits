package ru.true_ip.trueip.app.messages_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class MessagesRouter extends BaseRouter {

    private Activity activity;

    public MessagesRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case MESSAGES_SCREEN:
                MessagesActivity.start(activity, bundle);
                break;
        }
    }
}
