package ru.true_ip.trueip.app.main_screen.objects_fragment;


import java.util.ArrayList;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.service.data.BroadcastEventReceiver;
import ru.true_ip.trueip.service.data.SipAccountData;

/**
 * Created by user on 11-Sep-17.
 */

public interface ObjectsContract extends BaseContract {

    void updateSipAccounts(ArrayList<SipAccountData> accountsList);

    void registerBroadcastReceiver(BroadcastEventReceiver broadcastEventReceiver);

    void unregisterBroadcastReceiver(BroadcastEventReceiver broadcastEventReceiver);

    void getAllAccountsState();

    void reloadHLMObjects();
}
