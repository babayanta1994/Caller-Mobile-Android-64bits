package ru.true_ip.trueip.app.main_screen.settings_fragments.settings_calls_fragment;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.widget.CompoundButton;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.db.entity.SettingsDb;


/**
 * Created by user on 19-Oct-17.
 */

public class SettingsCallPresenter extends BasePresenter<BaseContract> {

    private static final String TAG = SettingsCallPresenter.class.getSimpleName();

    public SettingsDb settings;
    public ObservableBoolean isVideoEnabled = new ObservableBoolean();

    public SettingsCallPresenter() {
        repositoryController.getFirstSettings().subscribe(settingsDb -> {
            settings = settingsDb;
            isVideoEnabled.set(settings.getCall_type() == 1);
            }, throwable -> {
            isVideoEnabled.set(true);
        });
    }

    @Override
    public void attachToView(BaseContract contract) {
        super.attachToView(contract);
        isVideoEnabled.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

            }
        });
    }

    public void onCheckedChanged(CompoundButton v, boolean check) {
        if (settings == null) {
            settings = new SettingsDb();
        }
        settings.setCall_type(check ? 1 : 0);
        repositoryController.addSettings(settings).subscribe(() -> {}, throwable -> { });
    }
}
