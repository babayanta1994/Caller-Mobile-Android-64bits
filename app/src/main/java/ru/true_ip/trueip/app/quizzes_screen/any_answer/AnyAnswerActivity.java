package ru.true_ip.trueip.app.quizzes_screen.any_answer;

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
import ru.true_ip.trueip.databinding.ActivityAnyAnswerBinding;

/**
 *
 * Created by Andrey Filimonov on 10.01.2018.
 */

public class AnyAnswerActivity extends BaseActivity<AnyAnswerContract, AnyAnswerPresenter, ActivityAnyAnswerBinding> implements AnyAnswerContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AnyAnswerActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Context context, Bundle extras) {
        Intent intent = new Intent(context, AnyAnswerActivity.class);

        if (extras != null) {
            intent.putExtras(extras);
        }

        if (context instanceof Activity) {
            ((Activity)context).startActivityForResult(intent, 1);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public ActivityAnyAnswerBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_any_answer, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((AnyAnswerRouter)getRouter()).moveBackward(1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
        }
        //Here is how to handle Home button click
        binding.homeButton.setOnClickListener(view -> {
            homeButtonClicked();
        });
        processHomeButtonClick = true;
    }

    @Override
    public AnyAnswerContract getContract() {
        return this;
    }

    @Override
    public AnyAnswerPresenter createPresenter() {
        return new AnyAnswerPresenter();
    }

    @Override
    public BaseRouter createRouter() { return new AnyAnswerRouter(this); }
    @Override
    public void showPreloader() { }
    @Override
    public void hidePreloader() { }
    @Override
    public BaseRouter getRouter() { return router; }
}

