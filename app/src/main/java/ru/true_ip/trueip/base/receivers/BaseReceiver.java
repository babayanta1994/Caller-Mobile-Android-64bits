package ru.true_ip.trueip.base.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;

/**
 * Created by ektitarev on 12.09.2018.
 */

public class BaseReceiver extends BroadcastReceiver {

    @Inject
    protected ApiControllerWithReactivation apiController;

    @Inject
    protected RepositoryController repositoryController;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getMainComponent().inject(this);
    }
}
