package ru.true_ip.trueip.app.object_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.app.object_screen.abstract_fragments.AbstractDevicesFragment;
import ru.true_ip.trueip.app.object_screen.adapters.ViewPagerAdapter;
import ru.true_ip.trueip.app.object_screen.cameras_fragment.CamerasFragment;
import ru.true_ip.trueip.app.object_screen.panels_fragment.PanelsFragment;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.CameraModel;
import ru.true_ip.trueip.service.data.BroadcastEventEmitter;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

/**
 * Created by user on 19-Sep-17.
 */

public class ObjectPresenter extends BasePresenter<ObjectContract> {
    private ObjectDb objectDb;
    private Context context;
    public ObservableBoolean isConciergeAvailable = new ObservableBoolean(false);

    public int objectId;

    public ObservableField<ViewPagerAdapter> viewPagerAdapter = new ObservableField<>();

    @SuppressLint("CheckResult")
    public void setTitle(int objectId) {
        repositoryController.getObject(objectId)
                .subscribe(objectDb -> {
                    this.objectDb = objectDb;
                    getContract().setTitle(objectDb.getName());
                    isConciergeAvailable.set(objectDb.getHas_concierge());
                });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.BUNDLE_INT_KEY);
    }

    @SuppressLint("CheckResult")
    public void createViewPager(Bundle bundle) {

        //bundle.putBoolean(Constants.LOAD_DEVICES, false);

        ViewPagerAdapter adapter = new ViewPagerAdapter(((FragmentActivity) context).getSupportFragmentManager());
        adapter.addFrag(PanelsFragment.getInstance(bundle), context.getString(R.string.panels));
        adapter.addFrag(CamerasFragment.getInstance(bundle), context.getString(R.string.cameras));
        viewPagerAdapter.set(adapter);

        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (objectDb.getIs_cloud() != 0 && Utils.checkNetworkAvaibility(context)) {
                checkResponseStatus(objectDb, new Callback<Response<List<CameraModel>>>() {
                    @Override
                    public void onSuccess(Response<List<CameraModel>> response) {
                        super.onSuccess(response);
                        if (!response.isSuccessful()) {
                            int status = response.code();

                            DialogHelper.createErrorDialog(context,
                                    context.getString(R.string.text_error_dialog_title),
                                    context.getString(R.string.text_api_token_expired));

                            repositoryController.removeDevicesByObjectId(objectId);

                            if (status == 401 || status == 403) {
                                SipServiceCommands.removeAccount(context, objectDb.getIdUri());
                            }
                        } else {
                            updateDevices();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        updateDevices();
                    }
                });
            } else {
                updateDevices();
            }
        });
    }

    private void checkResponseStatus(ObjectDb objectDb, Callback<Response<List<CameraModel>>> callback) {
        apiController.getCameras(context, objectDb, callback);
    }

    private void updateDevices() {
        for (int i = 0; i < viewPagerAdapter.get().getCount(); ++i) {
            Fragment fragment = viewPagerAdapter.get().getItem(i);
            if (fragment instanceof AbstractDevicesFragment) {
                AbstractDevicesFragment devicesFragment = (AbstractDevicesFragment) fragment;
                Bundle args = devicesFragment.getArguments();
                if (args != null) {
                    //args.putBoolean(Constants.LOAD_DEVICES, true);
                    //devicesFragment.setArguments(args);
                    if (devicesFragment.isFragmentReady()) {
                        devicesFragment.setMustLoadDevices(true);
                        devicesFragment.updateDevices();
                    }
                }
            }
        }
    }

    public void onConciergeClick(View v) {
        int objectId = objectDb.getObject_id();
        if (!objectDb.IsObjectActive()) {
            Toast.makeText(context, context.getString(R.string.text_no_call_made), Toast.LENGTH_SHORT).show();
            return;
        }
        //start activity
        Bundle params = new Bundle();
        String accountID = objectDb.getSip_number() + "@" + objectDb.getIp_address();
        params.putString(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID, accountID);
        params.putBoolean(Constants.CALL_CONCIERGE, true);
        params.putInt(Constants.OBJECT_ID, objectDb.getObject_id());
        params.putString(Constants.CONCIERGE_SIP, objectDb.getConcierge_number());
        DeviceActivity.start(context, params);
    }

    @Override
    protected void onDevicesUpdated(ObjectDb objectDb) {
        super.onDevicesUpdated(objectDb);
        updateDevices();
    }
}
