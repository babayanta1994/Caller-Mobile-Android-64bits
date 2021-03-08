package ru.true_ip.trueip.app.add_new_object_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.Device;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.UserModel;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.ReactivationCodeDialog;
import ru.true_ip.trueip.utils.Utils;

import static ru.true_ip.trueip.utils.Constants.BUNDLE_INT_KEY;
import static ru.true_ip.trueip.utils.Constants.BUNDLE_IS_EDIT_MODE;
import static ru.true_ip.trueip.utils.Constants.TYPE_CAMERA;
import static ru.true_ip.trueip.utils.Constants.TYPE_PANEL;

public class AddNewObjectPresenter extends BasePresenter<AddNewObjectContract> {

    public static final String TAG = AddNewObjectPresenter.class.getSimpleName();

    public ObservableField<String> objectName = new ObservableField<>("");
    public ObservableField<String> ipAddress = new ObservableField<>("");
    public ObservableField<String> port = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> sip = new ObservableField<>("");
    public ObservableField<String> conciergeNum = new ObservableField<>("");
    public ObservableField<String> refreshButtonText = new ObservableField<>("");
    public ObservableBoolean isSaveButtonEnabled = new ObservableBoolean(true);
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    public ObservableBoolean isConciergeChecked = new ObservableBoolean(false);
    public ObservableBoolean isCloud = new ObservableBoolean(false);
    private List<ObjectDb> objectDbs;
    private ObjectDb objectDb;
    private String sipNumberBeforeModification;
    private String ipAddressBeforeModification;
    private int is_cloud = 0;
    private boolean dontCloseActivity;
    private Context context;

    @Override
    public void attachToView(AddNewObjectContract contract) {
        super.attachToView(contract);
        getLocalObjects();
        setListeners();
    }

    public void setContext(Context applicationContext) {
        this.context = applicationContext;
    }

    private void setListeners() {
        isConciergeChecked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                conciergeNum.set("");
            }
        });
    }

    public void getLocalObjects() {
        repositoryController.getAllObjects().subscribe(objectDbs -> {
            if (objectDbs != null) {
                this.objectDbs = objectDbs;
            }
        });
    }

    @SuppressLint("CheckResult")
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                moveBackward();
                break;
            case R.id.btn_save:
                Logger.error(TAG, "BTN Save pressed");
                if (isCloud.get()) {
                    DialogHelper.createDeleteDialog(context, R.string.text_logout_dialog_message, (dialog, i) -> {
                        dialog.dismiss();
                        deleteHLMObject(v);
                    });
                } else {
                    isSaveButtonEnabled.set(false);
                    if ( objectDb != null) {
                        SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());
                        repositoryController.deleteObject(objectDb).subscribe(this::createAndSaveData);
                    } else {
                        createAndSaveData();
                    }
                }
                break;
            case R.id.btn_delete:
                Logger.error(TAG, "BTN DELETE pressed");
                DialogHelper.createDeleteDialog(context, R.string.text_delete_object, (dialog, i) -> {
                    SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());
                    repositoryController.deleteObject(objectDb).subscribe(() -> {
                        dialog.dismiss();
                        moveBackward();
                    });
                });
                break;
            case R.id.btn_cloud_refresh_devices:
                if (!objectDb.isBlocked()) {
                    updateDevices();
                } else {
                    reactivateObjectManually();
                }
        }
    }

    //
    //reactivateObjectManually
    //
    private void reactivateObjectManually() {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity)context;
            ReactivationCodeDialog dialog = ReactivationCodeDialog.getInstance(objectDb.getName(), new ReactivationCodeDialog.OnClickListener() {
                @Override
                public void onSubmitClick(DialogInterface dialogInterface, CharSequence reactivationCode) {
                    String lang = getLanguageTag(context);
                    if (validateActivationCode(reactivationCode.toString())) {

                        apiController.reactivation(lang, objectDb.getFlat_number(), reactivationCode.toString(), new Callback<Response<UserModel>>() {
                            @Override
                            public void onSuccess(Response<UserModel> object) {
                                super.onSuccess(object);
                                if (object.isSuccessful()) {
                                    UserModel userModel = object.body();
                                    if (userModel != null) {
                                        dialogInterface.dismiss();
                                        objectDb.setActivation_code(reactivationCode.toString());
                                        objectDb.setIsBlocked(false);

                                        refreshButtonText.set(context.getString(R.string.text_refresh_devices));

                                        updateUser(objectDb, userModel);
                                        moveBackward();
                                    }
                                } else {
                                    handleError(object);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                super.onError(error);
                            }
                        });
                    }
                }

                @Override
                public void onCancelClick(DialogInterface dialogInterface) {
                    Logger.error(TAG, "user canceled manual reactivation");
                    dialogInterface.dismiss();
                }
            });

            FragmentManager manager = activity.getSupportFragmentManager();
            if (manager.findFragmentByTag(ReactivationCodeDialog.class.getSimpleName()) == null) {
                dialog.show(manager, ReactivationCodeDialog.class.getSimpleName());
            }
        }

    }

    //
    //validateActivationCode
    //

    private boolean validateActivationCode(String apartmentCode) {
        boolean result = true;
        String message = "";

        if (apartmentCode != null && !apartmentCode.isEmpty()) {
            if (!apartmentCode.matches(Constants.APARTMENT_ACTIVATION_CODE_REGEXP)) {
                result = false;
                if (apartmentCode.length() < Constants.ACTIVATION_CODE_LENGTH + 3) {
                    message = context.getString(R.string.login_number_not_complete, "");
                } else {
                    message = context.getString(R.string.login_code_text_error, "");
                }
            }
        } else {
            result = false;
            message = context.getString(R.string.login_code_text_error_empty, "");
        }

        if (!result) {
            DialogHelper.createErrorDialog(
                    context,
                    context.getString(R.string.text_error_dialog_title),
                    message);
        }

        return result;
    }

    //
    //updateUser
    //
    @SuppressLint("CheckResult")
    private void updateUser(ObjectDb objectDb, UserModel userModel) {
        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb -> {
            userDb.setToken(userModel.getApi_token());

            repositoryController.updateUser(userDb).subscribe(() -> {
                updateObject(objectDb, userModel);
                updateDevices();
            });
        });

    }

    //
    //deleteDevices
    //
    private void updateDevices() {
        SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());
        apiController.getSipNumber(context, objectDb, new Callback<Response<UserModel>>() {
            @Override
            public void onSuccess(Response<UserModel> response) {
                super.onSuccess(response);
                if (response.isSuccessful()) {
                    UserModel userModel = response.body();
                    if (userModel != null) {
                        updateObject(objectDb, userModel);
                        if (userModel.getSip_client_number()  == null || userModel.getSip_client_password() == null) {
//                            userModel.setSip_client_number("");
//                            userModel.setSip_client_password("");
//                            updateObject(objectDb, userModel);
                            dontCloseActivity = true;
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_warning_dialog_title),
                                    context.getResources().getString(R.string.text_object_without_sip),
                                    (dlg, i) -> { dlg.dismiss(); moveBackward(); });
                        }
                    }
                } else {
                    handleGetSipNumberError(response);
                }
                loadCameras(objectDb, objectDb.getObject_id());
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                loadCameras(objectDb, objectDb.getObject_id());
            }
        });

    }

    //
    //handleGetSipNumberError
    //
    private void handleGetSipNumberError(Response<UserModel> response) {
        int status = response.code();

        if (status == 401 || status == 403) {
            SipServiceCommands.removeAccount(context, objectDb.getIdUri());
        } else {
            handleError(response);
        }
    }
    //
    //handleError
    //
    private <T> void handleError(Response<T> response) {
        ResponseBody responseBody = response.errorBody();
        if (responseBody != null) {
            ErrorApiResponse errorApiResponse = new ErrorApiResponse(response.errorBody());
            String msg = Utils.getDetailedErrorMessage(errorApiResponse);

            if (!msg.isEmpty()) {
                DialogHelper.createErrorDialog(context,
                        context.getString(R.string.text_error_dialog_title),
                        msg);
            } else {
                String error = errorApiResponse.getError();
                if (error != null && !error.isEmpty()) {
                    DialogHelper.createErrorDialog(context,
                            context.getString(R.string.text_error_dialog_title),
                            error);
                } else {
                    DialogHelper.createErrorDialog(context,
                            context.getString(R.string.text_error_dialog_title),
                            context.getString(R.string.text_error_default_message));
                }
            }
        }
    }

    //
    //updateObject
    //
    private void updateObject(ObjectDb objectDb, UserModel userModel) {

        objectDb.setName(userModel.getAddress());

        objectDb.setIp_address(userModel.getSip_server_address());

        objectDb.setPort(userModel.getSip_server_port());

        //objectDb.setPassword(userDb.getPassword());
        objectDb.setPassword(userModel.getSip_client_password());

        //objectDb.setSip_number(userDb.getSipNumber());
        objectDb.setSip_number(userModel.getSip_client_number());

        String concierge_sip_number = userModel.getConcierge_sip_number();
        if (concierge_sip_number != null && !concierge_sip_number.isEmpty()) {
            objectDb.setHas_concierge(true);
            objectDb.setConcierge_number(concierge_sip_number);
        } else {
            objectDb.setHas_concierge(false);
            objectDb.setConcierge_number("");
        }

        SipAccountData sipAccountData = new SipAccountData(objectDb.getIp_address(),
                objectDb.getSip_number(),
                objectDb.getPassword(),
                objectDb.getPort());

        SipServiceCommands.createAccount(context, sipAccountData);

        repositoryController.updateObject(objectDb).subscribe(() -> {
            Logger.error(TAG, "Object successfully added");

            String currentFCMToken = repositoryController.getToken();

            apiController.postPushToken(context, objectDb, currentFCMToken, new Callback<Response<Device>>() {
                @Override
                public void onSuccess(Response<Device> object) { super.onSuccess(object); }

                @Override
                public void onError(String error) { super.onError(error); }
            });
        });
    }

    //
    //loadCameras
    //
    @SuppressLint("CheckResult")
    private void loadCameras(ObjectDb objectDb, int object_id) {
        apiController.getCamerasShort(context, objectDb, new Callback<Response<ArrayList<CameraShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<CameraShortModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<CameraShortModel> cameraShortModels = response.body();
                    if (cameraShortModels != null) {
                        addAllCameras(cameraShortModels, object_id);
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, "Load cameras on error" + error);
            }
        });
    }

    private DevicesDb findCamera(List<DevicesDb> cameras, CameraShortModel cameraShortModel) {
        for (DevicesDb devicesDb : cameras) {
            if (devicesDb.getDevice_server_id() == cameraShortModel.getId()) {
                return devicesDb;
            }
        }

        return null;
    }

    private DevicesDb findPanel(List<DevicesDb> panels, PanelShortModel cameraShortModel) {
        for (DevicesDb devicesDb : panels) {
            if (devicesDb.getDevice_server_id() == cameraShortModel.getId()) {
                return devicesDb;
            }
        }

        return null;
    }

    @SuppressLint("CheckResult")
    private void addAllCameras(List<CameraShortModel> cameraShortModels, int object_id) {
        repositoryController.getDevicesOfObjectByType(object_id, TYPE_CAMERA).subscribe(devicesDbs -> {
            List<DevicesDb> camerasToAdd = new ArrayList<>();

            for (CameraShortModel cameraModel : cameraShortModels) {
                DevicesDb foundCamera = findCamera(devicesDbs, cameraModel);
                if (foundCamera == null) {
                    camerasToAdd.add(new DevicesDb(cameraModel, object_id, 1));
                } else {
                    DevicesDb modelToUpdate = new DevicesDb(cameraModel, object_id, 1);
                    modelToUpdate.setDevice_id(foundCamera.getDevice_id());
                    modelToUpdate.setIs_favorite(foundCamera.getIs_favorite());

                    camerasToAdd.add(modelToUpdate);
                }
            }

            repositoryController.addDevices(camerasToAdd).subscribe(() -> {
                removeAbsentCameras(cameraShortModels, object_id);
            }, throwable -> Logger.error(TAG, "onClicked: Error while saving Device to DB"));
        }, throwable -> Logger.error(TAG, "onClicked: Error while selecting devices from DB"));
    }

    @SuppressLint("CheckResult")
    private void addAllPanels(List<PanelShortModel> panelShortModels, int object_id) {

        repositoryController.getDevicesOfObjectByType(object_id, Constants.TYPE_PANEL).subscribe(devicesDbs -> {
            List<DevicesDb> panelsToAdd = new ArrayList<>();

            for (PanelShortModel panelModel : panelShortModels) {
                DevicesDb foundPanel = findPanel(devicesDbs, panelModel);
                if (foundPanel == null) {
                    panelsToAdd.add(new DevicesDb(panelModel, object_id, 1));
                } else {
                    DevicesDb modelToUpdate = new DevicesDb(panelModel, object_id, 1);
                    modelToUpdate.setDevice_id(foundPanel.getDevice_id());
                    modelToUpdate.setIs_favorite(foundPanel.getIs_favorite());

                    panelsToAdd.add(modelToUpdate);
                }
            }

            repositoryController.addDevices(panelsToAdd).subscribe(
                    () -> removeAbsentPanels(panelShortModels, object_id),
                    throwable -> Logger.error(TAG, "onClicked: Error while saving Device to DB"));

        }, throwable -> Logger.error(TAG, "onClicked: Error while selecting devices from DB"));
    }

    @SuppressLint("CheckResult")
    private void removeAbsentCameras(List<CameraShortModel> cameraShortModels, int object_id) {
        repositoryController.getDevicesOfObjectByType(object_id, TYPE_CAMERA).subscribe(devicesDbs -> {
            List<DevicesDb> camerasToDelete = getCamerasToDelete(devicesDbs, cameraShortModels);
            if (!camerasToDelete.isEmpty()) {
                repositoryController.removeDevices(camerasToDelete).subscribe(
                        () -> loadPanels(objectDb),
                        throwable -> Logger.error(TAG,"can't remove cameras"));
            } else {
                loadPanels(objectDb);
            }
        }, throwable -> Logger.error(TAG, "Get all cameras: cameras not found"));
    }

    @SuppressLint("CheckResult")
    private void removeAbsentPanels(List<PanelShortModel> panelShortModels, int object_id) {
        repositoryController.getDevicesOfObjectByType(object_id, TYPE_PANEL).subscribe(devicesDbs -> {
            List<DevicesDb> panelsToDelete = getPanelsToDelete(devicesDbs, panelShortModels);
            if (!panelsToDelete.isEmpty()) {
                repositoryController.removeDevices(panelsToDelete).subscribe(
                        () -> Logger.info(TAG,"panels removed"),
                        throwable -> Logger.error(TAG,"can't remove panels"));
            }
        }, throwable -> Logger.error(TAG,"Get all panels: panels not found"));

    }

    @NonNull
    private List<DevicesDb> getCamerasToDelete(List<DevicesDb> allCameras, List<CameraShortModel> actualCameras) {
        ArrayList<DevicesDb> devicesToDelete = new ArrayList<>();
        for (DevicesDb devicesDb : allCameras) {
            boolean found = false;
            for (CameraShortModel cameraModel : actualCameras) {
                if (cameraModel.getId() == devicesDb.getDevice_server_id()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                devicesToDelete.add(devicesDb);
            }
        }

        return devicesToDelete;
    }

    @NonNull
    private List<DevicesDb> getPanelsToDelete(List<DevicesDb> allPanels, List<PanelShortModel> actualPanels) {
        ArrayList<DevicesDb> devicesToDelete = new ArrayList<>();
        for (DevicesDb devicesDb : allPanels) {
            boolean found = false;
            for (PanelShortModel panelModel : actualPanels) {
                if (panelModel.getId() == devicesDb.getDevice_server_id()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                devicesToDelete.add(devicesDb);
            }
        }

        return devicesToDelete;
    }

    //
    //loadPanels
    //
    @SuppressWarnings("CheckResult")
    private void loadPanels(ObjectDb objectDb) {
        apiController.getPanelsShort(context, objectDb, new Callback<Response<ArrayList<PanelShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<PanelShortModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<PanelShortModel> panelShortModels = response.body();
                    if (panelShortModels != null) {
                        addAllPanels(panelShortModels, objectDb.getObject_id());
                        //String message = context.getString(R.string.text_object_created) + " " + objectDb.getName() ;
                        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        if (!dontCloseActivity) {
                            moveBackward();
                        }
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, error);
            }
        });
    }

    private void moveBackward() {
        AddNewObjectContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    //
    //deleteHLMObject
    //
    @SuppressLint("CheckResult")
    private void deleteHLMObject(View v) {
        apiController.logout(objectDb, new Callback<Response<Void>>() {
            @Override
            public void onSuccess(Response<Void> responseLogout) {
                super.onSuccess(responseLogout);
                int status = responseLogout.code();
                if (status == 200 || status == 401) {
                    repositoryController.getUserById(objectDb.getUser_id()).subscribe(UsersDB -> {
                        Logger.error(TAG, "Deleting related user with id " + UsersDB.getUser_id());
                        repositoryController.deleteUser(UsersDB);
                        repositoryController.removeDevicesByObjectId(objectDb.getObject_id());
                    });
                    SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());

                    repositoryController.deleteMessagesByUserId(objectDb.getUser_id()).subscribe(() ->
                        repositoryController.deleteObject(objectDb).subscribe(() -> moveBackward()));

                } else {
                    Logger.error(TAG, "haven't succeeded to logout. Status: " + status);
                    ErrorApiResponse response = new ErrorApiResponse(responseLogout.errorBody());
                    String message = Utils.getDetailedErrorMessage(response);
                    if (!message.isEmpty()) {
                        DialogHelper.createErrorDialog(v.getContext(),
                                v.getContext().getResources().getString(R.string.text_error_dialog_title),
                                message);
                    } else {
                        DialogHelper.createErrorDialog(v.getContext(),
                                v.getContext().getString(R.string.text_error_dialog_title),
                                v.getContext().getString(R.string.text_error_default_message));
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                DialogHelper.createErrorDialog(v.getContext(),
                        v.getContext().getResources().getString(R.string.text_error_dialog_title),
                        v.getContext().getResources().getString(R.string.text_error_default_message));
            }
        });
    }

    //
    //loadObjects
    //
    public void loadObject(int objectId) {
        Logger.error(TAG, "loadObject: " + objectId);
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            Logger.error(TAG, "loadObject: " + objectDb.getName());
            this.objectDb = objectDb;
            sipNumberBeforeModification = objectDb.getSip_number();
            ipAddressBeforeModification = objectDb.getIp_address();
            showObjectData(objectDb);
        });
    }

    //
    //showObjectData
    //
    private void showObjectData(ObjectDb objectDb) {
        if (objectDb.getName() != null) {
            objectName.set(objectDb.getName());
        }
        if (objectDb.getIp_address() != null) {
            ipAddress.set(objectDb.getIp_address());
        }
        if (objectDb.getPort() != null) {
            port.set(objectDb.getPort());
        }
        if (objectDb.getPassword() != null) {
            password.set(objectDb.getPassword());
        }
        if (objectDb.getSip_number() != null) {
            sip.set(objectDb.getSip_number());
        }
        if (objectDb.getHas_concierge() != null) {
            isConciergeChecked.set(objectDb.getHas_concierge());
        }
        if (objectDb.getConcierge_number() != null) {
            conciergeNum.set(objectDb.getConcierge_number());
        }

        if (!objectDb.isBlocked()) {
            refreshButtonText.set(context.getString(R.string.text_refresh_devices));
        } else {
            refreshButtonText.set(context.getString(R.string.activation_button_text));
        }

        is_cloud = objectDb.getIs_cloud();
        isCloud.set(is_cloud != 0);
    }

    //
    //createAndSaveData
    //
    private void createAndSaveData() {
        if (objectDb == null) {
            objectDb = new ObjectDb();
        } else {
            Logger.error(TAG, "createAndSaveData: " + sipNumberBeforeModification);
            Logger.error(TAG, "createAndSaveData: " + ipAddressBeforeModification);
        }
        int indexToSkip = objectDbs.indexOf(objectDb);
        Logger.error(TAG, "createAndSaveData: " + indexToSkip);

        if (validateFields()) {
            Logger.error(TAG, "createAndSaveData: Missing field data");
            isSaveButtonEnabled.set(true);
            return;
        }
        objectDb.setName(objectName.get());
        objectDb.setIp_address(ipAddress.get());
        objectDb.setPort(port.get());
        objectDb.setPassword(password.get());
        objectDb.setSip_number(sip.get());
        objectDb.setHas_concierge(isConciergeChecked.get());
        objectDb.setConcierge_number(conciergeNum.get());
        objectDb.setIs_cloud(is_cloud);
        if (checkObject(objectDb)) {
            Logger.error(TAG, "createAndSaveData: Such object exist!");
            isSaveButtonEnabled.set(true);
            return;
        }
        //create new account
        SipAccountData sipAccountData = new SipAccountData(ipAddress.get(), sip.get(), password.get(), port.get());
        SipServiceCommands.createAccount(App.getContext(), sipAccountData);
        repositoryController.addObject(objectDb).subscribe(() -> {
            Logger.error(TAG, "createAndSaveData: Object successfully add");
            moveBackward();
            Intent intent = new Intent();
            intent.setAction(Constants.FINISH_ACTIVITY);
            context.sendBroadcast(intent);
        });
    }

    private boolean checkObject(ObjectDb saveObject) {
//        if (saveObject.getSip_number().equals(sipNumberBeforeModification)
//                && saveObject.getIp_address().equals(ipAddressBeforeModification)) {
//            return false;
//        }
//        for (int i = 0; i < objectDbs.size(); i++) {
//            if (objectDbs.get(i).getIp_address().equals(saveObject.getIp_address())
//                    && objectDbs.get(i).getSip_number().equals(saveObject.getSip_number())) {
//                getContract().displayDialog(R.string.text_object_same_data);
//                return true;
//            }
//        }
        if (saveObject.getSip_number().equals(sipNumberBeforeModification)) {
            return false;
        }
        //TODO temporary solution
        String sip_numger;
        for (int i = 0; i < objectDbs.size(); i++) {
            sip_numger = objectDbs.get(i).getSip_number();
            if (sip_numger == null)
                objectDbs.get(i).setSip_number("");
            if (objectDbs.get(i).getSip_number().equals(saveObject.getSip_number())) {
                AddNewObjectContract contract = getContract();
                if (contract != null) {
                    contract.displayDialog(R.string.text_object_same_sip_number);
                }
                return true;
            }
        }
        return false;
    }

    private boolean validateFields() {
        if (objectName.get().isEmpty()
                || ipAddress.get().isEmpty()
                || port.get().isEmpty()
                || sip.get().isEmpty()
                || password.get().isEmpty()
                || (conciergeNum.get().isEmpty() && isConciergeChecked.get())) {
            AddNewObjectContract contract = getContract();
            if (contract != null) {
                contract.displayDialog(R.string.text_need_enter_all_settings);
            }
            return true;
        }
        return false;
    }

    public void handleBundle(Bundle bundle) {
        if (bundle == null) return;
        Integer objectId = bundle.getInt(BUNDLE_INT_KEY);
        isEditMode.set(bundle.getBoolean(BUNDLE_IS_EDIT_MODE));
        if (isEditMode.get()) {
            loadObject(objectId);
        }
    }

    public void changeChecked(View view) {
        isConciergeChecked.set(!isConciergeChecked.get());
        AddNewObjectContract contract = getContract();
        if (contract != null) {
            contract.resetImeOption(isConciergeChecked.get());
        }
    }

    public void onConciergeCheckedChanged(CompoundButton compoundButton, boolean b) {
        AddNewObjectContract contract = getContract();
        if (contract != null) {
            contract.resetImeOption(b);
        }
    }
}
