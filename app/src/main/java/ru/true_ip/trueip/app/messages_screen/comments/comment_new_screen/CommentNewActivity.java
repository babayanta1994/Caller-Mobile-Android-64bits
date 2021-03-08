package ru.true_ip.trueip.app.messages_screen.comments.comment_new_screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityCommentNewBinding;

/**
 * Created by ektitarev on 12.01.2018.
 */

public class CommentNewActivity extends BaseActivity<CommentNewContract, CommentNewPresenter, ActivityCommentNewBinding> implements CommentNewContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommentNewActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommentNewActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 1);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
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
    }

    @Override
    public ActivityCommentNewBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_comment_new, null, false);
    }

    @Override
    public CommentNewContract getContract() {
        return this;
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
    public CommentNewPresenter createPresenter() {
        return new CommentNewPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new CommentNewRouter(this);
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
