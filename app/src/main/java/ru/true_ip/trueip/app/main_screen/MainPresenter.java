package ru.true_ip.trueip.app.main_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.javaFCM.TokenHelper;
import ru.true_ip.trueip.service.service.Logger;

/**
 * Created by user on 10-Sep-17.
 */

public class MainPresenter extends BasePresenter<MainContract> {

    public static final String TAG = MainPresenter.class.getSimpleName();

    private Context context;
    private ObservableBoolean isCloud = new ObservableBoolean(false);


    @Override
    public void checkServerStatus(Context context) {
        Logger.error(TAG, "Check Server Status");
        if (App.shouldCheckServerStatus)
            super.checkServerStatus(context);
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachToView(MainContract contract) {
        super.attachToView(contract);
        repositoryController.getHMLObjectsCount().subscribe((count, throwable) -> {
            //Logger.error(TAG,"Counter = " + count);
            if (count == 0) {
                isCloud.set(false);
            } else {
                isCloud.set(true);
            }
        });
    }

    @Override
    protected void onObjectChanged(ObjectDb objectDb) {
        super.onObjectChanged(objectDb);

        MainContract contract = getContract();
        if (contract != null) {
            contract.notifyObjectChanged();
        }
    }

    @Override
    protected void onDevicesUpdated(ObjectDb objectDb) {
        super.onDevicesUpdated(objectDb);

        MainContract contract = getContract();
        if (contract != null) {
            contract.notifyDevicesUpdated();
        }
    }

    public void testApiCalls() {
        TokenHelper.sendPushToken(context, repositoryController, apiController);
    }


    public void savePushToken(String token){
        repositoryController.setToken(token);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void onMenuItemClick(View v) {
        switch (v.getId()) {
            case R.id.menu_objects:
                App.shouldCheckServerStatus = true;
                getContract().getRouter().moveTo(BaseRouter.Destination.OBJECTS_SCREEN);
                break;
            case R.id.menu_favorites:
                App.shouldCheckServerStatus = false;
                getContract().getRouter().moveTo(BaseRouter.Destination.FAVORITES_SCREEN);
                break;
            case R.id.menu_picture:
                App.shouldCheckServerStatus = true;
                getContract().getRouter().moveTo(BaseRouter.Destination.PHOTO_SCREEN);
                break;
            case R.id.menu_settings:
                App.shouldCheckServerStatus = true;
                getContract().getRouter().moveTo(BaseRouter.Destination.SETTINGS_SCREEN);
                break;
            case R.id.menu_hlm:
                App.shouldCheckServerStatus = true;
                getContract().getRouter().moveTo(BaseRouter.Destination.HLM_SCREEN);
                break;
        }
        getContract().closeDrawer();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
