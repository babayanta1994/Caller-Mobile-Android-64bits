package ru.true_ip.trueip.app.messages_screen.adboard_screen;

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
import ru.true_ip.trueip.databinding.ActivityAdBoardBinding;

/**
 *
 * Created by Andrey Filimonov on 29.12.2017.
 */

public class AdBoardActivity extends BaseActivity<AdBoardContract, AdBoardPresenter, ActivityAdBoardBinding> implements AdBoardContract {

    private final static String TAG = AdBoardActivity.class.getSimpleName();

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AdBoardActivity.class);
        intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.text_adboard));
            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
            presenter.getAds();
            //Here is how to handle Home button click
            binding.newRequestHome.setOnClickListener(view -> {
                homeButtonClicked();
            });
            processHomeButtonClick = true;
        }
    }

    @Override
    public ActivityAdBoardBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_ad_board, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public AdBoardContract getContract() {
        return this;
    }
    @Override
    public AdBoardPresenter createPresenter() {
        return new AdBoardPresenter();
    }
    @Override
    public BaseRouter createRouter() { return new AdBoardRouter(this); }
    @Override
    public void showPreloader() { }
    @Override
    public void hidePreloader() { }
    @Override
    public BaseRouter getRouter() { return router; }

}
