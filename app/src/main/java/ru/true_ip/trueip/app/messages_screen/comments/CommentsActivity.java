package ru.true_ip.trueip.app.messages_screen.comments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityCommentsBinding;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentsActivity extends BaseActivity<CommentsContract, CommentsPresenter, ActivityCommentsBinding> implements CommentsContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommentsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        presenter.setContext(this);
        presenter.setExtras(getIntent().getExtras());
        presenter.setComments();
        binding.homeButton.setOnClickListener(view -> {
            homeButtonClicked();
        });
        processHomeButtonClick = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            presenter.onRefreshSwiped();
        }
    }

    @Override
    public ActivityCommentsBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_comments, null, false);
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
    public void showPreloader() { }
    @Override
    public void hidePreloader() { }
    @Override
    public BaseRouter getRouter() { return router; }
    @Override
    public CommentsContract getContract() { return this; }
    @Override
    public CommentsPresenter createPresenter() { return new CommentsPresenter(); }
    @Override
    public BaseRouter createRouter() { return new CommentsRouter(this); }
}