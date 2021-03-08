package ru.true_ip.trueip.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.true_ip.trueip.BR;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.app.splash_screen.SplashActivity;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by user on 07-Sep-17.
 *
 */

public abstract class BaseActivity<C extends BaseContract, P extends BasePresenter, B extends ViewDataBinding> extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    protected P presenter = null;
    protected B binding = null;
    protected BaseRouter router = null;
    private BroadcastReceiver broadcastReceiver = null;
    private BroadcastReceiver localeChangedReceiver = null;
    protected boolean processHomeButtonClick = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.router = createRouter();
        this.binding = initBinding();
        this.presenter = createPresenter();
        this.binding.setVariable(BR.presenter, this.presenter);
        setContentView(binding.getRoot());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processBroadCast();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.FINISH_ACTIVITY);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public abstract B initBinding();

    public abstract C getContract();

    public abstract P createPresenter();

    public abstract BaseRouter createRouter();

    public void setTitle(String title) {     }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachToView(getContract());
        if (!(this instanceof SplashActivity)) {
            if (App.getStartedActivityNumber() == 0) {
                presenter.checkServerStatus(this);
            }
            App.incrementStartedActivityNumber();

            if (App.isLocaleChanged()) {
                presenter.setLocale(BaseActivity.this);
                App.setLocaleChanged(false);
            }
            registerLocaleChangedReceiver();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        if (!(this instanceof SplashActivity)) {
            unregisterLocaleChangedReceiver();
            App.decrementStartedActivityNumber();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter = null;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public void onBackPressed() {
        router.moveBackward();
    }

    protected void processBroadCast() {
        if ( processHomeButtonClick )
            finish();
    }

    protected void homeButtonClicked() {
        Intent intent = new Intent();
        intent.setAction(Constants.FINISH_ACTIVITY);
        sendBroadcast(intent);
    }

    private void registerLocaleChangedReceiver() {
        localeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action != null && action.equals(Constants.ACTION_NOTIFY_LOCALE_CHANGED)) {
                    presenter.setLocale(BaseActivity.this);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_NOTIFY_LOCALE_CHANGED);
        registerReceiver(localeChangedReceiver, intentFilter);
    }

    private void unregisterLocaleChangedReceiver() {
        if (localeChangedReceiver != null) {
            unregisterReceiver(localeChangedReceiver);
        }
    }

}
