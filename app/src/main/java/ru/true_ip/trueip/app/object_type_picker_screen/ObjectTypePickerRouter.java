package ru.true_ip.trueip.app.object_type_picker_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.app.add_hlm_object_screen.AddHlmObjectActivity;
import ru.true_ip.trueip.app.add_new_object_screen.AddNewObjectActivity;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by Eugen on 16.10.2017.
 */

public class ObjectTypePickerRouter extends BaseRouter {

    private Activity activity;

    public ObjectTypePickerRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case ADD_NEW_OBJECT_SCREEN:
                AddNewObjectActivity.start(activity, bundle);
                break;
            case ADD_HLM_OBJECT_SCREEN:
                AddHlmObjectActivity.start(activity);
                break;
        }
    }
}
