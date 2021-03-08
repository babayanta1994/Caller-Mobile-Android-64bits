package ru.true_ip.trueip.app.quizzes_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityQuizzesBinding;

/**
 * Created by ektitarev on 27.12.2017.
 */

public class QuizzesActivity extends BaseActivity<QuizzesContract, QuizzesPresenter, ActivityQuizzesBinding> implements QuizzesContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QuizzesActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    public ActivityQuizzesBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_quizzes, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getRouter().moveBackward();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            binding.tabLayout.setupWithViewPager(binding.viewPager);

            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
            presenter.createViewPager();
        }
        //Here is how to handle Home button click
        binding.homeButton.setOnClickListener(view -> {
            homeButtonClicked();
        });
        processHomeButtonClick = true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode != 1) {
            presenter.updateQuizzesList();
        }
    }

    @Override
    public QuizzesContract getContract() {
        return this;
    }

    @Override
    public QuizzesPresenter createPresenter() {
        return new QuizzesPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new QuizzesRouter(this);
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
