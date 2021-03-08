package ru.true_ip.trueip.app.login_screen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityLoginBinding;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.input_filters.ActivationCodeInputFilter;
import ru.true_ip.trueip.utils.input_filters.ApartmentNumberInputFilter;
import ru.true_ip.trueip.utils.text_watchers.ActivationCodeTextWatcher;


public class LoginActivity extends BaseActivity<LoginContract, LoginPresenter, ActivityLoginBinding> implements LoginContract {

    private final static int PERMISSIONS_REQUEST_RECORD_AUDIO = 1778;
    private final int SCROLL_MARGIN = 10;

    private Handler handler;

    @Override
    public ActivityLoginBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        if(binding != null) {
            binding.mainContent.addOnLayoutChangeListener((View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) ->
                handler.post(this::scrollDown)
            );
            binding.licenceAgreementText.setMovementMethod(LinkMovementMethod.getInstance());

            binding.edtFlatNum.setFilters(new InputFilter[] { new ApartmentNumberInputFilter() });

            binding.edtActivationCode.setFilters(new InputFilter[] { new ActivationCodeInputFilter(), new InputFilter.LengthFilter(Constants.ACTIVATION_CODE_LENGTH + 3) });
            binding.edtActivationCode.addTextChangedListener(new ActivationCodeTextWatcher(binding.edtActivationCode));

        }
        presenter.setContext(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission();
    }

    @Override
    public LoginContract getContract() {
        return this;
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new LoginRouter(this);
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

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
        SipServiceCommands.startService(context);
    }

    @Override
    public void showFlatNumberError(String message) {
        binding.loginNumberError.setVisibility(View.VISIBLE);
        binding.loginNumberError.setText(message);
    }

    @Override
    public void showCodeError(String message) {
        binding.loginCodeError.setVisibility(View.VISIBLE);
        binding.loginCodeError.setText(message);
    }

    @Override
    public void dismissFlatNumberError() {
        binding.loginNumberError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void dismissCodeError() {
        binding.loginCodeError.setVisibility(View.INVISIBLE);
    }

    private void checkPermission() {
        //Logger.error(TAG, "Check Permission called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.can_not_handle_calls))
                    .setCancelable(false)
                    .setPositiveButton(R.string.text_ok, (dialog, id) -> {
                        requestPermission();
                    });

            builder.create().show();
        } else {
            presenter.gotPermissions = true;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSIONS_REQUEST_RECORD_AUDIO);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.gotPermissions = true;
                }
            }
        }
    }

    private void scrollDown() {
        int contentHeight = binding.getRoot().getHeight();
        int buttonBottomEdge = binding.btnLogin.getBottom();

        if (contentHeight < buttonBottomEdge) {
            int dy = buttonBottomEdge - contentHeight;
            binding.scrollView.smoothScrollTo(0, dy + SCROLL_MARGIN);
        }
    }
}