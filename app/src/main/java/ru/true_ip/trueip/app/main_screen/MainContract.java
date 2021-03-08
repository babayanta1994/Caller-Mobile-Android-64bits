package ru.true_ip.trueip.app.main_screen;


import ru.true_ip.trueip.base.BaseContract;

/**
 * Created by user on 10-Sep-17.
 */

interface MainContract extends BaseContract {

    void closeDrawer();
    void notifyObjectChanged();
    void notifyDevicesUpdated();
}
