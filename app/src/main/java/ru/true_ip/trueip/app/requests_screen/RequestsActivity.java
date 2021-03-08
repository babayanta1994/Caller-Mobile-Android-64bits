package ru.true_ip.trueip.app.requests_screen;

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
import ru.true_ip.trueip.databinding.ActivityRequestsBinding;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsActivity extends BaseActivity<RequestsContract, RequestsPresenter, ActivityRequestsBinding> implements RequestsContract{

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, RequestsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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
    public ActivityRequestsBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_requests, null, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            presenter.update();
            if (resultCode == 2) {
                presenter.showInfoMessage();
            }
        }
    }

    @Override
    public RequestsContract getContract() {
        return this;
    }

    @Override
    public RequestsPresenter createPresenter() {
        return new RequestsPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new RequestsRouter(this);
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
