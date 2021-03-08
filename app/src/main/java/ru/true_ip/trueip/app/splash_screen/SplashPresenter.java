package ru.true_ip.trueip.app.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.utils.Constants;

import static ru.true_ip.trueip.base.BaseRouter.Destination.FAVORITES_SCREEN;
import static ru.true_ip.trueip.base.BaseRouter.Destination.HLM_SCREEN;
import static ru.true_ip.trueip.base.BaseRouter.Destination.LOGIN_SCREEN;


/**
 * Created by user on 07-Sep-17.
 */

public class SplashPresenter extends BasePresenter<SplashContract> {

    private static final String TAG = SplashPresenter.class.getSimpleName();
    private static final long SPLASH_SCREEN_DURATION = 1000;
    private Handler handler = new Handler();

    @Override
    public void attachToView(SplashContract contract) {
        super.attachToView(contract);
        handler.postDelayed(this::moveForward, SPLASH_SCREEN_DURATION);
    }

    private void moveForward() {
        repositoryController.getOjbectsCount().subscribe((count, throwable) -> {
            if (count == 0) {
                getContract().getRouter().moveTo(LOGIN_SCREEN);
            } else {
                Bundle bundle = new Bundle();
                if (isFromNotification()) {
                    bundle.putSerializable(Constants.BUNDLE_DESTINATION, HLM_SCREEN);
                } else {
                    bundle.putSerializable(Constants.BUNDLE_DESTINATION, FAVORITES_SCREEN);
                }
                getContract().getRouter().moveTo(BaseRouter.Destination.MAIN_SCREEN, bundle);
            }
        });
    }

    private boolean isFromNotification() {
        Intent intent = getContract().getIntent();
        return intent != null && intent.getBooleanExtra(Constants.BUNDLE_FROM_NOTIFICATION, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
