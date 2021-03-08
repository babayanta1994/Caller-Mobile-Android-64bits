package ru.true_ip.trueip.app.object_screen.abstract_fragments;

import android.content.Context;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;

/**
 * Created by ektitarev on 24.01.2018.
 */

public abstract class AbstractDevicesPresenter<C extends BaseContract> extends BasePresenter<C> {

    protected boolean mustLoadDevices;

    protected Context context;

    public void setContext(Context context) { this.context = context; }

    public void setMustLoadDevices(boolean mustLoadDevices) {
        this.mustLoadDevices = mustLoadDevices;
    }

    public abstract void getDevicesList();
}
