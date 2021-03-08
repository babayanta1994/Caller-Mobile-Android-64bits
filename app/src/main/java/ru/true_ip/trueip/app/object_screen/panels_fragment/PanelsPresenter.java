package ru.true_ip.trueip.app.object_screen.panels_fragment;

import android.annotation.SuppressLint;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.add_new_device_screen.AddNewDeviceActivity;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.app.object_screen.abstract_fragments.AbstractDevicesPresenter;
import ru.true_ip.trueip.app.object_screen.panels_fragment.adapters.PanelsAdapter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

import static ru.true_ip.trueip.utils.Constants.BUNDLE_IS_EDIT_MODE;

/**
 * Created by user on 19-Sep-17.
 */

public class PanelsPresenter extends AbstractDevicesPresenter<PanelsContract> {

    private static final String TAG = PanelsPresenter.class.getSimpleName();
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    public ObservableBoolean isHLMObject = new ObservableBoolean(false);
    public ObservableField<PanelsAdapter> panelsAdapter = new ObservableField<>();
    private int objectId;

    private List<DevicesDb> devicesList = new ArrayList<>();
    private ArrayList<Integer> cellColors = null;
    private ScheduledFuture<?> scheduledFuture = null;

    @Override
    public void attachToView(PanelsContract contract) {
        super.attachToView(contract);
        getDevicesList();
    }

    public void onResume() {
        //Logger.error(TAG, "Start Coloring timer");
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            String address;
            cellColors = new ArrayList<>();
            int color;
            for (DevicesDb device : devicesList) {
                try {
                    address = DevicesDb.getIpAddress(device);
                    InetAddress inetAddress = InetAddress.getByName(address);
                    color = R.color.color_indicator_inactive;
                    if (address.length() != 0 && inetAddress.isReachable(200)) {
                        Logger.error(TAG, "Greening camera address ->" + address);
                        color = R.color.color_indicator_active;
                    }
                    cellColors.add(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Logger.error(TAG, "Coloring complete. Cell colors size = " + cellColors.size());
            if (cellColors.size() > 0) {
                panelsAdapter.get().setCellColors(cellColors);
                //cameraAdapter.get().setItems(new ArrayList<>());
                panelsAdapter.notifyChange();
            }
        }, 1, 30, TimeUnit.SECONDS);
    }

    public void onPause() {
        if (scheduledFuture != null) {
            //Logger.error(TAG, "Stop Coloring timer");
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }


    public void onClickChange(View v) {
        isEditMode.set(!isEditMode.get());
        PanelsAdapter adapter = panelsAdapter.get();
        if (adapter != null) {
            adapter.setEditMode(isEditMode.get());
        }
    }

    private boolean isObjectActive = false;

    @SuppressLint("CheckResult")
    public void setObjectId(int objectId) {
        this.objectId = objectId;
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            isObjectActive = objectDb.IsObjectActive();
            isHLMObject.set(objectDb.getIs_cloud() != 0);
        });
    }

    public void onClickAdd(View v) {
        mustLoadDevices = true;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_DEVICE_TYPE, AddNewDeviceActivity.DeviceType.PANEL_DEVICE);
        bundle.putInt(Constants.BUNDLE_INT_KEY, objectId);
        getContract().getRouter().moveTo(BaseRouter.Destination.ADD_NEW_DEVICE_SCREEN, bundle);
    }

    public void createPhotoRv() {
        PanelsAdapter adapter = new PanelsAdapter(R.layout.item_device, new ArrayList<>());
        adapter.addOnItemClickListener((position, item) -> {
            if (!isObjectActive) {
                Toast.makeText(context,context.getString(R.string.text_no_call_made), Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_DEVICE_TYPE, AddNewDeviceActivity.DeviceType.PANEL_DEVICE);
            bundle.putBoolean(BUNDLE_IS_EDIT_MODE, isEditMode.get());
            bundle.putInt(Constants.DEVICE_ID_KEY, item.getDevice_id());
            bundle.putInt(Constants.BUNDLE_INT_KEY, objectId);
            if (!isEditMode.get()) {
                //getContract().getRouter().moveTo(BaseRouter.Destination.DEVICE_SCREEN, bundle);
                if (item.getIs_callable() == null || item.getIs_callable()) {
                    DeviceActivity.start(context, bundle);
                } else {
                    DialogHelper.createExplanationDialog(context,
                            R.string.text_error_panel_not_callable);
                }
            } else {
                getContract().getRouter().moveTo(BaseRouter.Destination.ADD_NEW_DEVICE_SCREEN, bundle);
            }
            mustLoadDevices = true;
        });

        panelsAdapter.set(adapter);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDevicesList() {
        if (mustLoadDevices || isEditMode.get()) {
            mustLoadDevices = false;
            repositoryController.getAllDevices(Constants.TYPE_PANEL, objectId).subscribe(devicesDbs -> {
                devicesList = devicesDbs;
                panelsAdapter.get().setItems(devicesList);
                panelsAdapter.get().init();
            }, throwable -> {
                //TODO show some message instead of recycler
            });
        }
    }
}
