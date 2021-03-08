package ru.true_ip.trueip.app.requests_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsRouter extends BaseRouter {

    private Activity activity;

    public RequestsRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        super.moveBackward();
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {

    }
}
