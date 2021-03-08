package ru.true_ip.trueip.app.main_screen.favorites_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.app.main_screen.favorites_fragment.adapters.FavoritesRvAdapter;
import ru.true_ip.trueip.app.object_screen.abstract_fragments.AbstractDevicesPresenter;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by user on 11-Sep-17.
 */

public class FavoritesPresenter extends AbstractDevicesPresenter<FavoritesContract> {

    private static final String TAG = FavoritesPresenter.class.getSimpleName();
    public ObservableField<FavoritesRvAdapter> favoritesAdapter = new ObservableField<>();
    private List<DevicesDb> devicesList = new ArrayList<>();
    private ArrayList<Integer> cellColors = null;
    private ScheduledFuture<?> scheduledFuture = null;


    @Override
    public void attachToView(FavoritesContract contract) {
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
                    Logger.error(TAG,"Checking address = " + address);
                    color = R.color.color_indicator_inactive;
                    if ( address.length() != 0 && inetAddress.isReachable(200)) {
                        Logger.error(TAG, "Greening camera address ->" + address);
                        color = R.color.color_indicator_active;
                    }
                    cellColors.add(color);
                } catch (Exception e) {
                    e.printStackTrace();
                    cellColors.add(R.color.color_indicator_inactive);
                }
            }
            //Logger.error(TAG, "Coloring complete. Cell colors size = " + cellColors.size());
            if ( cellColors.size() > 0) {
                favoritesAdapter.get().setCellColors(cellColors);
                favoritesAdapter.notifyChange();
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
    @Override
    public void getDevicesList() {
        repositoryController.getFavoriteDevices().subscribe(devicesDbs -> {
            devicesList = devicesDbs;
            favoritesAdapter.get().setItems(devicesList);
            favoritesAdapter.get().init();
        }, throwable -> throwable.printStackTrace());
    }

    @SuppressLint("CheckResult")
    public void setFavoritesAdapter() {
        FavoritesRvAdapter adapter = new FavoritesRvAdapter(R.layout.item_device, new ArrayList<>());
        adapter.addOnItemClickListener((position, item) -> {
            if (item.getDevice_type() == Constants.TYPE_CAMERA) {
                openDeviceScreen(item);
            } else {
                repositoryController.getObject(item.object_id).subscribe(objectDb -> {
                    if (objectDb.IsObjectActive()) {
                        if (item.getDevice_type() == Constants.TYPE_PANEL && item.getIs_callable() != null && !item.getIs_callable()) {
                            showOptionsMessage(position, item);
                        } else {
                            openDeviceScreen(item);
                        }
                    } else {
                        Toast.makeText(context,context.getString(R.string.text_no_call_made), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        favoritesAdapter.set(adapter);
    }

    private void openDeviceScreen (DevicesDb devicesDb) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DEVICE_ID_KEY, devicesDb.getDevice_id());
        DeviceActivity.start(context, bundle);
    }

    @SuppressLint("CheckResult")
    private void removePanelFromFavorite(int position, DevicesDb devicesDb) {
        devicesDb.setIs_favorite(0); // 0 means not favorite
        repositoryController.updateDevice(devicesDb).subscribe(
                () -> removePanelFromList(position),
                throwable -> Logger.error(TAG, "Can't remove device from favorite list: " + throwable.getMessage()));
    }

    private void removePanelFromList(int position) {
        FavoritesRvAdapter adapter = favoritesAdapter.get();
        if (adapter != null) {
            adapter.removeItem(position);
        }
    }

    private void showOptionsMessage(int position, DevicesDb devicesDb) {
        DialogHelper.createOptionsDialog(context,
                R.string.text_error_panel_not_callable,
                new DialogHelper.DialogOption(context.getString(R.string.text_ok)) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },
                new DialogHelper.DialogOption(context.getString(R.string.remove_from_favorite)) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removePanelFromFavorite(position, devicesDb);
                    }
                });
    }
}
