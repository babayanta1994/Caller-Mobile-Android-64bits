package ru.true_ip.trueip.app.add_hlm_object_screen;

import android.annotation.SuppressLint;
import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import retrofit2.Response;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.db.entity.UserDb;
import ru.true_ip.trueip.javaFCM.TokenHelper;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.LangModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.UserModel;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;

import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

public class AddHlmObjectPresenter extends BasePresenter<AddHlmObjectContract> {

    public static final String TAG = AddHlmObjectPresenter.class.getSimpleName();

    public ObservableBoolean isButtonEnabled = new ObservableBoolean(true);
    public ObservableField<String> flatNumber = new ObservableField<>("");
    public ObservableField<String> activationCode = new ObservableField<>("");

    public Context context;
    private ObjectDb objectDb;

    private boolean dontCloseActivity;

    public void setContext(Context value) {
        context = value;
    }

    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                moveBackward();
                break;
            case R.id.btn_login:
                isButtonEnabled.set(false);
                if (validateFields()) {
                    Utils.closeSoftKeyboard(context);
                    login(v);
                } else {
                    isButtonEnabled.set(true);
                }
                break;
        }
    }

    private void moveBackward() {
        AddHlmObjectContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }
    //
    //validateFields
    //
    private boolean validateFields() {
        boolean result = true;
        StringBuilder messageBuilder = new StringBuilder();

        String apartmentNumber = flatNumber.get();
        String apartmentCode = activationCode.get();
        if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
            if (!apartmentNumber.matches(Constants.APARTMENT_NUMBER_FORMAT_REGEXP)) {
                result = false;
                messageBuilder.append(context.getString(R.string.login_number_text_error, ""));
            }
        } else {
            result = false;
            messageBuilder.append(context.getString(R.string.login_number_text_error_empty, ""));
        }

        if (apartmentCode != null && !apartmentCode.isEmpty()) {
            if (!apartmentCode.matches(Constants.APARTMENT_ACTIVATION_CODE_REGEXP)) {
                result = false;
                if (messageBuilder.length() > 0) {
                    messageBuilder.append("\n");
                }

                if (apartmentCode.length() < Constants.ACTIVATION_CODE_LENGTH + 3) {
                    messageBuilder.append(context.getString(R.string.login_number_not_complete, ""));
                } else {
                    messageBuilder.append(context.getString(R.string.login_code_text_error, ""));
                }
            }
        } else {
            result = false;
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n");
            }

            messageBuilder.append(context.getString(R.string.login_code_text_error_empty, ""));
        }

        if (messageBuilder.length() > 0) {
            DialogHelper.createErrorDialog(context,
                    context.getString(R.string.text_error_dialog_title),
                    messageBuilder.toString());
        }

        return result;
    }

    //
    //login
    //
    private void login(View v) {
        Logger.error(TAG, "login: " + flatNumber.get() + " : " + activationCode.get());
        String language = getLanguageTag(context);
        Logger.error(TAG, "language " + language);
        apiController.activation(language, flatNumber.get(), activationCode.get(), new Callback<Response<UserModel>>() {
            @Override
            public void onSuccess(Response<UserModel> response) {
                if (response.isSuccessful()) {
                    createNewObject(response);
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onError(String message) {
                Logger.error(TAG, "Login onError with message =" + message);
                try {
                    //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Toast.makeText(context, context.getString(R.string.text_activation_timeout) , Toast.LENGTH_LONG).show();
                } catch (Throwable t) {
                    Logger.error(TAG, message);
                }
                isButtonEnabled.set(true);
            }
        });
    }
    //
    //createNewObject
    //
    @SuppressLint("CheckResult")
    private void createNewObject(Response<UserModel> response) {
        UserModel userModel = response.body();
        if (userModel != null) {
            repositoryController.getObject(userModel.getId()).subscribe((obj, throwable) -> {
                if (obj != null && throwable == null) {
                    isButtonEnabled.set(true);
                    logout(userModel);

                    DialogHelper.createErrorDialog(
                            context,
                            context.getString(R.string.text_error_dialog_title),
                            context.getString(R.string.text_error_object_exists));
                } else {
                    if (throwable instanceof EmptyResultSetException) {
                        if (userModel.getSip_client_number() == null || userModel.getSip_client_password() == null) {
                            dontCloseActivity = true;
                            DialogHelper.createErrorDialog(context,
                                    context.getString(R.string.text_warning_dialog_title),
                                    context.getString(R.string.text_object_without_sip),
                                    (dlg, i) -> { dlg.dismiss(); closeActivity(); });
                        }
                        UserDb userDb = new UserDb(userModel);
                        repositoryController.addUser(userDb).subscribe(() -> {
                            Logger.error(TAG, "User Added");
                            createObject(userModel, flatNumber.get(), activationCode.get());

                            String language = getLanguageTag(context);
                            setLanguage(objectDb, language);

                        }, throwable1 -> Logger.error(TAG, "User NOT Added" + throwable1.getMessage()));
                    } else {
                        throwable.printStackTrace();
                        isButtonEnabled.set(true);
                        logout(userModel);

                        DialogHelper.createErrorDialog(
                                context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_default_message));

                    }
                }
            });
        }
    }
    //
    //handleError
    //
    private void handleError(Response<UserModel> response) {
        isButtonEnabled.set(true);

        if (response.code() == 429) { // 429 Too Many Requests
            DialogHelper.createErrorDialog(context,
                    context.getString(R.string.text_error_dialog_title),
                    context.getString(R.string.text_error_too_many_requests));
        } else {

            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
            String msg = Utils.getDetailedErrorMessage(error);

            if (!msg.isEmpty()) {
                DialogHelper.createErrorDialog(context,
                        context.getString(R.string.text_error_dialog_title),
                        msg);
            } else {
                DialogHelper.createErrorDialog(context,
                        context.getString(R.string.text_error_dialog_title),
                        context.getString(R.string.text_error_default_message));
            }
        }
    }
    //
    //logout
    //
    private void logout(UserModel userModel) {
        apiController.logout(userModel, new Callback<Response<Void>>() {
            @Override
            public void onSuccess(Response<Void> responseLogout) {
                super.onSuccess(responseLogout);
                int status = responseLogout.code();

                if (status != 200 && status != 401) {
                    Logger.error(TAG, "haven't succeeded to logout. Status: " + status);

                    ErrorApiResponse response = new ErrorApiResponse(responseLogout.errorBody());
                    String message = Utils.getDetailedErrorMessage(response);

                    if (!message.isEmpty()) {
                        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(App.getContext(), R.string.text_error_default_message, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onError(String error) { super.onError(error); }
        });
    }
    //
    //setLanguage
    //
    private void setLanguage(ObjectDb objectDb, String lang) {
        apiController.setLanguage(context, objectDb, lang, new Callback<Response<LangModel>>() {
            @Override
            public void onSuccess(Response<LangModel> object) {
                super.onSuccess(object);
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        });
    }

    //
    //createObject
    //
    @SuppressLint("CheckResult")
    private void createObject(UserModel userModel, String flatNumber, String activationCode) {
        objectDb = new ObjectDb();

        objectDb.setIsBlocked(false);

        objectDb.setObject_id(userModel.getId());

        objectDb.setFlat_number(flatNumber);

        objectDb.setActivation_code(activationCode);

        objectDb.setName(userModel.getAddress());

        objectDb.setIp_address(userModel.getSip_server_address());

        objectDb.setPort(userModel.getSip_server_port());

        //objectDb.setPassword(userDb.getPassword());
        objectDb.setPassword(userModel.getSip_client_password());

        //objectDb.setSip_number(userDb.getSipNumber());
        objectDb.setSip_number(userModel.getSip_client_number());

        String concierge_sip_number = userModel.getConcierge_sip_number();
        if ( concierge_sip_number != null && concierge_sip_number.length() > 0 ) {
            objectDb.setHas_concierge(true);
            objectDb.setConcierge_number(concierge_sip_number);
        } else {
            objectDb.setHas_concierge(false);
            objectDb.setConcierge_number("");
        }

        objectDb.setIs_cloud(1);

        objectDb.setUser_id(userModel.getId());

        objectDb.setServerUrl(userModel.getServer().getUrl());

        objectDb.setLicenseType(userModel.getServer().getLicense());

        objectDb.setIsServerActive(Constants.SERVER_STATUS_ACTIVE);

        repositoryController.addObject(objectDb).subscribe(() -> {
            Logger.error(TAG, "Object successfully added");
            SipAccountData sipAccountData = new SipAccountData(objectDb.getIp_address(),
                    objectDb.getSip_number(),
                    objectDb.getPassword(),
                    objectDb.getPort());
            SipServiceCommands.createAccount(App.getContext(), sipAccountData);
            loadCameras(objectDb, objectDb.getObject_id());
            TokenHelper.sendPushToken(context, repositoryController, apiController);
        });
    }
    //
    //loadCameras
    //
    private void loadCameras(ObjectDb objectDb, int object_id) {
        apiController.getCamerasShort(context, objectDb, new Callback<Response<ArrayList<CameraShortModel>>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(Response<ArrayList<CameraShortModel>> response) {
                if (response.code() == 200) {
                    ArrayList<CameraShortModel> body = response.body();
                    if (body != null) {
                        Logger.error(TAG,"Camera Response not null");
                        for (CameraShortModel cameraModel: body ) {
                            repositoryController.addDevice(new DevicesDb(cameraModel,object_id, 1)).subscribe(() -> {
                            }, Throwable::printStackTrace);
                        }
                        loadPanels(objectDb, object_id);
                    }
                } else {
                    ErrorApiResponse errorApiResponse = new ErrorApiResponse(response.errorBody());
                    String msg = Utils.getDetailedErrorMessage(errorApiResponse);

                    if (!msg.isEmpty()) {
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                msg);
                    } else {
                        DialogHelper.createErrorDialog(context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_default_message));
                    }
                }
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG,"Load cameras on error" + error);
            }
        });
    }
    //
    //loadPanels
    //
    private void loadPanels(ObjectDb objectDb, int object_id) {
        apiController.getPanelsShort(context, objectDb, new Callback<Response<ArrayList<PanelShortModel>>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(Response<ArrayList<PanelShortModel>> response) {
                if (response.code() == 200) {
                    ArrayList<PanelShortModel> body = response.body();
                    if (body != null) {
                        Logger.error(TAG,"Panel Response not null");
                        for (PanelShortModel panelShortModel: body ) {
                            repositoryController.addDevice(new DevicesDb(panelShortModel,object_id, 1)).subscribe(() -> {
                            }, Throwable::printStackTrace);
                        }

                        if (!dontCloseActivity) {
                            closeActivity();
                        }
                    }
                } else {
                    ErrorApiResponse errorApiResponse = new ErrorApiResponse(response.errorBody());
                    String msg = Utils.getDetailedErrorMessage(errorApiResponse);

                    if (!msg.isEmpty()) {
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                msg);
                    } else {
                        DialogHelper.createErrorDialog(context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_default_message));
                    }
                }
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, error);
            }
        });
    }

    private void closeActivity() {
        moveBackward();
        Intent intent = new Intent();
        intent.setAction(Constants.FINISH_ACTIVITY);
        context.sendBroadcast(intent);
    }
}
