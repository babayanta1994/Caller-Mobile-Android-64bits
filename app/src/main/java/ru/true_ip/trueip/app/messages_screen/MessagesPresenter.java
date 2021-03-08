package ru.true_ip.trueip.app.messages_screen;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.adboard_screen.AdBoardActivity;
import ru.true_ip.trueip.app.messages_screen.comments.CommentsActivity;
import ru.true_ip.trueip.app.messages_screen.notifications_screen.NotificationsActivity;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.UnreadMessagesModel;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class MessagesPresenter extends BasePresenter<MessagesContract> {
    public ObservableField<String> adboardCounter = new ObservableField<>("");
    public ObservableField<String> notificationsCounter = new ObservableField<>("");

    public ObservableBoolean adboardCounterVisible = new ObservableBoolean(false);
    public ObservableBoolean notificationsCounterVisible = new ObservableBoolean(false);

    private MessagesActivity context;
    private int objectId = -1;

    public void setContext(MessagesActivity context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, -1);
    }

    @Override
    protected void onObjectChanged(ObjectDb objectDb) {
        super.onObjectChanged(objectDb);
        displayData();
    }

    public void displayData() {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (checkIsApiAvailable(objectDb)) {
                apiController.getUnreadMessages(context, objectDb, new Callback<Response<UnreadMessagesModel>>() {
                    @Override
                    public void onSuccess(Response<UnreadMessagesModel> response) {
                        super.onSuccess(response);
                        int status = response.code();
                        if (response.isSuccessful()) {
                            UnreadMessagesModel responseBody = response.body();
                            if (responseBody != null) {
                                adboardCounterVisible.set(false);
                                notificationsCounterVisible.set(false);
                                if (responseBody.getAdvert() > 0) {
                                    adboardCounterVisible.set(true);
                                    adboardCounter.set(Integer.toString(responseBody.getAdvert()));
                                }
                                if (responseBody.getNotice() > 0) {
                                    notificationsCounterVisible.set(true);
                                    notificationsCounter.set(Integer.toString(responseBody.getNotice()));
                                }
                            }
                        } else {
                            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                            DialogHelper.createErrorDialog(context, context.getResources().getString(R.string.text_error_dialog_title), error.getError());

                            if (status == 401 || status == 403) {
                                SipServiceCommands.removeAccount(context, objectDb.getIdUri());
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        DialogHelper.createErrorDialogForActivity(context, context.getResources().getString(R.string.text_error_dialog_title), error);
                    }
                });
            } else {
                adboardCounterVisible.set(false);
                notificationsCounterVisible.set(false);
            }
        });

    }

    public void onAdBoardClick(View v) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (checkIsApiAvailable(objectDb)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, objectId);
                AdBoardActivity.start(context, bundle);
            } else {
                showProLicenseRequiredMessage();
            }
        });
    }

    public void onNotificationsClick(View v) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (checkIsApiAvailable(objectDb)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, objectId);
                NotificationsActivity.start(context, bundle);
            } else {
                showProLicenseRequiredMessage();
            }
        });
    }

    public void onResponsesClick(View v) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (checkIsApiAvailable(objectDb)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, objectId);
                CommentsActivity.start(context, bundle);
            } else {
                showProLicenseRequiredMessage();
            }
        });
    }

    private boolean checkIsApiAvailable(ObjectDb objectDb) {
        String license = objectDb.getLicenseType();
        return license != null && license.equals(Constants.LICENSE_TYPE_PRO) || license == null;
    }

    private void showProLicenseRequiredMessage() {
        DialogHelper.createInfoDialog(context,
                context.getString(R.string.text_error_dialog_title),
                context.getString(R.string.text_unavailable_in_lite));
    }
}
