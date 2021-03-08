package ru.true_ip.trueip.app.object_screen.abstract_fragments;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BasePresenter;

/**
 * Created by ektitarev on 24.01.2018.
 */

public abstract class AbstractDevicesFragment<C extends BaseContract, P extends BasePresenter, B extends android.databinding.ViewDataBinding> extends BaseFragment<C, P, B>{

    private boolean fragmentReady;

    public abstract void updateDevices();

    private boolean mustLoadDevices = true;

    @Override
    public void onResume() {
        super.onResume();
        fragmentReady = true;
    }

    public boolean isFragmentReady() {
        return fragmentReady;
    }

    public boolean isMustLoadDevices() {
        return mustLoadDevices;
    }

    public void setMustLoadDevices(boolean mustLoadDevices) {
        this.mustLoadDevices = mustLoadDevices;
    }
}
