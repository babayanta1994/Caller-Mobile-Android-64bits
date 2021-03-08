package ru.true_ip.trueip.app.main_screen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import ru.true_ip.trueip.app.add_new_object_screen.AddNewObjectActivity;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.app.main_screen.favorites_fragment.FavoritesFragment;
import ru.true_ip.trueip.app.main_screen.hlm_fragment.HLMFragment;
import ru.true_ip.trueip.app.main_screen.objects_fragment.ObjectsFragment;
import ru.true_ip.trueip.app.main_screen.photo_fragment.PhotoFragment;
import ru.true_ip.trueip.app.main_screen.settings_fragments.SettingsFragment;
import ru.true_ip.trueip.app.main_screen.settings_fragments.settings_about_fragment.SettingsAboutFragment;
import ru.true_ip.trueip.app.main_screen.settings_fragments.settings_calls_fragment.SettingsCallFragment;
import ru.true_ip.trueip.app.object_screen.ObjectActivity;
import ru.true_ip.trueip.app.object_type_picker_screen.ObjectTypePickerActivity;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by user on 10-Sep-17.
 */

public class MainRouter extends BaseRouter {

    BaseActivity activity;

    public MainRouter(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            activity.finish();
        }
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        BaseFragment fragment;
        Bundle extras = bundle != null ? bundle : new Bundle();
        switch (dest) {
            case OBJECTS_SCREEN:
                fragment = new ObjectsFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, true);
                break;
            case FAVORITES_SCREEN:
                fragment = new FavoritesFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, true);
                break;
            case SETTINGS_SCREEN:
                fragment = new SettingsFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, true);
                break;
            case SETTINGS_CALLS_SCREEN:
                fragment = new SettingsCallFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, false);
                break;
            case SETTINGS_ABOUT_SCREEN:
                fragment = new SettingsAboutFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, false);
                break;
            case OBJECT_TYPE_PICKER_SCREEN:
                ObjectTypePickerActivity.start(activity);
                break;
            case OBJECT_SCREEN:
                ObjectActivity.start(activity, bundle);
                break;
            case ADD_NEW_OBJECT_SCREEN:
                AddNewObjectActivity.start(activity, bundle);
                break;
            case PHOTO_SCREEN:
                fragment = new PhotoFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, true);
                break;
            case DEVICE_SCREEN:
                DeviceActivity.start(activity, bundle);
                break;
            case HLM_SCREEN:
                fragment = new HLMFragment();
                fragment.setArguments(extras);
                navigateToFragment(activity, fragment, true, true, true);
                break;

        }
    }
}
