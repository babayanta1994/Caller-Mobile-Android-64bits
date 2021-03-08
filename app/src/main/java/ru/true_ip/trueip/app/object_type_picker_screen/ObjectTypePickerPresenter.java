package ru.true_ip.trueip.app.object_type_picker_screen;

import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;


/**
 * Created by Eugen on 16.10.2017.
 */

public class ObjectTypePickerPresenter extends BasePresenter<BaseContract> {

    public void onClick(View view) {
        BaseContract contract = getContract();
        switch (view.getId()) {
            case R.id.btn_close:
                if (contract != null) {
                    contract.getRouter().moveBackward();
                }
                break;
            case R.id.btn_add_hlm_object:
                if (contract != null) {
                    contract.getRouter().moveTo(BaseRouter.Destination.ADD_HLM_OBJECT_SCREEN);
                }
                break;
            case R.id.btn_add_local_object:
                if (contract != null) {
                    contract.getRouter().moveTo(BaseRouter.Destination.ADD_NEW_OBJECT_SCREEN);
                }
                break;
        }
    }

}
