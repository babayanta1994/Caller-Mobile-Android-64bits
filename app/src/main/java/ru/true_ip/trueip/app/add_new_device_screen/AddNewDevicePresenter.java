package ru.true_ip.trueip.app.add_new_device_screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

import static ru.true_ip.trueip.utils.Constants.TYPE_CAMERA;
import static ru.true_ip.trueip.utils.Constants.TYPE_PANEL;


public class AddNewDevicePresenter extends BasePresenter<AddNewDeviceContract> {

    public static final String TAG = AddNewDevicePresenter.class.getSimpleName();

    public ObservableField<String> objectName = new ObservableField<>("");
    public ObservableField<String> deviceName = new ObservableField<>("");
    public ObservableField<String> sipNumber = new ObservableField<>("");
    public ObservableField<String> fullRTSP = new ObservableField<>("");
    public ObservableField<String> login = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> ipAddress = new ObservableField<>("");
    public ObservableField<String> port = new ObservableField<>("");
    public ObservableField<String> dftm1 = new ObservableField<>("");
    public ObservableField<String> dftm2 = new ObservableField<>("");
    public ObservableField<Integer> imeOptiondftm1 = new ObservableField<>(EditorInfo.IME_ACTION_DONE);
    public ObservableBoolean isAdditionRelayChecked = new ObservableBoolean(false);
    public ObservableBoolean isFullRTSPLink = new ObservableBoolean(false);
    public ObservableBoolean isCameraDevice = new ObservableBoolean(false);
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    public ObservableBoolean isCloud = new ObservableBoolean(false);
    private DevicesDb devicesDb;
    private File compressedImage;
    private Context context;
    private boolean isDataLoaded = false;
    private int objectId;
    private int deviceId;
    private int is_cloud;

    @Override
    public void attachToView(AddNewDeviceContract contract) {
        super.attachToView(contract);
        setListeners();
        if (compressedImage != null) {
            getContract().setObjectPhoto(compressedImage);
        }
        loadObject(objectId);
        if (isEditMode.get() && !isDataLoaded) {
            loadDevice(deviceId);
        }
    }

    public void setContext (Context context) { this.context = context; }

    public void setDeviceTypeBoolean(boolean typeBoolean) {
        isCameraDevice.set(typeBoolean);
    }

    public void handelBundle(Bundle bundle) {
        objectId = bundle.getInt(Constants.BUNDLE_INT_KEY);
        isEditMode.set(bundle.getBoolean(Constants.BUNDLE_IS_EDIT_MODE));
        deviceId = bundle.getInt(Constants.DEVICE_ID_KEY);

        switch ((AddNewDeviceActivity.DeviceType) (bundle)
                .get(Constants.BUNDLE_DEVICE_TYPE)) {
            case PANEL_DEVICE:
                setDeviceTypeBoolean(false);
                break;
            case CAMERA_DEVICE:
                setDeviceTypeBoolean(true);
                break;
        }
    }

    private void moveBackward() {
        AddNewDeviceContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                moveBackward();
                break;
            case R.id.btn_add:
                createOrSaveDevice();
                break;
            case R.id.object_photo_container:
                getContract().pickPhoto();
                break;
            case R.id.btn_delete_cloud:
            case R.id.btn_delete:
                DialogHelper.createDeleteDialog(context, R.string.text_delete_device, (dialog, i) -> {
                    repositoryController.deleteDevice(devicesDb).subscribe(() -> {
                        dialog.dismiss();
                        moveBackward();
                    });
                });
                break;
            case R.id.btn_photo_by_default:
                if(getContract() != null){
                    getContract().setDefaultPhoto();
                }
                if (devicesDb != null) {
                    devicesDb.setImage("");
                }
                compressedImage = null;
                break;
        }
    }

    private void setListeners() {
        isAdditionRelayChecked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getContract().setImeOptions(isAdditionRelayChecked.get() ? EditorInfo.IME_ACTION_NEXT : EditorInfo.IME_ACTION_DONE);
                dftm2.set("");
            }
        });
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                compressedImage = compressImage(activity, imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    public File compressImage(Context baseContext, File originalImage) {
        File compressedImage;
        try {
            compressedImage = new Compressor(baseContext)
                    .setQuality(Constants.IMAGE_COMPRESS_QUALITY)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(originalImage);
            deleteOriginalImage(originalImage);
            return compressedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteOriginalImage(File originalImageFile) {
        if (originalImageFile.exists() && originalImageFile.isFile() && originalImageFile.canWrite()) {
        }
    }

    private void createOrSaveDevice() {
        if (validateData()) {
            return;
        }
        if (devicesDb == null) {
            devicesDb = new DevicesDb();
        }
        devicesDb.setName(deviceName.get());
        devicesDb.setDevice_type(isCameraDevice.get() ? TYPE_CAMERA : TYPE_PANEL);
        devicesDb.setObject_id(objectId);
        if (compressedImage != null) {
            devicesDb.setImage(Utils.convertImageToBase64String(compressedImage));
        }
        devicesDb.setIs_full_rstp(isFullRTSPLink.get());
        devicesDb.setRtsp_link((isFullRTSPLink.get() || isCameraDevice.get()) ?
                fullRTSP.get() : Utils.createRTSPLink(login.get(), password.get(), ipAddress.get(), port.get()));
        devicesDb.setSip_number(sipNumber.get());
        devicesDb.setLogin(login.get());
        devicesDb.setPassword(password.get());
        devicesDb.setIp_address(ipAddress.get());
        devicesDb.setPort(port.get());
        devicesDb.setIs_additional_relay(isAdditionRelayChecked.get());
        devicesDb.setDftm1(dftm1.get());
        devicesDb.setDftm2(dftm2.get());
        repositoryController.addDevice(devicesDb).subscribe(
            this::moveBackward,
            throwable -> { throwable.printStackTrace(); }
        );
    }

    private boolean validateData() {
        if (isCameraDevice.get() && (deviceName.get().isEmpty() || fullRTSP.get().isEmpty())) {
            getContract().showDialog();
            return true;
        }
        if (!isCameraDevice.get() && (deviceName.get().isEmpty()
                || sipNumber.get().isEmpty()
                || (!isFullRTSPLink.get() && (password.get().isEmpty()
                || login.get().isEmpty()
                || ipAddress.get().isEmpty()
                || port.get().isEmpty()))
                || (isFullRTSPLink.get() && fullRTSP.get().isEmpty())
                || dftm1.get().isEmpty()
                || (isAdditionRelayChecked.get() && dftm2.get().isEmpty()))) {
            getContract().showDialog();
            return true;
        }
        return false;
    }


    public void loadDevice(int deviceId) {
        repositoryController.getDeviceByDeviceId(deviceId).subscribe(devicesDb -> {
            this.devicesDb = devicesDb;
            showDeviceData(devicesDb);
            isDataLoaded = true;
        });
    }

    private void loadObject(int objectId) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            objectName.set(objectDb.getName());
            is_cloud = objectDb.is_cloud;
            isCloud.set(is_cloud != 0);
        });
    }

    private void showDeviceData(DevicesDb devicesDb) {
        if (devicesDb.getName() != null) {
            deviceName.set(devicesDb.getName());
        }
        if (devicesDb.getImage() != null && !devicesDb.getImage().isEmpty()) {
            getContract().setObjectPhoto(Utils.convertBase64StringToImage(devicesDb.getImage()));
        }
        if (devicesDb.is_full_rstp() != null) {
            isFullRTSPLink.set(devicesDb.is_full_rstp());
        }
        if (devicesDb.getIs_additional_relay() != null) {
            isAdditionRelayChecked.set(devicesDb.getIs_additional_relay());
        }
        if (devicesDb.getSip_number() != null) {
            sipNumber.set(devicesDb.getSip_number());
        }
        if (devicesDb.getLogin() != null) {
            login.set(devicesDb.getLogin());
        }
        if (devicesDb.getPassword() != null) {
            password.set(devicesDb.getPassword());
        }
        if (devicesDb.getIp_address() != null) {
            ipAddress.set(devicesDb.getIp_address());
        }
        if (devicesDb.getPort() != null) {
            port.set(devicesDb.getPort());
        }
        if (devicesDb.getDftm1() != null) {
            dftm1.set(devicesDb.getDftm1());
        }
        if (devicesDb.getDftm2() != null) {
            dftm2.set(devicesDb.getDftm2());
        }
        if (devicesDb.getRtsp_link() != null && !devicesDb.getRtsp_link().isEmpty()) {
            fullRTSP.set(devicesDb.getRtsp_link());
        }
    }

    public void changeChecked(View view) {
        isAdditionRelayChecked.set(!isAdditionRelayChecked.get());
    }
}
