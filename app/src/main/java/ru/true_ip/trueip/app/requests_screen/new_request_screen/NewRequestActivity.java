package ru.true_ip.trueip.app.requests_screen.new_request_screen;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import java.security.Permissions;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityNewRequestBinding;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class NewRequestActivity extends BaseActivity<NewRequestContract, NewRequestPresenter, ActivityNewRequestBinding> implements NewRequestContract {
    private final static String TAG = NewRequestActivity.class.getSimpleName();

    private ProgressDialog dialog;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NewRequestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NewRequestActivity.class);
        intent.setExtrasClassLoader(context.getApplicationContext().getClass().getClassLoader());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (context instanceof Activity) {
            ((Activity)context).startActivityForResult(intent, 1);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent, bundle);
        }
    }

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            presenter.setContext(this);
            presenter.setSpinner(findViewById(R.id.new_request_categories));
            presenter.setExtras(getIntent().getExtras());
            presenter.setFragmentManager(getFragmentManager());
            //Here is how to handle Home button click
            binding.newRequestHome.setOnClickListener(view -> {
                homeButtonClicked();
            });
            processHomeButtonClick = true;
            presenter.loadClaimTypes();

            handler = new Handler();
            binding.mainContent.addOnLayoutChangeListener((View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) ->
                    handler.post(this::scrollDown)
            );
        }
    }

    @Override
    public ActivityNewRequestBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_new_request, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleImagePickerResult(this, requestCode, resultCode, data);
    }

    @Override
    public NewRequestContract getContract() {
        return this;
    }
    @Override
    public NewRequestPresenter createPresenter() {
        return new NewRequestPresenter();
    }
    @Override
    public BaseRouter createRouter() { return new NewRequestRouter(this); }

    @Override
    public void showPreloader() {
        if (!isDestroyed()) {
            dialog = DialogHelper.createProgressDialog(this, getString(R.string.text_send_progress_dialog));
            dialog.show();
        }
    }

    @Override
    public void hidePreloader() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    @Override
    public BaseRouter getRouter() { return router; }

    public void scrollDown() {
        binding.scrollView.smoothScrollTo(0, binding.scrollView.getBottom());
    }

    @Override
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                presenter.openChooserWithGallery(this);
            } else {
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, NewRequestPresenter.REQUEST_CODE_TAKE_PHOTO);
            }
        } else {
            presenter.openChooserWithGallery(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NewRequestPresenter.REQUEST_CODE_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                presenter.openChooserWithGallery(this);
            }
        }
    }
}
