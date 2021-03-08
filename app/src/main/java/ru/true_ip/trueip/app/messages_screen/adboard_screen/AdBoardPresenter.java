package ru.true_ip.trueip.app.messages_screen.adboard_screen;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.adboard_screen.adapters.AdBoardAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.AdvertModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 *
 * Created by Andrey Filimonov on 29.12.2017.
 */

public class AdBoardPresenter extends BasePresenter<AdBoardContract> {
    private final static String TAG = AdBoardPresenter.class.getSimpleName();
    private Context context;
    private int objectId = -1;
    private ObjectDb objectDb;
    private String userToken;

    public ObservableField<AdBoardAdapter> adBoardAdapter = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);

    //
    //support
    //
    public void setContext(Context context) { this.context = context; }
    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, 0);
    }

    public void getAds() {
        if (userToken == null || userToken.isEmpty()) {
            if(objectId != 0) {
                repositoryController.getObject(objectId).subscribe(objectDb ->
                        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb -> {
                            this.objectDb = objectDb;
                            userToken = userDb.getToken();
                            getAdvertList();
                        }));
            }
        } else {
            getAdvertList();
        }
    }

    private void getAdvertList() {
        repositoryController.getObject(objectId).subscribe(objectDb1 ->
                apiController.getAdvertList(context, objectDb1, new Callback<Response<List<AdvertModel>>>() {
                    @Override
                    public void onSuccess(Response<List<AdvertModel>> response) {
                        super.onSuccess(response);
                        int status = response.code();
                        if (response.isSuccessful()) {
                            AdBoardAdapter adapter = new AdBoardAdapter(new ArrayList<>());
                            adapter.addOnItemClickListener(((position, item) ->
                                showAdvertAndMarkAsRed(position, item)
                            ));
                            List<AdvertModel> items = response.body();
                            if (items != null) {
                                adapter.setItems(items);
                            }
                            adBoardAdapter.set(adapter);
                        } else {
                            ErrorApiResponse errorResponse = new ErrorApiResponse(response.errorBody());

                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    errorResponse.getError());

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
                }));
    }

    private void showAdvertAndMarkAsRed(int pos, AdvertModel item) {
        DialogHelper.createInfoDialog(context, item.getTheme(), item.getText());
        if (item.isIs_viewed() == 0) {
            if (userToken != null && !userToken.isEmpty()) {
                setAdvertAsRead(pos, item);
            } else {
                repositoryController.getObject(objectId).subscribe(objectDb ->
                        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb -> {
                            userToken = userDb.getToken();
                            setAdvertAsRead(pos, item);
                        }));
            }
        }
    }

    private void setAdvertAsRead (int pos, AdvertModel item) {
        repositoryController.getObject(objectId).subscribe(objectDb1 ->
                apiController.setAdvertAsRead(context, objectDb1, String.valueOf(item.getId()), new Callback<Response<AdvertModel>>() {
                    @Override
                    public void onSuccess(Response<AdvertModel> response) {
                        super.onSuccess(response);
                        if(response.isSuccessful()) {
                            adBoardAdapter.get().updateItem(pos, response.body());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                    }
                }));
    }

    public void onRefreshSwiped() {
        getAds();
    }
}
