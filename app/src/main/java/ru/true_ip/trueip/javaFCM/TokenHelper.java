package ru.true_ip.trueip.javaFCM;


import android.annotation.SuppressLint;
import android.content.Context;

import retrofit2.Response;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.Device;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;
import ru.true_ip.trueip.service.service.Logger;

/**
 * Created by rmolodkin on 12.01.2018.
 */

public class TokenHelper {

    @SuppressLint("CheckResult")
    public static void sendPushToken(Context context, RepositoryController repositoryController, ApiControllerWithReactivation apiController) {

        String currentFCMToken = repositoryController.getToken();

        repositoryController.getHLMObjects().subscribe(hlmObjects -> {

            for (ObjectDb item : hlmObjects) {
                apiController.postPushToken(context, item, currentFCMToken, new Callback<Response<Device>>() {
                    @Override
                    public void onSuccess(Response<Device> response) {
                        super.onSuccess(response);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                    }
                });

            }
        }, throwable -> Logger.error("TokenHelper", "getHLMObjects: throwable", throwable));
    }

}
