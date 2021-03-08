package ru.true_ip.trueip.app.add_hlm_object_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityAddHlmObjectBinding;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.input_filters.ActivationCodeInputFilter;
import ru.true_ip.trueip.utils.input_filters.ApartmentNumberInputFilter;
import ru.true_ip.trueip.utils.text_watchers.ActivationCodeTextWatcher;


public class AddHlmObjectActivity extends BaseActivity<AddHlmObjectContract, AddHlmObjectPresenter, ActivityAddHlmObjectBinding> implements AddHlmObjectContract {

    @Override
    public ActivityAddHlmObjectBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_add_hlm_object, null, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding != null) {
            binding.etIpAddress.setFilters(new InputFilter[] { new ApartmentNumberInputFilter() });

            binding.etPort.setFilters(new InputFilter[] { new ActivationCodeInputFilter(), new InputFilter.LengthFilter(Constants.ACTIVATION_CODE_LENGTH + 3) });
            binding.etPort.addTextChangedListener(new ActivationCodeTextWatcher(binding.etPort));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setContext(this);
    }

    @Override
    public AddHlmObjectContract getContract() {
        return this;
    }

    @Override
    public AddHlmObjectPresenter createPresenter() {
        return new AddHlmObjectPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new AddHlmObjectRouter(this);
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
        Intent intent = new Intent(context, AddHlmObjectActivity.class);
        context.startActivity(intent);
    }
}
