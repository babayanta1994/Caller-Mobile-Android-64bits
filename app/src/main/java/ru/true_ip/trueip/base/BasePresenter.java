package ru.true_ip.trueip.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.Device;
import ru.true_ip.trueip.models.responses.LangModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.ServerStatusModel;
import ru.true_ip.trueip.models.responses.UserModel;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

import static ru.true_ip.trueip.utils.Constants.TYPE_CAMERA;
import static ru.true_ip.trueip.utils.Constants.TYPE_PANEL;

/**
 * Created by user on 07-Sep-17.
 */

public class BasePresenter<C extends BaseContract> {

    private static final String TAG = BasePresenter.class.getSimpleName();

    @Inject
    protected ApiControllerWithReactivation apiController;

    @Inject
    protected RepositoryController repositoryController;


    public BasePresenter() {
        App.getMainComponent().inject((BasePresenter<BaseContract>) this);
    }

    private WeakReference<C> contractReference = null;

    public void attachToView(C contract) {
        contractReference = new WeakReference<>(contract);
    }

    public void detachView() {
        if (contractReference != null)
            contractReference.clear();
        contractReference = null;
    }

    protected C getContract() {
        if (contractReference == null) {
            return null;
        }
        return contractReference.get();
    }

    protected void onObjectChanged(ObjectDb objectDb) {}

    @SuppressLint("CheckResult")
    public void checkServerStatus(Context context) {
        repositoryController.getHLMObjects().subscribe(objectDbs -> {
            for (ObjectDb item : objectDbs) {
                Logger.error(TAG,"Check Server Status Is Active = " + item.IsObjectActive());
                apiController.getServerStatus(context, item, new Callback<Response<ServerStatusModel>>() {
                    @Override
                    public void onSuccess(Response<ServerStatusModel> response) {
                        super.onSuccess(response);
                        if (response.isSuccessful()) {
                            ServerStatusModel statusModel = response.body();
                            if (statusModel != null) {
                                if (item.getIsServerActive() == null || item.getLicenseType() == null) {

                                    item.setIsServerActive(statusModel.getIs_active());
                                    item.setLicenseType(statusModel.getLicense());

                                    updateObject(item);
                                } else {
                                    if (!item.getLicenseType().equals(statusModel.getLicense()) || item.getIsServerActive() != statusModel.getIs_active()) {
                                        if (item.getIsServerActive() != statusModel.getIs_active() &&
                                                statusModel.getIs_active() == Constants.SERVER_STATUS_INACTIVE) {
                                            DialogHelper.createErrorDialog(
                                                    context,
                                                    context.getString(R.string.text_error_dialog_title),
                                                    context.getString(R.string.text_error_inactive_server));
                                        }

                                        item.setIsServerActive(statusModel.getIs_active());
                                        item.setLicenseType(statusModel.getLicense());

                                        updateObject(item);
                                    }
                                }

                            }
                        }

                        updateDevices(context, item);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                    }
                });
            }
        });
    }

    @SuppressLint("CheckResult")
    public void setLocale(Context context) {
        String lang = getLanguageTag(context);

        repositoryController.getHLMObjects().subscribe(objectDbs -> {
            for (ObjectDb objectDb : objectDbs) {
                apiController.setLanguage(context, objectDb, lang, new Callback<Response<LangModel>>() {
                    @Override
                    public void onSuccess(Response<LangModel> object) {
                        super.onSuccess(object);
                        apiController.getObject(context, objectDb, new Callback<Response<UserModel>>() {
                            @Override
                            public void onSuccess(Response<UserModel> response) {
                                super.onSuccess(response);
                                if (response.isSuccessful()) {
                                    UserModel userModel = response.body();
                                    if (userModel != null) {
                                        objectDb.setName(userModel.getAddress());
                                        updateObject(objectDb);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {
                                super.onError(error);
                                Toast.makeText(context, context.getString(R.string.text_error_default_message), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        Toast.makeText(context, context.getString(R.string.text_error_default_message), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public String getLanguageTag(Context context) {
        final Locale locale = Utils.getCurrentLocale(context);
        String lang = "en";

        if (locale != null) {
            lang = locale.getLanguage();
            //FIXME following 'if' must be removed when language tag is fixed on server
            if (lang.equals("uk")) {
                lang = "ua";
            }
        }

        return lang;
    }

    @SuppressLint("CheckResult")
    private void updateObject(ObjectDb objectDb, Runnable onComplete) {
        repositoryController.updateObject(objectDb).subscribe(
                () -> {
                    BasePresenter.this.onObjectChanged(objectDb);

                    if (onComplete != null) {
                        onComplete.run();
                    }
                },
                Throwable::printStackTrace);
    }

    private void updateObject(ObjectDb objectDb) {
        updateObject(objectDb, null);
    }

    //
    //deleteDevices
    //
    private void updateDevices(Context context, ObjectDb objectDb) {
        SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());
        apiController.getSipNumber(context, objectDb, new Callback<Response<UserModel>>() {
            @Override
            public void onSuccess(Response<UserModel> response) {
                super.onSuccess(response);
                if (response.isSuccessful()) {
                    UserModel userModel = response.body();
                    if (userModel != null) {
                        Logger.error(TAG,"Update Divices On Success Is Active = " + objectDb.IsObjectActive());
                        updateObject(context, objectDb, userModel);
                    }
                }

                loadCameras(context, objectDb);
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                loadCameras(context, objectDb);
            }
        });

    }

    //
    //updateObject
    //
    @SuppressLint("CheckResult")
    private void updateObject(Context context, ObjectDb objectDb, UserModel userModel) {
        Logger.error(TAG,"Update Object Is Active = " + objectDb.IsObjectActive());
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

        sipAccountData.setActive(objectDb.IsObjectActive());

        SipServiceCommands.createAccount(context, sipAccountData);

        updateObject(objectDb, () -> {
            Logger.error(TAG, "Object successfully added");

            String currentFCMToken = repositoryController.getToken();

            BasePresenter.this.onObjectChanged(objectDb);
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
    private void loadCameras(Context context, ObjectDb objectDb) {
        apiController.getCamerasShort(context, objectDb, new Callback<Response<ArrayList<CameraShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<CameraShortModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<CameraShortModel> cameraShortModels = response.body();
                    if (cameraShortModels != null) {
                        addAllCameras(context, objectDb, cameraShortModels);
                    }
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
    private void addAllCameras(Context context, ObjectDb objectDb, List<CameraShortModel> cameraShortModels) {
        repositoryController.getDevicesOfObjectByType(objectDb.getObject_id(), TYPE_CAMERA).subscribe(devicesDbs -> {
            List<DevicesDb> camerasToAdd = new ArrayList<>();

            for (CameraShortModel cameraModel : cameraShortModels) {
                DevicesDb foundCamera = findCamera(devicesDbs, cameraModel);
                if (foundCamera == null) {
                    camerasToAdd.add(new DevicesDb(cameraModel, objectDb.getObject_id(), 1));
                } else {
                    DevicesDb modelToUpdate = new DevicesDb(cameraModel, objectDb.getObject_id(), 1);
                    modelToUpdate.setDevice_id(foundCamera.getDevice_id());
                    modelToUpdate.setIs_favorite(foundCamera.getIs_favorite());

                    camerasToAdd.add(modelToUpdate);
                }
            }

            repositoryController.addDevices(camerasToAdd).subscribe(() -> {
                removeAbsentCameras(context, objectDb, cameraShortModels);
            }, throwable -> Logger.error(TAG, "onClicked: Error while saving Device to DB"));
        }, throwable -> Logger.error(TAG, "onClicked: Error while selecting devices from DB"));
    }

    @SuppressLint("CheckResult")
    private void addAllPanels(ObjectDb objectDb, List<PanelShortModel> panelShortModels) {

        repositoryController.getDevicesOfObjectByType(objectDb.getObject_id(), Constants.TYPE_PANEL).subscribe(devicesDbs -> {
            List<DevicesDb> panelsToAdd = new ArrayList<>();

            for (PanelShortModel panelModel : panelShortModels) {
                DevicesDb foundPanel = findPanel(devicesDbs, panelModel);
                if (foundPanel == null) {
                    panelsToAdd.add(new DevicesDb(panelModel, objectDb.getObject_id(), 1));
                } else {
                    DevicesDb modelToUpdate = new DevicesDb(panelModel, objectDb.getObject_id(), 1);
                    modelToUpdate.setDevice_id(foundPanel.getDevice_id());
                    modelToUpdate.setIs_favorite(foundPanel.getIs_favorite());

                    panelsToAdd.add(modelToUpdate);
                }
            }

            repositoryController.addDevices(panelsToAdd).subscribe(
                    () -> removeAbsentPanels(objectDb, panelShortModels),
                    throwable -> Logger.error(TAG, "onClicked: Error while saving Device to DB"));

        }, throwable -> Logger.error(TAG, "onClicked: Error while selecting devices from DB"));
    }

    @SuppressLint("CheckResult")
    private void removeAbsentCameras(Context context, ObjectDb objectDb, List<CameraShortModel> cameraShortModels) {
        repositoryController.getDevicesOfObjectByType(objectDb.getObject_id(), TYPE_CAMERA).subscribe(devicesDbs -> {
            List<DevicesDb> camerasToDelete = getCamerasToDelete(devicesDbs, cameraShortModels);
            if (!camerasToDelete.isEmpty()) {
                repositoryController.removeDevices(camerasToDelete).subscribe(
                        () -> loadPanels(context, objectDb),
                        throwable -> Logger.error(TAG,"can't remove cameras"));
            } else {
                loadPanels(context, objectDb);
            }
        }, throwable -> Logger.error(TAG, "Get all cameras: cameras not found"));
    }

    @SuppressLint("CheckResult")
    private void removeAbsentPanels(ObjectDb objectDb, List<PanelShortModel> panelShortModels) {
        repositoryController.getDevicesOfObjectByType(objectDb.getObject_id(), TYPE_PANEL).subscribe(devicesDbs -> {
            List<DevicesDb> panelsToDelete = getPanelsToDelete(devicesDbs, panelShortModels);
            if (!panelsToDelete.isEmpty()) {
                repositoryController.removeDevices(panelsToDelete).subscribe(
                        () -> onDevicesUpdated(objectDb),
                        throwable -> Logger.error(TAG,"can't remove panels"));
            } else {
                onDevicesUpdated(objectDb);
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
    private void loadPanels(Context context, ObjectDb objectDb) {
        apiController.getPanelsShort(context, objectDb, new Callback<Response<ArrayList<PanelShortModel>>>() {
            @Override
            public void onSuccess(Response<ArrayList<PanelShortModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<PanelShortModel> panelShortModels = response.body();
                    if (panelShortModels != null) {
                        addAllPanels(objectDb, panelShortModels);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Logger.error(TAG, error);
            }
        });
    }

    protected void onDevicesUpdated(ObjectDb objectDb) {}
}
