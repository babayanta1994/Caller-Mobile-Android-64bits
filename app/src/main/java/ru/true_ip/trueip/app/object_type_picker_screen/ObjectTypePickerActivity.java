package ru.true_ip.trueip.app.object_type_picker_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityObjectTypePickerBinding;


/**
 * Created by Eugen on 16.10.2017.
 */

public class ObjectTypePickerActivity extends BaseActivity<BaseContract, ObjectTypePickerPresenter, ActivityObjectTypePickerBinding> implements BaseContract {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getContract().getRouter().moveBackward();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        processHomeButtonClick = true;
    }

    @Override
    public ActivityObjectTypePickerBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_object_type_picker, null, false);
    }

    @Override
    public BaseContract getContract() {
        return this;
    }

    @Override
    public ObjectTypePickerPresenter createPresenter() {
        return new ObjectTypePickerPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new ObjectTypePickerRouter(this);
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
        Intent intent = new Intent(context, ObjectTypePickerActivity.class);
        context.startActivity(intent);
    }
}
