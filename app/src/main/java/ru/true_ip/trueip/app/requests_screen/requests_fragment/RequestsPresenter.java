package ru.true_ip.trueip.app.requests_screen.requests_fragment;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.requests_screen.new_request_screen.NewRequestActivity;
import ru.true_ip.trueip.app.requests_screen.requests_fragment.adapters.RequestsAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsPresenter extends BasePresenter<RequestsContract> {

    public ObservableField<RequestsAdapter> requestsAdapter = new ObservableField<>(new RequestsAdapter(R.layout.item_request, new ArrayList<>()));

    public ObservableBoolean showFloatingButton = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);

    private int requestsOfStatus;
    private int objectId;

    private String baseUrl;

    private Context context;

    @Override
    public void attachToView(RequestsContract contract) {
        super.attachToView(contract);
    }

    public void getRequests() {
        if (objectId != 0) {
            repositoryController.getObject(objectId).subscribe(objectDb -> {
                    baseUrl = objectDb.getServerUrl();
                    apiController.getClaimsList(context, objectDb, new Callback<Response<ArrayList<ClaimModel>>>() {
                        @Override
                        public void onSuccess(Response<ArrayList<ClaimModel>> response) {
                            super.onSuccess(response);
                            int status = response.code();
                            if (response.isSuccessful()) {
                                ArrayList<ClaimModel> responseBody = response.body();
                                if (responseBody != null) {
                                    requestsAdapter.get().setItems(filterItems(responseBody));
                                }
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
            });
        }
    }

    private List<ClaimModel> filterItems(List<ClaimModel> sourceItems) {
        List<ClaimModel> items;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (requestsOfStatus) {
                case Constants.ACTIVE_REQUESTS:
                    items = sourceItems.stream()
                            .filter(item -> item.getStatus().toLowerCase().equals(Constants.STATUS_NEW) ||
                                    item.getStatus().toLowerCase().equals(Constants.STATUS_IN_WORK)).collect(Collectors.toList());
                    break;
                case Constants.COMPLETED_REQUESTS:
                    items = sourceItems.stream()
                            .filter(item -> item.getStatus().toLowerCase().equals(Constants.STATUS_DONE) ||
                                    item.getStatus().toLowerCase().equals(Constants.STATUS_REJECTED)).collect(Collectors.toList());
                    break;
                default:
                    items = sourceItems;
            }
        } else {
            items = new ArrayList<>();
            switch(requestsOfStatus) {
                case Constants.ACTIVE_REQUESTS:
                    for (ClaimModel item : sourceItems) {
                        if(item.getStatus().toLowerCase().equals(Constants.STATUS_NEW) ||
                                item.getStatus().toLowerCase().equals(Constants.STATUS_IN_WORK)) {
                            items.add(item);
                        }
                    }
                    break;
                case Constants.COMPLETED_REQUESTS:
                    for (ClaimModel item : sourceItems) {
                        if(item.getStatus().toLowerCase().equals(Constants.STATUS_DONE) ||
                                item.getStatus().toLowerCase().equals(Constants.STATUS_REJECTED)) {
                            items.add(item);
                        }
                    }
                    break;
                default:
                    items = sourceItems;
            }
        }

        return items;
    }

    public void setExtras(Bundle bundle) {
        showFloatingButton.set(bundle.getBoolean(Constants.BUNDLE_SHOW_FLOATING_BUTTON, false));
        requestsOfStatus = bundle.getInt(Constants.REQUESTS_STATUS);
        objectId = bundle.getInt(Constants.OBJECT_ID, 0);

        requestsAdapter.get().addOnItemClickListener((i, item) -> {
            Bundle extras = new Bundle();

            extras.putInt(Constants.OBJECT_ID, objectId);
            extras.putString(Constants.BASE_URL, baseUrl);

            if (item.getStatus().toLowerCase().equals(Constants.STATUS_NEW)) {
                extras.putBoolean(Constants.REQUEST_EDITABLE, true);
            } else {
                extras.putBoolean(Constants.REQUEST_EDITABLE, false);
            }
            extras.setClassLoader(ClaimModel.class.getClassLoader());
            extras.putParcelable(Constants.REQUEST_OBJECT, item);

            NewRequestActivity.startForResult(context, extras);
        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onClickAdd (View v) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (mayUseApi(objectDb)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, objectId);
                bundle.putString(Constants.BASE_URL, objectDb.getServerUrl());
                NewRequestActivity.startForResult(context, bundle);
            }
        });
    }

    private boolean mayUseApi(ObjectDb objectDb) {
        return (objectDb.getIsServerActive() != null && objectDb.getIsServerActive() == 1 && !objectDb.isBlocked()) ||
                (objectDb.getIsServerActive() == null && !objectDb.isBlocked());
    }

    public void onRefreshSwiped() {
        getRequests();
    }
}
