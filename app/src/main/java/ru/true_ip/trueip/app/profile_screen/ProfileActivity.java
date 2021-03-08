package ru.true_ip.trueip.app.profile_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityProfileBinding;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class ProfileActivity extends BaseActivity<ProfileContract, ProfilePresenter, ActivityProfileBinding> implements ProfileContract{

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ProfileActivity.class);
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
            getSupportActionBar().setTitle(getString(R.string.text_profile));
        }
        presenter.setContext(this);
        presenter.setExtras(getIntent().getExtras());
        presenter.displayData();
        binding.profileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public ActivityProfileBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_profile, null, false);
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
    public void showPreloader() {}
    @Override
    public void hidePreloader() {}
    @Override
    public BaseRouter getRouter() {return router;}
    @Override
    public ProfileContract getContract() { return this; }
    @Override
    public ProfilePresenter createPresenter() { return new ProfilePresenter(); }
    @Override
    public BaseRouter createRouter() { return new ProfileRouter(this); }
}
