package ru.true_ip.trueip.app.main_screen.photo_details_activity;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by ektitarev on 18.01.2018.
 */

public class PhotoDetailsRouter extends BaseRouter {

    private Activity activity;

    public PhotoDetailsRouter(Activity activity) {
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
