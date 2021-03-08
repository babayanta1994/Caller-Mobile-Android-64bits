package ru.true_ip.trueip.app.messages_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityMessagesBinding;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class MessagesActivity extends BaseActivity<MessagesContract, MessagesPresenter, ActivityMessagesBinding> implements MessagesContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MessagesActivity.class);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.text_messages));
        }
        presenter.setContext(this);
        presenter.setExtras(getIntent().getExtras());
        //Here is how to handle Home button click
        binding.messagesHome.setOnClickListener(view -> {
            homeButtonClicked();
        });
        processHomeButtonClick = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.displayData();
    }

    @Override
    public ActivityMessagesBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_messages, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
    public MessagesContract getContract() {
        return this;
    }

    @Override
    public MessagesPresenter createPresenter() {
        return new MessagesPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new MessagesRouter(this);
    }
}