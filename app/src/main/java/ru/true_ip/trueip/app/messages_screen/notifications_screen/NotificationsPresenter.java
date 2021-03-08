package ru.true_ip.trueip.app.messages_screen.notifications_screen;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.notifications_screen.adapters.NotificationsAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.NotificationModel;
import ru.true_ip.trueip.models.responses.NotificationsListModel;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by ektitarev on 29.12.2017.
 */

public class NotificationsPresenter extends BasePresenter<NotificationsContract> {

    public ObservableField<NotificationsAdapter> notificationsAdapter = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);

    private Context context;
    private int objectId;
    private ObjectDb objectDb;

    public void onHomeClick (View v) {
        NotificationsContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID);
    }

    public void getNotifications() {

        if (objectDb == null) {
            if (objectId != 0) {
                repositoryController.getObject(objectId).subscribe(objectDb ->
                    repositoryController.getUserById(objectDb.user_id).subscribe(userDb -> {
                        this.objectDb = objectDb;
                        getNotificationsList();
                    }));
            }
        } else {
            getNotificationsList();
        }
    }

    private void getNotificationsList() {
        apiController.getNotificationsList(context, objectDb, new Callback<Response<List<NotificationModel>>>() {
            @Override
            public void onSuccess(Response<List<NotificationModel>> response) {
                super.onSuccess(response);
                int status = response.code();
                if (status == 200) {
                    NotificationsAdapter adapter = new NotificationsAdapter(new ArrayList<>());
                    adapter.addOnItemClickListener(((position, item) -> {
                        showNotificationAndMarkAsRed(position, item);
                    }));
                    List<NotificationModel> items = response.body();

                    if (items != null) {
                        adapter.setItems(items);
                    }
                    notificationsAdapter.set(adapter);
                } else {
                    ErrorApiResponse error = new ErrorApiResponse(response.errorBody());

                    DialogHelper.createErrorDialog(context,
                            context.getResources().getString(R.string.text_error_dialog_title),
                            error.getError());
                    if (status == 401 || status == 403) {
                        SipServiceCommands.removeAccount(context, objectDb.getIdUri());
                    }
                }
                isLoading.notifyChange();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                isLoading.notifyChange();
                DialogHelper.createErrorDialog(context,
                        context.getResources().getString(R.string.text_error_dialog_title),
                        context.getResources().getString(R.string.text_error_default_message));
            }
        });
    }

    private void showNotificationAndMarkAsRed(int pos, NotificationModel item) {
        DialogHelper.createInfoDialog(context, item.getTheme(), item.getText());
        if (objectDb != null) {
            setNotificationAsRead(pos, item);
        } else {
            repositoryController.getObject(objectId).subscribe(objectDb -> {
                this.objectDb = objectDb;
                setNotificationAsRead(pos, item);
            });
        }
    }

    private void setNotificationAsRead(int pos, NotificationModel item) {
        apiController.setNotificationAsRead(context, objectDb, String.valueOf(item.getId()), new Callback<Response<NotificationModel>>() {
            @Override
            public void onSuccess(Response<NotificationModel> response) {
                super.onSuccess(response);
                if (response.isSuccessful()) {
                    notificationsAdapter.get().updateItem(pos, response.body());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    public void onRefreshSwiped() {
        //isLoading.set(true);
        getNotifications();
    }
}
