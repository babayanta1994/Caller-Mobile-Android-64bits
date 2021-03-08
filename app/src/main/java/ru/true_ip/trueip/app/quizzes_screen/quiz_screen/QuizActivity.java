package ru.true_ip.trueip.app.quizzes_screen.quiz_screen;

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
import ru.true_ip.trueip.databinding.ActivityQuizBinding;

/**
 * Created by ektitarev on 10.01.2018.
 */

public class QuizActivity extends BaseActivity<QuizContract, QuizPresenter, ActivityQuizBinding> implements QuizContract{

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QuizActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QuizActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity)context).startActivityForResult(intent, 1);
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
            presenter.setLayoutManager();
            presenter.getQuiz();
        }
        //Here is how to handle Home button click
        binding.homeButton.setOnClickListener(view -> {
            homeButtonClicked();
        });
        processHomeButtonClick = true;
    }

    @Override
    public ActivityQuizBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_quiz, null, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                setResult(1);
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                ((QuizRouter)getRouter()).moveBackward(1);
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
    public QuizContract getContract() {
        return this;
    }

    @Override
    public QuizPresenter createPresenter() {
        return new QuizPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new QuizRouter(this);
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
