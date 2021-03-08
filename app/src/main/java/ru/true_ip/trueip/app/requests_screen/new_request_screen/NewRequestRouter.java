package ru.true_ip.trueip.app.requests_screen.new_request_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class NewRequestRouter extends BaseRouter {
    private Activity activity;

    public NewRequestRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        super.moveBackward();
        activity.finish();
    }

    public void moveBackward(int resultCode) {
        activity.setResult(resultCode);
        moveBackward();
    }

    @Override
    public void moveTo(BaseRouter.Destination dest, Bundle bundle) {

    }

}
