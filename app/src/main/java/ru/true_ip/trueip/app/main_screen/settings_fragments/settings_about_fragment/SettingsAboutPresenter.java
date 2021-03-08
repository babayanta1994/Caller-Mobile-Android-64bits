package ru.true_ip.trueip.app.main_screen.settings_fragments.settings_about_fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.di.modules.ApiModule;


/**
 * Created by user on 19-Oct-17.
 */

public class SettingsAboutPresenter extends BasePresenter<BaseContract> {

    private static final String TAG = SettingsAboutPresenter.class.getSimpleName();
    public ObservableField<String> version = new ObservableField<>("");

    void getAppVersion(Context context) {
        String versionName = ApiModule.BASE_URL;

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            if (pInfo.versionName != null && !pInfo.versionName.isEmpty()) {
                //versionName = pInfo.versionName + " " + ApiModule.BASE_URL;
                versionName = pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version.set(versionName);
    }
}
