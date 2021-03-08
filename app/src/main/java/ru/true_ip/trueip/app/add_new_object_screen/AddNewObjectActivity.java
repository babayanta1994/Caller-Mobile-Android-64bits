package ru.true_ip.trueip.app.add_new_object_screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityAddNewObjectBinding;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.utils.DialogHelper;


public class AddNewObjectActivity extends BaseActivity<BaseContract, AddNewObjectPresenter, ActivityAddNewObjectBinding> implements AddNewObjectContract {

    public static final String TAG = AddNewObjectPresenter.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setContext(this);
        presenter.handleBundle(getIntent().getExtras());
    }

    @Override
    public ActivityAddNewObjectBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_add_new_object, null, false);
    }

    @Override
    public BaseContract getContract() {
        return this;
    }

    @Override
    public AddNewObjectPresenter createPresenter() {
        return new AddNewObjectPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new AddNewObjectRouter(this);
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

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddNewObjectActivity.class);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void showObjectData(ObjectDb objectDb) { }

    @Override
    public void displayDialog(@StringRes int stringId) {
        DialogHelper.createExplanationDialog(this, stringId);
    }

    @Override
    public void resetImeOption(boolean isConcierge) {
        View v = getCurrentFocus();
        if (v != null && v.getId() == R.id.et_password) {
            if (v instanceof EditText) {
                EditText editText = (EditText) v;

                if (isConcierge) {
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                } else {
                    editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                }

                editText.setText(editText.getText());
            }
        }
    }
}
