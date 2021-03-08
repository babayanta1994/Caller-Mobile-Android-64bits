package ru.true_ip.trueip.app.object_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityObjectBinding;
import ru.true_ip.trueip.utils.Constants;


/**
 * Created by user on 19-Sep-17.
 */

public class ObjectActivity extends BaseActivity<ObjectContract, ObjectPresenter, ActivityObjectBinding> implements ObjectContract {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ObjectActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.tabLayout.setupWithViewPager(binding.viewPager);

            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
            presenter.createViewPager(getIntent().getExtras());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setTitle(getIntent().getExtras().getInt(Constants.BUNDLE_INT_KEY));
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
    public ActivityObjectBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_object, null, false);
    }

    @Override
    public ObjectContract getContract() {
        return this;
    }

    @Override
    public ObjectPresenter createPresenter() {
        return new ObjectPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new ObjectRouter(this);
    }

    @Override
    public void setTitle(String title) {
        if (binding != null)
            binding.title.setText(title);
    }
}
