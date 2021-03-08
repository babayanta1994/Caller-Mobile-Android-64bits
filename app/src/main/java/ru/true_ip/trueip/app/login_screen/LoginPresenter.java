package ru.true_ip.trueip.app.login_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.db.entity.SettingsDb;
import ru.true_ip.trueip.db.entity.UserDb;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.LangModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.UserModel;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;


import static ru.true_ip.trueip.base.BaseRouter.Destination.OBJECTS_SCREEN;

public class LoginPresenter extends BasePresenter<LoginContract> {

    public static final String TAG = LoginPresenter.class.getSimpleName();

    public ObservableField<String> flatNumber = new ObservableField<>("");
    public ObservableField<String> activationCode = new ObservableField<>("");

    public ObservableBoolean buttonsEnabled = new ObservableBoolean(true);
    public ObservableBoolean isAgreementAccepted = new ObservableBoolean(false);

    private ObjectDb objectDb;

    private Context context;

    private boolean hasSipNumber;
    boolean gotPermissions = false;

    @Override
    public void checkServerStatus(Context context) {

    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!gotPermissions)
                    return;
                if (validateFields()) {
                    buttonsEnabled.set(false);
                    Utils.closeSoftKeyboard(context);
                    login(v);
                }
                break;
            case R.id.btn_configure_locally:
                if (!gotPermissions)
                    return;
                buttonsEnabled.set(false);
                loginLocally();
                break;
        }
    }

    private void continueToMainScreen() {
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.BUNDLE_DESTINATION, OBJECTS_SCREEN);
        bundle.putBoolean(Constants.OBJECT_HAS_SIP_NUMBER, hasSipNumber);

        moveToMainScreen(bundle);
    }

    private boolean validateFields() {
        boolean valid = true;
        LoginContract contract = getContract();
        if (contract != null) {
            contract.dismissCodeError();
            contract.dismissFlatNumberError();
        }
        String apartmentNumber = flatNumber.get();

        if (apartmentNumber == null || apartmentNumber.isEmpty()) {
            if (contract != null) {
                contract.showFlatNumberError(context.getString(R.string.login_number_text_error_empty, "* "));
            }
            valid = false;
        } else {
            if (!apartmentNumber.matches(Constants.APARTMENT_NUMBER_FORMAT_REGEXP)) {
                if (contract != null) {
                    contract.showFlatNumberError(context.getString(R.string.login_number_text_error, "* "));
                }
                valid = false;
            }
        }

        String code = activationCode.get();
        if (code == null || code.isEmpty()) {
            if (contract != null) {
                contract.showCodeError(context.getString(R.string.login_code_text_error_empty, "* "));
            }
            valid = false;
        } else {
            if (!code.matches(Constants.APARTMENT_ACTIVATION_CODE_REGEXP)) {
                if (contract != null) {
                    if (code.length() < Constants.ACTIVATION_CODE_LENGTH + 3) {
                        contract.showCodeError(context.getString(R.string.login_number_not_complete, "* "));
                    } else {
                        contract.showCodeError(context.getString(R.string.login_code_text_error, "* "));
                    }
                }
                valid = false;
            }
        }
        return valid;
    }

    private void moveToMainScreen(Bundle bundle) {
        LoginContract contract = getContract();
        if(contract != null) {
            contract.getRouter().moveTo(BaseRouter.Destination.MAIN_SCREEN, bundle);
        }
    }

    private void loginLocally() {
        UserDb user = new UserDb();
        repositoryController.addUser(user).subscribe(() -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_DESTINATION, OBJECTS_SCREEN);
            moveToMainScreen(bundle);
        });
        //save settings to db
        SettingsDb settingsDb = new SettingsDb();
        settingsDb.setCall_type(1);
        settingsDb.setUser_id(user.getUser_id());
        repositoryController.addSettings(settingsDb).subscribe(() -> {}
            , throwable -> throwable.printStackTrace());
    }

    private void login(View v) {
        Logger.error(TAG, "login: " + flatNumber.get() + " : " + activationCode.get());
        String language = getLanguageTag(context);
        Logger.error(TAG, "language " + language);
        apiController.activation(language, flatNumber.get(), activationCode.get(), new Callback<Response<UserModel>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(Response<UserModel> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    UserModel userModel = response.body();
                    if (userModel != null) {
                        hasSipNumber = userModel.getSip_client_number() != null;
                        UserDb userDb = new UserDb(userModel);
                        repositoryController.addUser(userDb).subscribe(() -> {
                                    Logger.error(TAG, "User Added");
                                    createObject(userModel, flatNumber.get(), activationCode.get());
                                    Logger.error(TAG,"Setting Locale");
                                    String language = getLanguageTag(context);
                                    setLanguage(objectDb, language);
                                }, throwable ->
                                        Logger.error(TAG, "User NOT Added" + throwable.getMessage())
                        );
                    }
                } else {
                    buttonsEnabled.set(true);
                    if (status == 429) { // 429 Too Many Requests
                        DialogHelper.createErrorDialog(context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_too_many_requests));
                    } else {
                        ErrorApiResponse errorApiResponse = new ErrorApiResponse(response.errorBody());
                        String msg = Utils.getDetailedErrorMessage(errorApiResponse);

                        if (!msg.isEmpty()) {
                            DialogHelper.createErrorDialog(v.getContext(),
                                    v.getContext().getResources().getString(R.string.text_error_dialog_title),
                                    msg);
                        } else {
                            DialogHelper.createErrorDialog(v.getContext(),
                                    v.getContext().getString(R.string.text_error_dialog_title),
                                    v.getContext().getString(R.string.text_error_default_message));
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                buttonsEnabled.set(true);
                DialogHelper.createErrorDialog(v.getContext(),
                        v.getContext().getString(R.string.text_error_dialog_title),
                        v.getContext().getString(R.string.text_activation_timeout));
            }
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
        if (concierge_sip_number != null && concierge_sip_number.length() > 0) {
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

        SipAccountData sipAccountData = new SipAccountData(objectDb.getIp_address(),
                objectDb.getSip_number(),
                objectDb.getPassword(),
                objectDb.getPort());
        SipServiceCommands.createAccount(App.getContext(), sipAccountData);

        repositoryController.addObject(objectDb).subscribe(() -> {
            Logger.error(TAG, "Object successfully added");
            loadCameras(objectDb, objectDb.getObject_id());
        });
    }

    //
    //loadCameras
    //
    private void loadCameras(ObjectDb objectDb, int object_id) {
        apiController.getCamerasShort(context, objectDb, new Callback<Response<ArrayList<CameraShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<CameraShortModel>> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ArrayList<CameraShortModel> body = response.body();
                    if (body != null) {
                        Logger.error(TAG, "Camera Response not null");
                        for (CameraShortModel cameraModel : body) {
                            repositoryController.addDevice(new DevicesDb(cameraModel, object_id, 1)).subscribe(() -> {
                            }, throwable ->
                                throwable.printStackTrace());
                        }
                        loadPanels(objectDb, object_id);
                    }
                } else {
                    if (status == 429) { // 429 Too Many Requests
                        DialogHelper.createErrorDialog(context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_too_many_requests));
                    } else {

                        ErrorApiResponse errorResponse = new ErrorApiResponse(response.errorBody());
                        String msg = Utils.getDetailedErrorMessage(errorResponse);

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
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, "Load cameras on error" + error);
            }
        });
    }

    //
    //loadPanels
    //
    private void loadPanels(ObjectDb objectDb, int object_id) {
        apiController.getPanelsShort(context, objectDb, new Callback<Response<ArrayList<PanelShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<PanelShortModel>> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ArrayList<PanelShortModel> body = response.body();
                    if (body != null) {
                        Logger.error(TAG, "Panel Response not null");
                        for (PanelShortModel panelShortModel : body) {
                            repositoryController.addDevice(new DevicesDb(panelShortModel, object_id, 1)).subscribe(() -> {
                            }, throwable ->
                                throwable.printStackTrace());
                        }
                    } else {
                        return;
                    }
                } else {
                    if (status == 429) { // 429 Too Many Requests
                        DialogHelper.createErrorDialog(context,
                                context.getString(R.string.text_error_dialog_title),
                                context.getString(R.string.text_error_too_many_requests));
                    } else {

                        ErrorApiResponse errorResponse = new ErrorApiResponse(response.errorBody());
                        String msg = Utils.getDetailedErrorMessage(errorResponse);

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
                continueToMainScreen();
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, error);
            }
        });
    }
}
