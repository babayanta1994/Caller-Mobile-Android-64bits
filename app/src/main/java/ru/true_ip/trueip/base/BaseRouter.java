package ru.true_ip.trueip.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.true_ip.trueip.R;


/**
 * Created by user on 07-Sep-17.
 */

public abstract class BaseRouter {

    public enum Destination {
        MAIN_SCREEN,
        LOGIN_SCREEN,
        FAVORITES_SCREEN,
        OBJECTS_SCREEN,
        PICTURES_SCREEN,
        SETTINGS_SCREEN,
        SETTINGS_CALLS_SCREEN,
        SETTINGS_ABOUT_SCREEN,
        ADD_NEW_OBJECT_SCREEN,
        ADD_HLM_OBJECT_SCREEN,
        OBJECT_TYPE_PICKER_SCREEN,
        OBJECT_SCREEN,
        PHOTO_SCREEN,
        PHOTO_DETAILS_SCREEN,
        DEVICE_SCREEN,
        ADD_NEW_DEVICE_SCREEN,
        HLM_SCREEN,
        PROFILE_SCREEN,
        MESSAGES_SCREEN,
        ADBOARD_SCREEN
    }

    public abstract void moveTo(Destination dest, Bundle bundle);

    public void moveTo(Destination dest) {
        moveTo(dest, null);
    }

    public void moveBackward() {

    }

    protected void navigateToFragment(BaseActivity activity, BaseFragment fragment, Boolean addToBackStack, Boolean isReplace, Boolean clearBackStack) {
        FragmentManager manager = activity.getSupportFragmentManager();

        if (clearBackStack) {
            while (manager.popBackStackImmediate()) {
            }
        }

        FragmentTransaction transaction = manager.beginTransaction();

        if (isReplace) transaction.replace(R.id.container, fragment);
        else transaction.add(R.id.container, fragment);

        if (addToBackStack)
            transaction.addToBackStack(fragment.getTag());

        transaction.commit();

//        activity.setTitle(fragment.getTitle(activity));
    }
}
