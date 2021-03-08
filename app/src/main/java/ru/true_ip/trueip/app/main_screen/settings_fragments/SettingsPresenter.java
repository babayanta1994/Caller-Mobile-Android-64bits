package ru.true_ip.trueip.app.main_screen.settings_fragments;

import android.view.View;

import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by user on 11-Sep-17.
 */

public class SettingsPresenter extends BasePresenter<SettingsContract> {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    public void onClickCallSettings(View view) {
        getContract().getRouter().moveTo(BaseRouter.Destination.SETTINGS_CALLS_SCREEN);
    }

    public void onClickAboutSettings(View view) {
        getContract().getRouter().moveTo(BaseRouter.Destination.SETTINGS_ABOUT_SCREEN);
    }
}
