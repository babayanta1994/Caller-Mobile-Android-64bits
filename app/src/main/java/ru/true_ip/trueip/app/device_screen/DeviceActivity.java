package ru.true_ip.trueip.app.device_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;

import java.util.Objects;

import ru.true_ip.trueip.BR;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityDeviceBinding;
import ru.true_ip.trueip.service.data.BroadcastEventEmitter;
import ru.true_ip.trueip.service.portsip.PortSipEventHandler;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.VLCPlayer;

import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_DISCONNECTED;

/**
 *
 * Created by Andrey Filimonov on 03-Oct-17.
 */
public class DeviceActivity extends BaseActivity<DeviceContract, DevicePresenter, ActivityDeviceBinding> implements
        DeviceContract,
        IVLCVout.Callback,
        VLCPlayer.PlayerEventListener,
        SensorEventListener {

    private final static String TAG = DeviceActivity.class.getSimpleName();
    private final static String STATE_KEY = "STATE_KEY";
    private final static int PERMISSIONS_REQUEST_RECORD_AUDIO = 1778;
    private BroadcastReceiver broadcastReceiver = null;

    private static final float ProximityThresholdValue = 4.0f;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    SensorManager manager;
    Sensor proximity;

    private View.OnLayoutChangeListener onLayoutChangeListener;

    public static void start(Context context, Bundle bundle) {
        Logger.error(TAG,"Start Called");
        Intent intent = new Intent(context, DeviceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }
    //
    //onCreate
    //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Logger.error(TAG,"on Create Called");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

//        setShowWhenLocked(true);
//        setTurnScreenOn(true);
        super.onCreate(savedInstanceState);

        initToolbar();

        if (savedInstanceState != null) {
            DeviceScreenState state = savedInstanceState.getParcelable(STATE_KEY);
            presenter.setDeviceScreenState(state);
        }

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        _onResume();
    }

    void initToolbar() {
        if (binding != null) {
            setSupportActionBar(binding.toolbar);
            try {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.binding = initBinding();
        this.binding.setVariable(BR.presenter, this.presenter);
        setContentView(binding.getRoot());

        initToolbar();

        presenter.initCameraStream(binding.cameraView, this, this, onLayoutChangeListener);
        presenter.revalidateCameraStream();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.revalidateCameraStream();
    }

    //
    // onResume
    //
    //@Override
    public void _onResume() {
        //Logger.error(TAG,"On Resume Called");
        super.onResume();
        presenter.setContext(this);
        presenter.setExtras(getIntent().getExtras());
        //Proximity listener
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = null;
        if (manager != null) {
            proximity = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            manager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //play video
        onLayoutChangeListener = (View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) -> {
            int width = binding.cameraView.getWidth();
            int height = binding.cameraView.getHeight();
            presenter.setViewportSize(width, height);
        };

        presenter.initCameraStream(binding.cameraView, this, this, onLayoutChangeListener);

        //Register receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) return;
                BroadcastEventEmitter.BroadcastAction action = BroadcastEventEmitter.BroadcastAction.valueOf(intent.getAction());
                int callState = intent.getIntExtra(BroadcastEventEmitter.BroadcastParameters.CALL_STATE, -1);
                String accountId = intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID);
                switch (action) {
                    case DO_FINISH:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.text_device_is_not_available)
                                .setPositiveButton(R.string.text_ok, (dialog, id) -> finish());

                        builder.create().show();
                        break;
                    case REJECT_CALL:
                        long sessionID = intent.getLongExtra(BroadcastEventEmitter.BroadcastParameters.SESSION_ID, -1L);
                        SipServiceCommands.rejectIncomingCall(context, accountId, sessionID, 486);
                        break;
                    case CALL_STATE:
                        presenter.onCallState(callState);
                        if (callState == CALL_STATE_DISCONNECTED) {
                            Logger.error(TAG, "isDialNew = " + presenter.isDialNew.get() +
                                    " isChangingDevice = " + presenter.isChangingDevice.get() +
                                    " isCallFailed = " + presenter.isCallFailed.get() +
                                    " isCallRejected = " + presenter.isCallRejected.get());
                            if (presenter.isDialNew.get() || presenter.isChangingDevice.get()) {
                                Logger.error(TAG, "Dialing New Or Change Device");
                                presenter.isDialNew.set(false);
                                presenter.isChangingDevice.set(false);
                                return;
                            } else if (presenter.isCallFailed.get() || presenter.isCallRejected.get()) {
                                String message;
                                if (presenter.isConciergeCall.get()) {
                                    message = getString(R.string.text_call_concierge_failed);
                                } else {
                                    if (presenter.isCallRejected.get())
                                        message = getString(R.string.text_call_failed_panel_rejected);
                                    else
                                        message = String.format(getString(R.string.text_call_failed_panel), presenter.getDeviceName());
                                }
                                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                                        .setCancelable(false)
                                        .setMessage(message)
                                        .setNegativeButton(R.string.text_ok, (dlg, i) -> {
                                            dlg.dismiss();
                                            finish();
                                        })
                                        .create();
                                dialog.show();
                            } else
                                finish();
                        }
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastEventEmitter.getAction(BroadcastEventEmitter.BroadcastAction.CALL_STATE));
        intentFilter.addAction(BroadcastEventEmitter.getAction(BroadcastEventEmitter.BroadcastAction.REJECT_CALL));
        intentFilter.addAction(BroadcastEventEmitter.getAction(BroadcastEventEmitter.BroadcastAction.DO_FINISH));
        this.registerReceiver(broadcastReceiver, intentFilter);
        checkPermission();
    }
    //
    // onDestroy
    //
    @Override
    public void onDestroy() {
        //Logger.error(TAG,"On Pause");
        if (broadcastReceiver != null) {
            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        presenter.stopDisplayCameraStream();
        binding.cameraView.removeOnLayoutChangeListener(onLayoutChangeListener);
        if (proximity != null) {
            manager.unregisterListener(this, proximity);
        }

        // connecting actions in case app was swiped from recent apps
        // decline call if is invited
        presenter.doDeclineCall();
        // hang up calls and close all active lines
        presenter.doHangUpAllCalls();
        // prevent further auto rejecting in portsip handler
        //PortSipEventHandler.setRejectCall(false);
        //PortSipEventHandler.longVal = -1;

        super.onDestroy();
    }
    //
    //initBindings
    //
    @Override
    public ActivityDeviceBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_device, null, false);
    }
    //
    // onBackPressed
    //
    @Override
    public void onBackPressed() {
        presenter.cancelCall();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }

    @Override
    public DeviceContract getContract() {
        return this;
    }

    @Override
    public DevicePresenter createPresenter() {
        return new DevicePresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new DeviceRouter(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_KEY, presenter.getDeviceScreenState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //DeviceScreenState state = savedInstanceState.getParcelable(STATE_KEY);
        //presenter.setDeviceScreenState(state);
    }
    //
    // checkPermission
    //
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            presenter.setRecordAudioGranted();
            presenter.process();
            presenter.startVideo();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.setRecordAudioGranted();
                presenter.process();
                presenter.startVideo();
            } else {
                Toast.makeText(this, getString(R.string.can_not_handle_calls), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {

    }

    @SuppressLint({"WakelockTimeout", "InlinedApi"})
    private void turnOffScreen() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, this.getClass().getName());
        wakeLock.acquire();
    }

    @SuppressLint("WakelockTimeout")
    @SuppressWarnings("deprecation")
    private void turnOnScreen() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, this.getClass().getName());
        wakeLock.acquire();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] < ProximityThresholdValue) {
            turnOffScreen();
        } else {
            turnOnScreen();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPlayerEvent(int event) {
        if (event == VLCPlayer.Vout) {
            if (presenter != null) {
                presenter.showVideoframe.set(true);
                presenter.setIsScreenShotEnabled(true);
            }
            return;
        }
        if (event == VLCPlayer.EncounteredError) {
            if (presenter != null) {
                presenter.displayVideoError();
            }
        }
    }

    @BindingAdapter("android:layout_centerInParent")
    public static void setCenterInParent(View view, boolean alignCenterInParent) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                view.getLayoutParams()
        );

        if(alignCenterInParent) {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_above")
    public static void setLayoutAbove(View view, int id) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

        layoutParams.addRule(RelativeLayout.ABOVE, id);
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void setDeviceName(CharSequence text) {
        binding.title.setText(text);
    }
}
