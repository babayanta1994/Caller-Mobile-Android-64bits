package ru.true_ip.trueip.app.messages_screen.notifications_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityNotificationsBinding;

/**
 * Created by ektitarev on 29.12.2017.
 */

public class NotificationsActivity extends BaseActivity<NotificationsContract, NotificationsPresenter, ActivityNotificationsBinding> implements NotificationsContract {

    public static void start (Context context, Bundle bundle) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
            presenter.getNotifications();

            //Here is how to handle Home button click
            binding.homeButton.setOnClickListener(view -> homeButtonClicked());
        }
        processHomeButtonClick = true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public ActivityNotificationsBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_notifications, null, false);
    }

    @Override
    public NotificationsContract getContract() {
        return this;
    }

    @Override
    public NotificationsPresenter createPresenter() {
        return new NotificationsPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new NotificationsRouter(this);
    }
}
