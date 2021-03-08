package ru.true_ip.trueip.app.object_screen.cameras_fragment;

import android.annotation.SuppressLint;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.add_new_device_screen.AddNewDeviceActivity;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.app.object_screen.abstract_fragments.AbstractDevicesPresenter;
import ru.true_ip.trueip.app.object_screen.cameras_fragment.adapters.CamerasAdapter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.utils.Constants;

import static ru.true_ip.trueip.utils.Constants.BUNDLE_IS_EDIT_MODE;


/**
 * Created by user on 19-Sep-17.
 */

public class CamerasPresenter extends AbstractDevicesPresenter<CamerasContract> {

    private static final String TAG = CamerasPresenter.class.getSimpleName();
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    public ObservableBoolean isHLMObject = new ObservableBoolean(false);
    public ObservableField<CamerasAdapter> cameraAdapter = new ObservableField<>();
    private List<DevicesDb> devicesList = new ArrayList<>();
    private int objectId;
    private ArrayList<Integer> cellColors = null;
    private ScheduledFuture<?> scheduledFuture = null;

    @Override
    public void attachToView(CamerasContract contract) {
        //Logger.error(TAG, "On Attach to view");
        super.attachToView(contract);
        getDevicesList();
    }

    public void onResume() {
        //Logger.error(TAG, "Start Coloring timer");
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            String address;
            cellColors = new ArrayList<>();
            int i = 0;
            int color;
            for (DevicesDb device : devicesList) {
                try {
                    address = DevicesDb.getIpAddress(device);
                    InetAddress inetAddress = InetAddress.getByName(address);
                    Logger.error(TAG,"Iterating i = " + i +  " address = " + address);
                    color = R.color.color_indicator_inactive;
                    if ( address.length() != 0 && inetAddress.isReachable(200)) {
                        //Logger.error(TAG, "Greening camera address ->" + address);
                        color = R.color.color_indicator_active;
                    }
                    cellColors.add(color);
                } catch (Exception e) {
                    e.printStackTrace();
                    cellColors.add(R.color.color_indicator_inactive);
                }
                i++;
            }
            //Logger.error(TAG, "Coloring complete. Cell colors size = " + cellColors.size());
            if (cellColors.size() > 0) {
                Objects.requireNonNull(cameraAdapter.get()).setCellColors(cellColors);
                //cameraAdapter.get().setItems(new ArrayList<>());
                cameraAdapter.notifyChange();
            }
        }, 1,  30, TimeUnit.SECONDS);
    }

    public void onPause() {
        if (scheduledFuture != null) {
            //Logger.error(TAG, "Stop Coloring timer");
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    @SuppressLint("CheckResult")
    public void setObjectId(int objectId) {
        this.objectId = objectId;
        repositoryController.getObject(objectId).subscribe(objectDb -> isHLMObject.set(objectDb.getIs_cloud() != 0));
    }

    @SuppressWarnings("unused")
    public void onClickChange(View v) {
        isEditMode.set(!isEditMode.get());
        CamerasAdapter adapter = cameraAdapter.get();
        if (adapter != null) {
            adapter.setEditMode(isEditMode.get());
        }
    }

    @SuppressWarnings("unused")
    public void onClickAdd(View v) {
        mustLoadDevices = true;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_DEVICE_TYPE, AddNewDeviceActivity.DeviceType.CAMERA_DEVICE);
        bundle.putInt(Constants.BUNDLE_INT_KEY, objectId);
        getContract().getRouter().moveTo(BaseRouter.Destination.ADD_NEW_DEVICE_SCREEN, bundle);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDevicesList() {
        if (mustLoadDevices || isEditMode.get()) {
            mustLoadDevices = false;
            repositoryController.getAllDevices(Constants.TYPE_CAMERA, objectId).subscribe(devicesDbs -> {
                devicesList = devicesDbs;
                Objects.requireNonNull(cameraAdapter.get()).setItems(devicesList);
                Objects.requireNonNull(cameraAdapter.get()).init();
            }, throwable -> {
                // TODO show some message instead of recycler
            });
        }
    }

    public void createPhotoRv() {
        CamerasAdapter adapter = new CamerasAdapter(R.layout.item_device, new ArrayList<>());
        adapter.addOnItemClickListener((position, item) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_DEVICE_TYPE, AddNewDeviceActivity.DeviceType.CAMERA_DEVICE);
            bundle.putBoolean(BUNDLE_IS_EDIT_MODE, isEditMode.get());
            bundle.putInt(Constants.DEVICE_ID_KEY, item.getDevice_id());
            bundle.putInt(Constants.BUNDLE_INT_KEY, objectId);
            if (!isEditMode.get()) {
                //getContract().getRouter().moveTo(BaseRouter.Destination.DEVICE_SCREEN, bundle);
                DeviceActivity.start(context, bundle);
            } else {
                getContract().getRouter().moveTo(BaseRouter.Destination.ADD_NEW_DEVICE_SCREEN, bundle);
            }
            mustLoadDevices = true;
        });
        cameraAdapter.set(adapter);
    }
}
