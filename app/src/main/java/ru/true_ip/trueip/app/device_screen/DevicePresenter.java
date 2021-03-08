package ru.true_ip.trueip.app.device_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.app.device_screen.adapters.DeviceListAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.db.entity.SettingsDb;
import ru.true_ip.trueip.service.data.BroadcastEventEmitter;
import ru.true_ip.trueip.service.portsip.PortSipEventHandler;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.SnapshotsHelper;
import ru.true_ip.trueip.utils.VLCPlayer;

import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_CONNECTED;
import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_CONNECTING;
import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_DISCONNECTED;

/**
 *
 * Created by Andrey Filimonov on 03-Oct-17.
 */

public class DevicePresenter extends BasePresenter<DeviceContract> {

    private static final String TAG = DevicePresenter.class.getSimpleName();

    private static final String FILE_NAME_PREFIX = "Snapshot";
    private static final String FILE_NAME_SEPARATOR = "_";
    private static final String FILE_EXTENSION = ".jpg";

    private DevicesDb currentDevice = null;
    private ObjectDb object = null;
    private SettingsDb settings = null;
    public ObservableField<DeviceListAdapter> deviceListAdapter = new ObservableField<>();
    public ObservableField<String> deviceName = new ObservableField<>();
    public ObservableField<String> preparingVideoText = new ObservableField<>();
    public ObservableBoolean isFavorite = new ObservableBoolean(false);
    public ObservableBoolean isDeviceListShown = new ObservableBoolean(false);
    public ObservableBoolean isPanelShown = new ObservableBoolean(true);
    public ObservableBoolean isFullScreen = new ObservableBoolean(false);
    public ObservableBoolean isIncomingCallState = new ObservableBoolean(true);
    public ObservableBoolean isMuted = new ObservableBoolean(false);
    public ObservableBoolean isLoudSpeakerOn = new ObservableBoolean(false);
    public ObservableBoolean isCallInProgress = new ObservableBoolean(false);
    public ObservableBoolean isConversationInProgress = new ObservableBoolean(false);
    public ObservableBoolean isDTMFOneConfigured = new ObservableBoolean(false);
    public ObservableBoolean isDTMFTwoConfigured = new ObservableBoolean(false);
    public ObservableBoolean isCamera = new ObservableBoolean(false);
    public ObservableBoolean isShowButton = new ObservableBoolean(true);
    public ObservableBoolean isDialNew = new ObservableBoolean(false);
    public ObservableBoolean isConciergeCall = new ObservableBoolean(false);
    public ObservableBoolean isScreenShotEnabled = new ObservableBoolean(false);
    public ObservableBoolean isCallFailed = new ObservableBoolean(false);
    public ObservableBoolean showVideoframe = new ObservableBoolean(false);
    public ObservableBoolean isChangingDevice = new ObservableBoolean(false);
    public ObservableBoolean isCallRejected = new ObservableBoolean(false);
    public ObservableBoolean showBackToCallButton = new ObservableBoolean(false);

    private VLCPlayer player;
    private VLCPlayer.PlayerEventListener playerEventListener;
    private SurfaceView playerSurfaceView;
    private IVLCVout.Callback playerCallBack;
    private View.OnLayoutChangeListener onLayoutChangeListener;

    private Context context;
    private int currentCallId = -1;
    private List<DevicesDb> devices;
    private List<ObjectDb> allObjects;
    private ArrayList<DevicesDb> listDevices = new ArrayList<>();
    private int deviceId = -1;
    private String accountId;
    private String displayName;
    private String remoteUri;
    private boolean isRecordAudioGranted = false;
    private boolean isCallInvited;
    private int objectId = -1;
    private String concierge_sip = "";
    private ObservableBoolean isScreenSateRestored = new ObservableBoolean(false);
    private String current_rtsp_link = "";
    private Handler handler = new Handler();
    private Toast toast;
    private boolean didOnHook = false;

    //portsip
    private long sessionId;
    private String callerDisplayName;
    private String caller;
    private String calleeDisplayName;
    private String callee;
    private String audioCodecs;
    private String videoCodecs;
    private boolean existsAudio;
    private boolean existsVideo;
    private String sipMessage;

    DevicePresenter() {
    }

    //
    //setExtras
    //
    void setExtras(Bundle extras) {
        print( "set Extrass called");
        if (!isScreenSateRestored.get()) {
            isIncomingCallState.set(false);
        }
        preparingVideoText.set(context.getResources().getString(R.string.text_prepare_video));

        if (extras != null && !isScreenSateRestored.get()) {
            isConciergeCall.set(extras.getBoolean(Constants.CALL_CONCIERGE, false));
            concierge_sip = extras.getString(Constants.CONCIERGE_SIP, "");
            objectId = extras.getInt(Constants.OBJECT_ID, -1);
            deviceId = extras.getInt(Constants.DEVICE_ID_KEY, -1);
            accountId = extras.getString(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID, null);
            currentCallId = extras.getInt(BroadcastEventEmitter.BroadcastParameters.CALL_ID, -1);
            displayName = extras.getString(BroadcastEventEmitter.BroadcastParameters.DISPLAY_NAME, "");
            remoteUri = extras.getString(BroadcastEventEmitter.BroadcastParameters.REMOTE_URI, "");
            //
            //portsip
            //
            sessionId = extras.getLong(BroadcastEventEmitter.BroadcastParameters.SESSION_ID,0);
            callerDisplayName = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLER_DISPLAY_NAME);
            caller = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLER);
            calleeDisplayName = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLEE_DISPLAY_NAME);
            callee = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLEE);
            print( "Caller " + caller + " caller name " + callerDisplayName);
            print( "Callee " + callee + " callee name " + calleeDisplayName);
            audioCodecs = extras.getString(BroadcastEventEmitter.BroadcastParameters.AUDIO_CODECS);
            videoCodecs = extras.getString(BroadcastEventEmitter.BroadcastParameters.VIDEO_CODECS);
            existsAudio = extras.getBoolean(BroadcastEventEmitter.BroadcastParameters.EXISTING_AUDIO);
            existsVideo = extras.getBoolean(BroadcastEventEmitter.BroadcastParameters.EXISTING_VIDEO);
            sipMessage = extras.getString(BroadcastEventEmitter.BroadcastParameters.SIP_MESSAGE);
        }
    }
    //
    //process
    //
    @SuppressLint("CheckResult")
    void process() {
        //Make Concierge call
        if (isConciergeCall.get() && !isScreenSateRestored.get()) {
            showVideoframe.set(false);
            isIncomingCallState.set(false);
            isShowButton.set(false);
            deviceName.set(context.getString(R.string.text_concierge));
            loadObject(objectId);
            //print( "===> Make call from process and screenRestored = " + isScreenSateRestored.get());
            doMakeCall();
            return;
        }
        //Handle Incoming call
        if (deviceId == -1 && accountId != null && !isScreenSateRestored.get()) {
            //print( "No currentDevice ID means Incoming call");
            isIncomingCallState.set(true);

            handler.post(() -> {
                if (currentDevice == null) {
                    deviceName.set(Uri.parse(caller).getSchemeSpecificPart().split("@")[0]);
                    setDeviceName();
                }
            });

            deviceName.set(displayName);
            setDeviceName();
            repositoryController.getAllObjects().subscribe(ObjectDb -> this.allObjects = ObjectDb);
            String accId;
            for (ObjectDb objectDb : allObjects) {
                accId = "sip:" + objectDb.getSip_number() + "@" + objectDb.getIp_address();
                print( "Checking id = " + accId + " against " + accountId);
                if (accountId.equalsIgnoreCase(accId)) {
                    object = objectDb;
                    print( "Found Object Id = " + object.getObject_id());
                    loadObject(object.getObject_id());
                    break;
                }
            }
            if (object == null) {
                print( "Object not found");
                return;
            }
            repositoryController.getAllDevices(Constants.TYPE_PANEL, object.getObject_id()).subscribe(DevicesDb -> this.devices = DevicesDb);
            if ( caller != null) {
                for (DevicesDb devicesDb : devices) {
                    accId = devicesDb.getSip_number();
                    if (caller.contains(accId)) {
                        deviceId = devicesDb.getDevice_id();
                        print( "Found currentDevice ID = " + deviceId);
                        break;
                    }
                }
            }
        }

        if (deviceId != -1) {
            print( "Outgoing call or we found currentDevice Id for incoming");
            repositoryController.getDeviceByDeviceId(deviceId).subscribe(devicesDb -> {
                this.currentDevice = devicesDb;
                current_rtsp_link = currentDevice.getRtsp_link();
                loadObject(this.currentDevice.getObject_id());
                processDevice();
                if (accountId == null)
                    getAccountId();
            }, throwable -> print("Loaded Device ---> FAILED"));
        }
        //DO_FINISH
        if (currentDevice == null && !isIncomingCallState.get()) {
            Intent intent = new Intent();
            intent.setAction(BroadcastEventEmitter.getAction(BroadcastEventEmitter.BroadcastAction.DO_FINISH));
            context.sendBroadcast(intent);
            return;
        }

        //check here for camera
        if (!isScreenSateRestored.get() && !isIncomingCallState.get() && currentDevice.getDevice_type() == Constants.TYPE_CAMERA) {
            isCamera.set(true);
            isPanelShown.set(true);
        }

        if (!isIncomingCallState.get() && !isCamera.get() && !isScreenSateRestored.get()) {
            print( "===> Make call from process 2");
            doMakeCall();
        }
        loadSettings();
        if ( isIncomingCallState.get() && !isCallInvited) {
            print( "Make service to answer call");
            SipServiceCommands.setRingtone(context, true);
            SipServiceCommands.onIncomingInvite(context, accountId,sessionId, callerDisplayName,
                    caller, calleeDisplayName, callee, audioCodecs, videoCodecs,
                    existsAudio, existsVideo, sipMessage);

            isCallInvited = true;
        }
    }

    //
    //processDevice()
    //
    private void processDevice() {
        if (!isConciergeCall.get()) {
            // FIXME
            // get to know why calling deviceName.set(...) doesn't influence on binding to TextView.
            // Code following after next line is to be removed
            deviceName.set(currentDevice.getName());
            setDeviceName();
            isFavorite.set(currentDevice.is_favorite == 1);
            if (!currentDevice.getDftm1().isEmpty())
                isDTMFOneConfigured.set(true);
            if (!currentDevice.getDftm2().isEmpty())
                isDTMFTwoConfigured.set(true);
        }
    }

    //
    //takeSnapshot
    //
    private void takeSnapshot(View view) {

        Thread r = new Thread(() -> {
            File outputFile = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (view.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    outputFile = getOutputFile(Environment.DIRECTORY_PICTURES);
                } else {
                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                outputFile = getOutputFile(Environment.DIRECTORY_PICTURES);
            }
            if (outputFile != null) {
                saveStreamSnapshot(outputFile);
            }
        });
        r.start();
    }


    //
    //saveScreenSnapshot
    //
    private void saveStreamSnapshot(File file) {

        SnapshotsHelper helper = new SnapshotsHelper(current_rtsp_link, file);
        helper.saveSnaphot();
        helper.release();
        handler.post(() -> {
            if (toast != null && toast.getView().isShown()) {
                toast.cancel();
            }
            toast = Toast.makeText(context, R.string.text_toast_saved, Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    private File getOutputFile(String dir) {

        handler.post(() -> {
            if (toast != null && toast.getView().isShown()) {
                toast.setText(R.string.text_toast_saving);
            } else {
                toast = Toast.makeText(context, R.string.text_toast_saving, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        String appFolderName = App.getContext().getApplicationContext().getPackageName();

        File d = new File(Environment.getExternalStoragePublicDirectory(dir), appFolderName);
        d.mkdirs();

        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = FILE_NAME_PREFIX + FILE_NAME_SEPARATOR + timestamp + FILE_EXTENSION;
        File targetFile = new File(d, fileName);

        int counter = 1;

        while (targetFile.exists()) {
            fileName = FILE_NAME_PREFIX + FILE_NAME_SEPARATOR + timestamp + FILE_NAME_SEPARATOR + counter++ + FILE_EXTENSION;
            targetFile = new File(d, fileName);
        }

        return targetFile;
    }

    //
    //loadObject
    //
    @SuppressLint("CheckResult")
    private void loadObject(int id) {
        repositoryController.getObject(id).subscribe(objectDb -> {
            this.object = objectDb;
            accountId = getAccountId();
            print( "Loaded object ---> " + this.object.getName());
            createDevicesRv();
        }, throwable -> {
            object = null;
            print( "Loaded object ---> FAILED");
        });
    }

    //
    // setContext
    //
    public void setContext(Context context) {
        this.context = context;
    }

    //
    // getAccountId
    //
    private String getAccountId() {
        if ( object != null) {
            return "sip:" + object.getSip_number() + "@" + object.getIp_address();
        }
        return "sip:0@0";
    }

    //
    // onClick
    //
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container_lock_1:
                if (isDTMFOneConfigured.get() && isConversationInProgress.get()) {
                    print( "Send DTMF1 code " + currentDevice.getDftm1());
                    SipServiceCommands.sendDTMF(context, accountId, sessionId, currentDevice.getDftm1());
                }
                break;

            case R.id.container_lock_2:
                if (isDTMFTwoConfigured.get() && isConversationInProgress.get()) {
                    print( "Send DTMF2 code " + currentDevice.getDftm1());
                    SipServiceCommands.sendDTMF(context, accountId, sessionId, currentDevice.getDftm2());
                }
                break;

            case R.id.container_accept_call:
                if (isIncomingCallState.get() && canHandleCall()) {
                    print( "Accepting call for account Id = " + accountId);
                    SipServiceCommands.acceptIncomingCall(context, accountId);
                    SipServiceCommands.setRingtone(context, false);
                } else {
                    print( "===> Make call from container accept call");
                    doMakeCall();
                }
                break;

            case R.id.container_camera_shot:
            case R.id.container_shot:
                takeSnapshot(view);
                break;

            case R.id.container_micro:
                isMuted.set(!isMuted.get());
                doCallMute(isMuted.get());
                break;

            case R.id.container_hangup:
                isCallRejected.set(false);
                isChangingDevice.set(false);
                print("Hangup from Container Hangup");
                doHangUp();
                //PortSipEventHandler.setRejectCall(false);
                //PortSipEventHandler.longVal = -1;
                break;

            case R.id.container_decline_call:
                doDeclineCall();
                //PortSipEventHandler.setRejectCall(false);
                //PortSipEventHandler.longVal = -1;
                break;

            case R.id.container_volume:
                isLoudSpeakerOn.set(!isLoudSpeakerOn.get());
                doSetLoudspeaker(isLoudSpeakerOn.get());
                break;
        }
    }

    //
    //doMakeCall
    //
    private void doMakeCall() {
        isCallRejected.set(true);
        isCallInProgress.set(true);
        if (isConciergeCall.get()) {
            print( "Make call from " + accountId + " to " + concierge_sip);
            SipServiceCommands.makeCall(context, accountId, concierge_sip);

        } else {
            print( "Make call from " + accountId + " to " + currentDevice.getSip_number());
            SipServiceCommands.makeCall(context, accountId, currentDevice.getSip_number());
        }
    }

    //
    // createDevicesRb
    //
    private void createDevicesRv() {
        print( "Create Devices RV");
        deviceListAdapter.set(new DeviceListAdapter(R.layout.item_device_list, loadDevices()));
        Objects.requireNonNull(deviceListAdapter.get()).addOnItemClickListener((position, item) -> {
            //hide list
            isDeviceListShown.set(false);
            //print( "check if we need to change object deviceId = " + deviceId + " new id = " + listDevices.get(position).getDevice_id());
            if (deviceId == listDevices.get(position).getDevice_id())
                return;

            Boolean isCallable = listDevices.get(position).getIs_callable();
            if (isCallable != null && !isCallable) {
                DialogHelper.createExplanationDialog(context,
                        R.string.text_error_panel_not_callable);
                return;
            }

            deviceId = listDevices.get(position).getDevice_id();
            if ( isIncomingCallState.get() ) {
                print( "New Camera and Incoming call. We just need to change rtsp link");
                stopDisplayCameraStream();
                current_rtsp_link = listDevices.get(position).getRtsp_link();
                print( "Changin video to " + current_rtsp_link);
                startVideo();
                return;
            }
            stopDisplayCameraStream();
            print( "setting new currentDevice");
            isChangingDevice.set(true);
            currentDevice = listDevices.get(position);
            current_rtsp_link = currentDevice.getRtsp_link();
            processDevice();
            if (currentDevice.getDevice_type() == Constants.TYPE_CAMERA) {
                print( "Setting new camera");
                if (isCallInProgress.get()) {
                    isDialNew.set(false);
                    doHangUp();
                }
                isCamera.set(true);
            } else {
                print( "Setting new panel");
                isCamera.set(false);
                if (isCallInProgress.get()) {
                    isDialNew.set(true);
                    doHangUp();
                } else {
                    doReleaseLine();
                    //print( "===> Make call from Create devices RV");
                    doMakeCall();
                }
            }
            final Handler handler = new Handler();
            handler.postDelayed(this::startVideo, 1000);
        });
    }

    //
    //loadDevices
    //
    @SuppressLint("CheckResult")
    private ArrayList<DevicesDb> loadDevices() {
        print( "Load devices");
        //clear current list of devices
        listDevices = new ArrayList<>();
        try {
            if (object == null) {
                changeDevicesButtonState(false);
                return listDevices;
            }
            if (isIncomingCallState.get()) {
                repositoryController.getAllDevices(Constants.TYPE_CAMERA, object.getObject_id()).subscribe(DevicesDb -> this.devices = DevicesDb);
            } else {
                repositoryController.getAllDevices(object.getObject_id()).subscribe(DevicesDb -> this.devices = DevicesDb);
            }

            listDevices.addAll(this.devices);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return listDevices;
    }

    @SuppressLint("CheckResult")
    private void loadSettings() {
        repositoryController.getFirstSettings().subscribe(settingsDb ->
            settings = settingsDb,
            throwable -> {
                settings = new SettingsDb();
                settings.setCall_type(1);
            });
    }

    //
    //changeDevicesButtonState();
    //
    private void changeDevicesButtonState(boolean value) {
        print( "Change Device Button State with value = " + value);
        isShowButton.set(value);
        /*if (listDevices.size() == 0 && isIncomingCallState.get()) {
            //print( "Array List = 0 and getting incoming call");
            isShowButton.set(false);
            isDeviceListShown.set(false);
            return;
        }
        if (isCallInProgress.get() ) { //&& !isConversationInProgress.get()
            print( "Call In progress = true and no Conversation ");
            isShowButton.set(true);
            isDeviceListShown.set(false);
            return;
        } else {
            print( "Just settings the value");
            isShowButton.set(value);
        }*/
    }

    //
    //onDisplayPanelClicked
    //
    @SuppressWarnings("unused")
    public void onDisplayPanelClicked(View v) {
        if (isFullScreen.get()) {
            if (!isDeviceListShown.get()) {
                isPanelShown.set(!isPanelShown.get());
            } else {
                isPanelShown.set(false);
            }
        }

        isDeviceListShown.set(false);
        //v.setSelected(!isPanelShown.get());
        //AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        /*try {
            print( "Seeting speaker to " + isLoudSpeakerOn.get());
            audioManager.setSpeakerphoneOn(isLoudSpeakerOn.get());
        } catch (Exception e) {
            e.printStackTrace();
            print( "Error while setting loudspeaket.");
            e.printStackTrace();
        }*/
    }

    //
    //onFullScreenClicked
    //
    @SuppressWarnings("unused")
    public void onFullScreenClicked(View v) {
        isFullScreen.set(!isFullScreen.get());
        isPanelShown.set(!isFullScreen.get());
    }

    //
    //onDeviceListClicked
    //
    @SuppressWarnings("unused")
    public void onDeviceListClicked(View v) {
        isDeviceListShown.set(!isDeviceListShown.get());
        v.setSelected(isDeviceListShown.get());
    }
    //
    //onDeviceListClicked
    //
    @SuppressWarnings("unused")
    public void onBackToCallClicked(View v) {
        showBackToCallButton.set(false);
        stopDisplayCameraStream();
        deviceId = currentDevice.getDevice_id();
        current_rtsp_link = currentDevice.getRtsp_link();
        print( "Changing video BACK to " + current_rtsp_link);
        startVideo();
    }

    //
    //onFavoriteClick
    //
    @SuppressLint("CheckResult")
    @SuppressWarnings("unused")
    public void onFavoriteClick(View v) {
        if (currentDevice != null) {
            currentDevice.setIs_favorite(isFavorite.get() ? 1 : 0);
            repositoryController.addDevice(currentDevice).subscribe(() -> {
            }, throwable -> {
            });
        }
    }

    //
    // onCallState
    //
    void onCallState(int callState) {
        switch (callState) {
            case CALL_STATE_CONNECTING:
                print( " STATE CONNECTING");
                isCallInProgress.set(true);
                changeDevicesButtonState(true);
                isLoudSpeakerOn.set(true);
                doSetLoudspeaker(isLoudSpeakerOn.get());
                return;

            case CALL_STATE_CONNECTED:
                isCallInProgress.set(true);
                isConversationInProgress.set(true);
                isCallRejected.set(false);
                changeDevicesButtonState(true);
                if(!isLoudSpeakerOn.get()) {
                    isLoudSpeakerOn.set(true);
                    doSetLoudspeaker(isLoudSpeakerOn.get());
                }
                return;
            case CALL_STATE_DISCONNECTED:
                SipServiceCommands.setRingtone(context, false);
                if (isCallInProgress.get() && !isConversationInProgress.get() &&!isChangingDevice.get() && !didOnHook ) {
                    isCallFailed.set(true);
                }
                if (didOnHook)
                    isCallRejected.set(false);
                didOnHook = false;
                isCallInvited = false;
                isCallInProgress.set(false);
                isConversationInProgress.set(false);
                //changeDevicesButtonState(false);
                if (isDialNew.get()) {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        startVideo();
                        print( "===> Make call onCallState");
                        doMakeCall();
                        isDialNew.set(false);
                    }, 2000);
                }
                isMuted.set(false);
                doCallMute(false);
                doReleaseLine();
        }
    }
    //
    //doHangUp
    //
    private void doHangUp() {
        print( "do Hangup accountID= " + accountId);
        didOnHook = true;
        SipServiceCommands.hangUpCall(context, accountId);
    }
    //
    //doHangUp
    //
    public void doHangUpAllCalls() {
        print( "do Hangup accountID= " + accountId);
        didOnHook = true;
        SipServiceCommands.hangUpAllCalls(context, accountId);
    }
    //
    //doDeclineCall
    //
    public void doDeclineCall() {
        print("Decline Incoming Call");
        SipServiceCommands.setRingtone(context,false);
        SipServiceCommands.declineIncomingCall(context, accountId, sessionId, 486);
    }
    //
    //cancelCall
    //
    void cancelCall() {
        print( "cancelCall called");
        //PortSipEventHandler.setRejectCall(false);
        //PortSipEventHandler.longVal = -1;
        if (isIncomingCallState.get() || isCallInProgress.get()) {
            if (isConversationInProgress.get() || isCallInProgress.get()) {
                print( "cancelCall called do Hangup");
                doHangUpAllCalls();
                return;
            }
            print( "cancelCall called do Decline");
            doDeclineCall();
        } else {
            if (isConversationInProgress.get() || isCallInProgress.get()) {
                doHangUpAllCalls();
            } else {
                doHangUpAllCalls();
            }
        }
    }
    //
    //doReleaseLine()
    //
    private void doReleaseLine() {
        SipServiceCommands.releaseCurrentLine(context);
    }
    //
    //doCallMute
    //
    private void doCallMute(boolean value) {
        SipServiceCommands.setCallMute(context, accountId, value);
    }

    //
    //doSetLoudspeaker
    //
    private void doSetLoudspeaker(boolean value) {
        print( "Set loud speaker to " + value);
        SipServiceCommands.setLoudspeaker(context, accountId, value);
    }

    //
    //getDeviceScreenState
    //
    DeviceScreenState getDeviceScreenState() {
        print( "get Device Screen State");
        isScreenSateRestored.set(true);
        return new DeviceScreenState(
                isDeviceListShown.get(),
                isPanelShown.get(),
                isFullScreen.get(),
                isIncomingCallState.get(),
                isMuted.get(),
                isLoudSpeakerOn.get(),
                isCallInProgress.get(),
                isConversationInProgress.get(),
                isShowButton.get(),
                isConciergeCall.get(),
                isCamera.get(),
                isScreenSateRestored.get(),
                isCallRejected.get(),
                currentCallId,
                concierge_sip,
                objectId,
                deviceId,
                accountId,
                displayName,
                remoteUri,
                sessionId,
                isCallInvited);
    }

    //
    //setDeviceScreenState
    //
    void setDeviceScreenState(DeviceScreenState state) {
        print( "set Device Screen State");
        isDeviceListShown.set(state.isDeviceListShown());
        isPanelShown.set(state.isPanelShown());
        isFullScreen.set(state.isFullScreen());
        isIncomingCallState.set(state.isIncomingCallState());
        isMuted.set(state.isMuted());
        isLoudSpeakerOn.set(state.isLoudSpeakerOn());
        isCallInProgress.set(state.isCallInProgress());
        isConversationInProgress.set(state.isConversationInProgress());
        isShowButton.set(state.isShowButton());
        isConciergeCall.set(state.isConciergeCall());
        isCamera.set(state.isCamera());
        isScreenSateRestored.set(state.isStateRestored());
        isCallRejected.set(state.isCallRejected());
        currentCallId = state.getCurrentCallId();
        concierge_sip = state.getConcierge_sip();
        objectId = state.getObjectId();
        deviceId = state.getDeviceId();
        accountId = state.getAccountId();
        displayName = state.getDisplayName();
        remoteUri = state.getRemoteUri();
        isCallInvited = state.isCallInvited();
        sessionId = state.getSessionId();
    }

    //
    // setPermission
    //
    void setRecordAudioGranted() {
        isRecordAudioGranted = true;
    }

    //
    // canHandleCalls
    //
    private boolean canHandleCall() {
        if (!isRecordAudioGranted) {
            Toast.makeText(context, context.getString(R.string.can_not_handle_calls), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //
    //initCameraStream
    //
    public void initCameraStream(SurfaceView view,
                                 VLCPlayer.PlayerEventListener listener,
                                 IVLCVout.Callback callback,
                                 View.OnLayoutChangeListener onLayoutChangeListener) {

        if (view == null) {
            print( "View == NULL");
        } else {
            print( "View != NULL");
        }
        if (currentDevice == null) {
            print( "Device == NULL");
            //return;
        }
        playerEventListener = listener;
        playerSurfaceView = view;
        playerCallBack = callback;
        this.onLayoutChangeListener = onLayoutChangeListener;
    }

    //
    //startPlayRTSPLink
    //
    private void createPlayer() {
        if (current_rtsp_link != null && !current_rtsp_link.isEmpty()) {
            //SurfaceHolder viewHolder = view.getHolder();
            //TextureView.setKeepScreenOn(true);
            print( "Creating player for " + current_rtsp_link);
            if ( !current_rtsp_link.equalsIgnoreCase(currentDevice.getRtsp_link())) {
                showBackToCallButton.set(true);
            }
            player = VLCPlayer.getVLCPlayer(context, playerSurfaceView, current_rtsp_link);
            player.setPlayerEventListener(playerEventListener);
            player.setViewportSize(playerSurfaceView.getWidth(), playerSurfaceView.getHeight());
        } else {
            preparingVideoText.set(context.getResources().getString(R.string.text_failed_connect_to_camera));
        }
    }

    //
    //startVideo
    //
    public void startVideo() {
        if (!isConciergeCall.get()) {
            if (isIncomingCallState.get() && settings != null && settings.getCall_type() == 0) {
                showVideoframe.set(false);
                preparingVideoText.set(context.getResources().getString(R.string.text_video_disabled));
                //print( "Video turned off");
            } else {
//                if (isIncomingCallState.get()) {
//                    if (currentDevice != null) {
//                        print( "Device not null");
//                        checkedVideoStart();
//                    }
//                } else {
//                    checkedVideoStart();
//                }
                checkedVideoStart();
            }
        }
    }

    //
    //checkedVideoStart
    //
    private void checkedVideoStart() {
        //print( "checkedVideoStart");
        preparingVideoText.set(context.getResources().getString(R.string.text_prepare_video));
        if (playerSurfaceView != null) {
            print( "Start video");
            playerSurfaceView.addOnLayoutChangeListener(onLayoutChangeListener);
            createPlayer();
            startDisplayCameraStream();
        } else {
            print( "playerSurfaceView == null");
        }
    }
    //
    //displayVideoError
    //
    public void displayVideoError() {
        preparingVideoText.set(context.getResources().getString(R.string.text_failed_connect_to_camera));
    }
    //
    //startDisplayCameraStream
    //
    private void startDisplayCameraStream() {
        if (player != null && !player.isPlaying()) {
            print( "StartDisplayCameraStream");
            player.play(playerCallBack);
        }
    }

    //
    //stopDisplayCameraStream
    //
    public void stopDisplayCameraStream() {
        if (!isConciergeCall.get()) {
            if (player != null ) {
                print( "Stop Display Camera Stream");
                player.stop();
                player = null;
                showVideoframe.set(false);
            }
            if (playerSurfaceView != null) {
                playerSurfaceView.invalidate();
                playerSurfaceView.removeOnLayoutChangeListener(onLayoutChangeListener);
            }
        }
    }

    //
    //setViewportSize
    //
    public void setViewportSize(int w, int h) {
        if (player == null)
            return;
        player.setViewportSize(w, h);
    }

    //
    //setIsScreenShotEnabled
    //
    public void setIsScreenShotEnabled(boolean value) {
        if (player != null) {
            isScreenShotEnabled.set(value);
            player.setVolume(0);
        }
    }

    //
    //getDecviceName
    //
    public String getDeviceName() {
        if (currentDevice != null)
            return currentDevice.getName();
        else
            return " ";
    }
    //
    //setDeviceName
    //
    private void setDeviceName() {
        DeviceContract contract = getContract();
        if (contract != null )
            contract.setDeviceName(deviceName.get());
        else
            print("Contract is null");
    }

    public void revalidateCameraStream() {
        showVideoframe.set(false);

        stopDisplayCameraStream();
        startVideo();
    }

    @Override
    public void checkServerStatus(Context context) {
        print("Overrided checkServerStatus");
    }

    private void print(String msg) {
        //Logger.error(TAG+"===>", msg);
    }
}
