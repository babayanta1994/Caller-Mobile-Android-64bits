package ru.true_ip.trueip.app.splash_screen;

import android.databinding.DataBindingUtil;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivitySplashBinding;


/**
 * Created by user on 07-Sep-17.
 */

public class SplashActivity extends BaseActivity<SplashContract, SplashPresenter, ActivitySplashBinding> implements SplashContract {

    @Override
    public ActivitySplashBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_splash, null, false);
    }

    @Override
    public SplashContract getContract() {
        return this;
    }

    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new SplashRouter(this);
    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }
}
