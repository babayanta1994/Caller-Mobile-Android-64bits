package ru.true_ip.trueip.service.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.portsip.PortSipEnumDefine;
import com.portsip.PortSipErrorcode;
import com.portsip.PortSipSdk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import ru.true_ip.trueip.BuildConfig;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.service.data.BroadcastEventEmitter;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.portsip.CallStates;
import ru.true_ip.trueip.service.portsip.Line;
import ru.true_ip.trueip.service.portsip.MyPortSipSDK;
import ru.true_ip.trueip.service.portsip.PortSipEventHandler;
import ru.true_ip.trueip.service.portsip.Session;
import ru.true_ip.trueip.service.portsip.SettingConfig;
import ru.true_ip.trueip.utils.Constants;

import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_ACCEPT_INCOMING_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_CHANGE_REGISTRATION;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_CREATE_ACCOUNT;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_DECLINE_INCOMING_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_GET_CALL_STATUS;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_GET_REGISTRATION_STATUS;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_HANG_UP_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_HANG_UP_CALLS;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_HOLD_CALLS;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_INCOMING_INVITE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_MAKE_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_REFRESH_REGISTRATION;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_REJECT_INCOMING_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_RELEASE_LINE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_REMOVE_ACCOUNT;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_RESTART_SIP_STACK;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SEND_DTMF;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SET_ACCOUNT;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SET_HOLD;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SET_LOUDSPEAKER;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SET_MUTE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SET_RINGTONE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SHOW_ACCOUNT_SATE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_SHOW_ALL_ACCOUNTS_SATE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_TOGGLE_HOLD;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_TOGGLE_MUTE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_TRANSFER_CALL;
import static ru.true_ip.trueip.service.service.SipServiceCommands.ACTION_UPDATE_ACCOUNTS_LIST;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_ACCOUNT_DATA;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_ACCOUNT_ID;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_CODE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_DIAL_NUMBER;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_DTMF;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_MUTE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_OBJECT_ID;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_REGISTRATION_STATE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_RINGTONE_MODE;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_SESSION_ID;
import static ru.true_ip.trueip.service.service.SipServiceCommands.PARAM_TO_SPEAKER;


/**
 *
 * Created by Eugen on 10.10.2017.
 * Modified by Andrey Filimonov
 */

public class SipService extends Service {
    public static final String TAG = SipService.class.getSimpleName();
    private final static String key = "accounts";
    private BroadcastEventEmitter eventEmitter;
    public static final int MAX_ACCOUNT_SIZE = 4;
    private Ringtone mRingTone = null;
    private static final long[] VIBRATOR_PATTERN = {0, 1000, 1000};
    private Vibrator mVibrator;
    private Uri mRingtoneUri;

    //PortSIP
    private final String localIP = "0.0.0.0";
    private ArrayList<MyPortSipSDK> portSipSDKs = new ArrayList<>();
    private static ArrayList<SipAccountData> sipAccountData = new ArrayList<>();
    private final String LogPath = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
    private String licenseKey ="test";
    //
    //onCreate
    //
    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = createNotification();
        startForeground(1, notification);
        print( "onCreate: SipService started");
        eventEmitter = new BroadcastEventEmitter(SipService.this);
        mRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(SipService.this, RingtoneManager.TYPE_RINGTONE);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < _CallSessions.length; i++) {
            _CallSessions[i] = new Line(i);
        }
    }
    //
    //onConciergeCheckedChanged
    //
    private Notification createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(Constants.BACKGROUND_SERVICE_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, Constants.BACKGROUND_SERVICE_CHANNEL_ID)
                .setContentTitle(getString(R.string.text_foreground_service_title))
                .setContentText(getString(R.string.text_foreground_service_message))
                .setContentIntent(createContentIntent())
                .setSmallIcon(R.drawable.ic_push_notification)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .build();
    }
    //
    //createContentIntent
    //
    private PendingIntent createContentIntent() {
        Intent contentIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        contentIntent.setData(Uri.fromParts("package", getPackageName(), null));

        return PendingIntent.getActivity(this, 0, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    //
    //onStartCommand
    //
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        print( "SipService startService command");
        String action;

        if (intent == null || intent.getAction() == null) {
            action = ACTION_UPDATE_ACCOUNTS_LIST;
        } else {
            action = intent.getAction();
        }
        print("action : " + action);
        switch (action) {
            case ACTION_SET_RINGTONE:
                boolean mode = intent.getBooleanExtra(PARAM_RINGTONE_MODE, false);
                if (mode)
                    startRingtone();
                else
                    stopRingtone();
                break;
            case ACTION_REFRESH_REGISTRATION:
                handleRefreshRegistration(intent);
                break;
            case ACTION_RELEASE_LINE:
                handleReleaseLine();
                break;
            case ACTION_CREATE_ACCOUNT:
                print( "onStartCommand: ACTION_CREATE_ACCOUNT");
                handleCreateAccount(intent);
                break;
            case ACTION_MAKE_CALL:
                print( "onStartCommand: ACTION_MAKE_CALL");
                handleMakeCall(intent);
                break;
            case ACTION_SHOW_ACCOUNT_SATE:
                print( "onStartCommand: ACTION_SHOW_ACCOUNT_SATE");
                handleActionState(intent);
                break;
            case ACTION_UPDATE_ACCOUNTS_LIST:
                print( "onStartCommand: ACTION_UPDATE_ACCOUNTS_LIST");
                handleUpdateAccountList(intent);
            case ACTION_SHOW_ALL_ACCOUNTS_SATE:
                print( "onStartCommand: ACTION_SHOW_ALL_ACCOUNTS_SATE");
                getAllAccountsState();
                break;
            case ACTION_ACCEPT_INCOMING_CALL:
                handleAcceptIncomingCall(intent);
                break;
            case ACTION_DECLINE_INCOMING_CALL:
                handleDeclineIncomingCall(intent);
                break;
            case ACTION_REJECT_INCOMING_CALL:
                handleRejectIncomingCall(intent);
                break;
            case ACTION_HANG_UP_CALL:
                handleHangUpCall(intent);
                break;
            case ACTION_HANG_UP_CALLS:
                handleHangUpActiveCalls(intent);
                break;
            case ACTION_SET_ACCOUNT:
                //handleSetAccount(intent);
                break;
            case ACTION_REMOVE_ACCOUNT:
                handleRemoveAccount(intent);
                break;
            case ACTION_RESTART_SIP_STACK:
                //handleRestartSipStack();
                break;
            case ACTION_HOLD_CALLS:
                //handleHoldActiveCalls(intent);
                break;
            case ACTION_GET_CALL_STATUS:
                //handleGetCallStatus(intent);
                break;
            case ACTION_SEND_DTMF:
                handleSendDTMF(intent);
                break;
            case ACTION_SET_HOLD:
                //handleSetCallHold(intent);
                break;
            case ACTION_TOGGLE_HOLD:
                //handleToggleCallHold(intent);
                break;
            case ACTION_SET_MUTE:
                handleSetCallMute(intent);
                break;
            case ACTION_TOGGLE_MUTE:
                handleToggleCallMute(intent);
                break;
            case ACTION_TRANSFER_CALL:
                //handleTransferCall(intent);
                break;
            case ACTION_GET_REGISTRATION_STATUS:
                //handleGetRegistrationStatus(intent);
                break;
            case ACTION_SET_LOUDSPEAKER:
                handleSetLoudspeaker(intent);
                break;
            case ACTION_INCOMING_INVITE:
                handleAcceptIncomingInvite(intent);
                break;
            case ACTION_CHANGE_REGISTRATION:
                handleChangeRegistration(intent);
                break;
            default:
                print( "onStartCommand: DEFAULT");
                break;
        }
        return START_STICKY;
    }
    //
    //handleRefreshRegistration
    //
    private void handleRefreshRegistration(Intent intent) {
        performRefreshRegistration(intent.getStringExtra(PARAM_ACCOUNT_ID));
    }
    //
    //getSdkByAccountId
    //
    private MyPortSipSDK getSdkByAccountId(String accId) {

        for (MyPortSipSDK sdk : portSipSDKs ) {
            if ( sdk.getSipAccountData().getIdUri().equalsIgnoreCase(accId) ) {
                return sdk;
            }
        }
        print( "SDK not found!!!!!!!!!");
        return null;
    }
    //
    //handleAcceptIncomingInvite
    //
    private void handleAcceptIncomingInvite(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null)
            return;
        String accountID = extras.getString(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID);
        long sessionId = extras.getLong(BroadcastEventEmitter.BroadcastParameters.SESSION_ID,0);
        String callerDisplayName = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLER_DISPLAY_NAME);
        String caller = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLER);
        String calleeDisplayName = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLEE_DISPLAY_NAME);
        String callee = extras.getString(BroadcastEventEmitter.BroadcastParameters.CALLEE);
        String audioCodecs = extras.getString(BroadcastEventEmitter.BroadcastParameters.AUDIO_CODECS);
        String videoCodecs = extras.getString(BroadcastEventEmitter.BroadcastParameters.VIDEO_CODECS);
        boolean existsAudio = extras.getBoolean(BroadcastEventEmitter.BroadcastParameters.EXISTING_AUDIO);
        boolean existsVideo = extras.getBoolean(BroadcastEventEmitter.BroadcastParameters.EXISTING_VIDEO);
        String sipMessage = extras.getString(BroadcastEventEmitter.BroadcastParameters.SIP_MESSAGE);

        Line tempSession = findIdleLine();

        if (tempSession == null) {
            long sessionID = intent.getLongExtra(PARAM_SESSION_ID, 0);
            int code = intent.getIntExtra(PARAM_CODE, 486);
            for (MyPortSipSDK sdk : portSipSDKs ) {
                if ( sdk.getSipAccountData().getIdUri().equalsIgnoreCase(accountID) ) {
                    sdk.rejectCall(sessionID, code);
                }
            }
            return;
        } else {
            tempSession.setRecvCallState(true);
        }
        if (existsVideo) {
            // If more than one codecs using, then they are separated with "#",
            // for example: "g.729#GSM#AMR", "H264#H263", you have to parse them
            // by yourself.
        }
        if (existsAudio) {
            // If more than one codecs using, then they are separated with "#",
            // for example: "g.729#GSM#AMR", "H264#H263", you have to parse them
            // by yourself.
        }
        tempSession.setSessionId(sessionId);
        tempSession.setVideoState(existsVideo);
        String comingCallTips = "Call incoming: "+ callerDisplayName + "<" + caller +">";
        tempSession.setDescriptionString(comingCallTips);
        setCurrentLine(null, tempSession);
    }
    //
    // handleAcceptIncomingCall
    //
    private void handleAcceptIncomingCall(Intent intent) {
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);
        MyPortSipSDK sdk = getSdkByAccountId(accountID);
        if ( sdk == null )
            return;
        Line sessionLine = (Line) getCurrentSession();
        long sessionId = sessionLine.getSessionId();
        int rt = PortSipErrorcode.INVALID_SESSION_ID;

        //Ring.getInstance(context).stopRingTone();
        if(sessionId != PortSipErrorcode.INVALID_SESSION_ID) {
            Logger.error(TAG,"Session Id = " + sessionLine.getSessionId());
            rt = sdk.answerCall(sessionLine.getSessionId(), false);
        }
        if(rt == 0) {
            sessionLine.setSessionState(true);
            setCurrentLine(sdk, sessionLine);
            sessionLine.setVideoState(false);
        } else {
            sessionLine.reset();
            print( "Failed to answer call! Error Code = " + rt);
        }
    }
    //
    // handleDeclineIncomingCall
    //
    private void handleDeclineIncomingCall(Intent intent) {
        print( "handleDeclineIncomingCall");
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);
        long sessionID = intent.getLongExtra(PARAM_SESSION_ID, 0);
        int code = intent.getIntExtra(PARAM_CODE, 486);
        for (MyPortSipSDK sdk : portSipSDKs ) {
            if ( sdk.getSipAccountData().getIdUri().equalsIgnoreCase(accountID) ) {
                sdk.rejectCall(sessionID, code);
                sdk.getPortSipEventHandler().clearRejectCallFlag();
                notifyCallDisconnected(accountID);
            }
        }
    }
    //
    // handleRejectIncomingCall
    //
    private void handleRejectIncomingCall(Intent intent) {
        print( "---> handleRejectIncomingCall");
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);
        long sessionID = intent.getLongExtra(PARAM_SESSION_ID, 0);
        int code = intent.getIntExtra(PARAM_CODE, 486);
        for (MyPortSipSDK sdk : portSipSDKs ) {
            if ( sdk.getSipAccountData().getIdUri().equalsIgnoreCase(accountID) ) {
                sdk.rejectCall(sessionID, code);
            }
        }
    }

    //
    // handleHangUpCall
    //
    private void handleHangUpCall(Intent intent) {
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);

        MyPortSipSDK sdk = getSdkByAccountId(accountID);
        if (sdk == null)
            return;
        Line sessionLine = (Line) getCurrentSession();
        long sessionId = sessionLine.getSessionId();
        if (sessionId != PortSipErrorcode.INVALID_SESSION_ID) {
            print( "Hanging up call for account " + accountID);
            sdk.hangUp(sessionId);
            sdk.getPortSipEventHandler().clearRejectCallFlag();
        }
        sessionLine.reset();
        eventEmitter.callState(accountID, CallStates.CALL_STATE_DISCONNECTED);
    }
    //
    // handleHangUpActiveCalls
    //
    private void handleHangUpActiveCalls(Intent intent) {
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);

        MyPortSipSDK sdk = getSdkByAccountId(accountID);
        if (sdk == null)
            return;

        for (int i = Line.LINE_BASE; i < Line.MAX_LINES; ++i)// get idle session
        {
            if (_CallSessions[i].getSessionState() || _CallSessions[i].getRecvCallState()) {

                long sessionId = _CallSessions[i].getSessionId();
                if (sessionId != PortSipErrorcode.INVALID_SESSION_ID) {
                    print( "Hanging up call for account " + accountID);
                    sdk.hangUp(sessionId);
                    sdk.getPortSipEventHandler().clearRejectCallFlag();
                }
                _CallSessions[i].reset();
            }
        }
        //eventEmitter.callState(accountID, CallStates.CALL_STATE_DISCONNECTED);
    }
    //
    //handleReleaseLine
    //
    private void handleReleaseLine() {
        Line sessionLine = (Line)getCurrentSession();
        if (sessionLine != null) {
            long sessionId = sessionLine.getSessionId();
            if (sessionId != PortSipErrorcode.INVALID_SESSION_ID) {
                sessionLine.reset();
            }
        }
    }

    //
    // notifyCallDisconnected
    //
    private void notifyCallDisconnected(String accountID) {
        print( "---> notifyCallDisconnected");
        eventEmitter.callState(accountID, CallStates.CALL_STATE_DISCONNECTED);
    }
    //
    // clearActiveAccountsList
    //
    private void clearActiveAccountsList() {
        Logger.error(TAG,"Unregister in clearActiveAccountsList");
        for (PortSipSdk sdk: portSipSDKs) {
            print("Unregister in clearActiveAccountsList");
            sdk.unRegisterServer();
            sdk.DeleteCallManager();
        }
        portSipSDKs.clear();
        sipAccountData.clear();
    }

    //
    // handleUpdateAccountList
    //
    private void handleUpdateAccountList(Intent intent) {
        clearActiveAccountsList();
        ArrayList<SipAccountData> accountsList = null;
        if ( intent != null) {
            accountsList = intent.getParcelableArrayListExtra(PARAM_ACCOUNT_DATA);
        }
        if (accountsList == null || accountsList.size() == 0 ) {
            accountsList = getSipAccountsData();
        }
        if (accountsList.size() == 0)
            return;
        final ArrayList<SipAccountData> tmp = accountsList;
        for (int i = 0; i < tmp.size(); i++) {
            createAccount(tmp.get(i));
        }
    }
    //
    // handleActionState
    //
    private void handleActionState(Intent intent) {
    }
    //
    // handleMakeCall
    //
    private void handleMakeCall(Intent intent) {
        String accountID = intent.getStringExtra(PARAM_ACCOUNT_ID);
        String number = intent.getStringExtra(PARAM_DIAL_NUMBER);

        print( "Making call to " + number);

        MyPortSipSDK sdk = getSdkByAccountId(accountID);
        if (sdk == null) {
            eventEmitter.callState(accountID, CallStates.CALL_STATE_FAILED);
            return;
        }

        Line currentLine = findIdleLine();
        if (currentLine == null) {
            eventEmitter.callState(accountID, CallStates.CALL_STATE_FAILED);
            return;
        }
        if (currentLine.getSessionState() || currentLine.getRecvCallState()) {
            eventEmitter.callState(accountID, CallStates.CALL_STATE_FAILED);
            print( "Current line is busy now, please switch a line.");
            return;
        }

        // Ensure that we have been added one audio codec at least
        if (sdk.isAudioCodecEmpty()) {
            eventEmitter.callState(accountID, CallStates.CALL_STATE_FAILED);
            print( "Audio Codec Empty,add audio codec at first");
            return;
        }

        // Usually for 3PCC need to make call without SDP
        long sessionId = sdk.call(number, true, false);
        if (sessionId <= 0) {
            eventEmitter.callState(accountID, CallStates.CALL_STATE_FAILED);
            print( "Call failure");
            return;
        }

        currentLine.setSessionId(sessionId);
        currentLine.setSessionState(true);
        currentLine.setVideoState(false);
        setCurrentLine(sdk, currentLine);
        print( currentLine.getLineName() + ": Calling..." + " Session Id = " + sessionId);

        //myApp.updateSessionVideo();
    }
    //
    // onBind
    //
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //
    // startStack
    //
    //
    //handleRemoveAccount(intent);
    //
    private void handleRemoveAccount(Intent intent) {
        String accountId = intent.getStringExtra(PARAM_ACCOUNT_ID);
        int objectId = intent.getIntExtra(PARAM_OBJECT_ID, 0);

        if ( accountId == null || accountId.length() == 0) {
            getEventEmitter().accountRemoved(accountId, objectId);
            return;
        }
        MyPortSipSDK sdk = getSdkByAccountId(accountId);
        if (sdk == null) {
            getEventEmitter().accountRemoved(accountId, objectId);
            return;
        }
        Logger.error(TAG,"Unregister in handleRemoveAccount");
        sdk.unRegisterServer();
        portSipSDKs.remove(sdk);
        for (SipAccountData data: sipAccountData) {
            if(data.getIdUri() != null) {
                if (data.getIdUri().equalsIgnoreCase(accountId)) {
                    sipAccountData.remove(data);
                    break;
                }
            }
        }
        print( "Removed account with ID = " + accountId);
        storeSipAccountsData(sipAccountData);
        getEventEmitter().accountRemoved(accountId, objectId);

    }
    //
    // handleCreateAccount
    //
    private void handleCreateAccount(Intent intent) {
        SipAccountData sipAccountData = intent.getParcelableExtra(PARAM_ACCOUNT_DATA);
        if (sipAccountData != null)
        print( "Handle Create Account Is Active = " + sipAccountData.isActive());
        createAccount(sipAccountData);
        getEventEmitter().accountCreated(sipAccountData.getIdUri());
    }
    //
    // createAccount
    //
    public void createAccount(final SipAccountData accountData) {
        print( "Create Account Is Active = " + accountData.isActive());
        if (portSipSDKs.size() >= MAX_ACCOUNT_SIZE) {
            print( "createAccount: Account limit has been reached");
            Toast.makeText(this, R.string.text_max_account_limit_has_been_reached, Toast.LENGTH_SHORT).show();
            return;
        }
        if (accountData.getSipNumber() == null || accountData.getSipNumber().length() == 0)
            return;
        //Check while account with same SIP NUMBER && IP ADDRESS exist
        int indexOfAccountToRemove = -1;
        for (SipAccountData data: sipAccountData) {
            if (data.getIdUri() != null) {
                if (data.getIdUri().equalsIgnoreCase(accountData.getIdUri())) {
                    //Account exists. Will update it
                    MyPortSipSDK sdk = getSdkByAccountId(data.getIdUri());
                    if (sdk == null)
                        return;
                    Logger.error(TAG,"Unregister in createAccount");
                    sdk.unRegisterServer();
                    portSipSDKs.remove(sdk);
                    indexOfAccountToRemove = sipAccountData.indexOf(data);
                    //SDK removed, SipAccountData removed. Now lets create new one.
                }
            }
        }
        if (indexOfAccountToRemove != -1 ) {
            sipAccountData.remove(indexOfAccountToRemove);
        }
        MyPortSipSDK sdk = new MyPortSipSDK();
        PortSipEventHandler eventHandler = new PortSipEventHandler(this);
        eventHandler.setSipAccountData(accountData);
        sdk.setOnPortSIPEvent(eventHandler);
        sdk.setPortSipEventHandler(eventHandler);
        sdk.CreateCallManager(getApplicationContext());
        String dataPath = Objects.requireNonNull(getFilesDir().getAbsolutePath());
        print("Init with DataPath = " + dataPath);

        int result = sdk.initialize(PortSipEnumDefine.ENUM_TRANSPORT_UDP,
                localIP,
                new Random().nextInt(4940) + 5060,
                PortSipEnumDefine.ENUM_LOG_LEVEL_NONE,
                dataPath,
                Line.MAX_LINES,
                "TrueIP for Android",
                0,
                0,
                dataPath,
                "",
                false,
                null);
        print("Inited Result = " + result);
        if (result != PortSipErrorcode.ECoreErrorNone) {
            Toast.makeText(this, "init Sdk Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        result = sdk.setLicenseKey(licenseKey);

        if (result != PortSipErrorcode.ECoreWrongLicenseKey) {
            // this is necessary to check if speakerphone is on
            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            boolean isSpeakerOn = audioManager.isSpeakerphoneOn();

            sdk.getAudioDevices();
            sdk.setVideoDeviceId(1);
            sdk.setSrtpPolicy(PortSipEnumDefine.ENUM_SRTPPOLICY_NONE);
            sdk.enable3GppTags(false);

            // reset speakerphone phone state because previous calls disable it
            audioManager.setSpeakerphoneOn(isSpeakerOn);
        } else {
            Toast.makeText(this, "Wrong SDK Key", Toast.LENGTH_SHORT).show();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(accountData.getPort());
        } catch (Exception e) {
            port = 5060;
        }
        print( "Set user info for " + accountData.getSipNumber() + " port = " + port);
        result = sdk.setUser(accountData.getSipNumber(),
                accountData.getSipNumber(),
                accountData.getSipNumber(),
                accountData.getPassword(),
                accountData.getServerHost(),
                accountData.getServerHost(),
                port,
                "",
                0,
                "",
                0);

        if (result != PortSipErrorcode.ECoreErrorNone) {
            print("Set User resource failed. Error code = " + result);
        } else {
            SettingConfig.setAVArguments(getApplicationContext(),sdk);
            sdk.setSipAccountData(accountData);
            portSipSDKs.add(sdk);
            sipAccountData.add(accountData);
        }
        if (sdk.getSipAccountData().isActive())
            sdk.registerServer(90,5);
        //Store accounts to Shared references
        storeSipAccountsData(sipAccountData);
        print( "Create account with ID = " + accountData.getIdUri());
    }
    //
    //onDestroy
    //
    @Override
    public void onDestroy() {
        print( "On Destroy");
        for (MyPortSipSDK sdk: portSipSDKs) {
            print( "Unregistering in onDestroy");
            sdk.unRegisterServer();
        }
        super.onDestroy();
    }
    //
    //GetEventEmitter
    //
    public BroadcastEventEmitter getEventEmitter() {
        return eventEmitter;
    }
    //
    //refresh registration
    //
    public void performRefreshRegistration(String accointId) {
        final MyPortSipSDK sdk = getSdkByAccountId(accointId);
        if ( sdk != null) {
            print("Unregister in performRefreshRegistration");
            sdk.unRegisterServer();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                    print( "Refreshing registration for " + sdk.getSipAccountData().getIdUri());
                    sdk.registerServer(90, 5);
            }, 1000L);
        }
    }
    //
    //getAllAccountsState
    //
    public void getAllAccountsState() {
        int code;
        for (MyPortSipSDK sdk: portSipSDKs) {
            if ( sdk.isRegistered())
                code = 1;
            else
                code =0;
            getEventEmitter().registrationState(sdk.getSipAccountData().getIdUri(), code);
        }
    }
    //
    //start ringtone
    //
    public synchronized void startRingtone() {
        if ( mRingTone != null)
            return;

        mVibrator.vibrate(VIBRATOR_PATTERN, 0);

        try {
            mRingTone = RingtoneManager.getRingtone(this, mRingtoneUri);
            if (!mRingTone.isPlaying()) {
                mRingTone.play();
            }
        } catch (Exception exc) {
            print("Error while trying to play ringtone!");
        }
    }
    //
    //stop ringtone
    //
    public synchronized void stopRingtone() {
        mVibrator.cancel();

        if (mRingTone != null) {
            try {
                if (mRingTone.isPlaying()) {
                    mRingTone.stop();
                }
                mRingTone = null;
            } catch (Exception ignored) { }
        }
    }
    //
    // Store Sip Account Data
    //
    public void storeSipAccountsData(ArrayList<SipAccountData> datas) {
        print( "storeSipAccountData");
        Set<String> accounts = new HashSet<>();
        SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();

        for (SipAccountData data: datas ) {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            print("Storing string " + json);
            accounts.add(json);
        }
        editor.putStringSet(key,accounts);
        editor.apply();
    }
    //
    // Get Sip Account Data
    //
    public ArrayList<SipAccountData> getSipAccountsData() {
        print("getSipAccountsData");
        Set<String> accounts;
        ArrayList<SipAccountData> sipAccountDatas = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        accounts = preferences.getStringSet(key, new HashSet<String>());
        Gson gson;
        SipAccountData sipAccountData;
        for (String account: accounts) {
            print( "Converting string " + account);
            gson = new Gson();
            sipAccountData = gson.fromJson(account, SipAccountData.class);
            sipAccountDatas.add(sipAccountData);
        }
        return sipAccountDatas;
    }
    //
    //handleSetCallMute
    //
    private void handleSetCallMute(Intent intent) {
        MyPortSipSDK sdk = getSdkByAccountId(intent.getStringExtra(PARAM_ACCOUNT_ID));
        if ( sdk != null ) {
            boolean value = intent.getBooleanExtra(PARAM_MUTE, false);
            long sessionId = getCurrentSession().getSessionId();
            if (sessionId == -1)
                return;
            //int muteSession (long sessionId, boolean muteIncomingAudio, boolean muteOutgoingAudio, boolean muteIncomingVideo, boolean muteOutgoingVideo)
            print("Muting value = " + value);
            sdk.muteSession(sessionId, false, value, false, value);
        }
    }
    //
    //handleSetLoudspeaker
    //
    private void handleSetLoudspeaker(Intent intent) {
        MyPortSipSDK sdk = getSdkByAccountId(intent.getStringExtra(PARAM_ACCOUNT_ID));
        if (sdk != null) {
            boolean value = intent.getBooleanExtra(PARAM_TO_SPEAKER, false);
            //Set<PortSipEnumDefine.AudioDevice> devices = sdk.getAudioDevices();

            sdk.setAudioDevice(value ? PortSipEnumDefine.AudioDevice.SPEAKER_PHONE : PortSipEnumDefine.AudioDevice.EARPIECE);

            //sdk.setLoudspeakerStatus(intent.getBooleanExtra(PARAM_TO_SPEAKER, false));
        }
    }
    //
    // handleToggleCallMute
    //
    private void handleToggleCallMute(Intent intent) {
    }
    //
    // handleSendDTMF
    //
    private void handleSendDTMF(Intent intent) {
        String symbols = "0123456789*#ABCD";

        MyPortSipSDK sdk = getSdkByAccountId(intent.getStringExtra(PARAM_ACCOUNT_ID));
        if (sdk == null)
            return;

        long sessinId = getCurrentSession().getSessionId();
        if (sessinId == -1)
            return;

        String dtmf = intent.getStringExtra(PARAM_DTMF);
        int code;
        for (int i=0; i < dtmf.length(); i++) {
            code = symbols.indexOf(dtmf.charAt(i));
            if ( code != -1) {
                print( "Sending code " + code + " sessoinID =" + sessinId);
                sdk.sendDtmf(sessinId, PortSipEnumDefine.ENUM_DTMF_MOTHOD_INFO, code, 300, true);
            }
        }
    }
    //
    // portsip integration
    //
    static final private Line[] _CallSessions = new Line[Line.MAX_LINES];
    private static Line _CurrentlyLine = _CallSessions[Line.LINE_BASE];// active line
    //
    // findIdleLine
    //
    public static Line findIdleLine() {

        for (int i = Line.LINE_BASE; i < Line.MAX_LINES; ++i)// get idle session
        {
            if (!_CallSessions[i].getSessionState()
                    && !_CallSessions[i].getRecvCallState()) {
                return _CallSessions[i];
            }
        }

        return null;
    }
    //
    //setCurrentLine
    //
    public static void setCurrentLine(MyPortSipSDK sdk, Line line) {
        if (sdk != null && _CurrentlyLine != null && _CurrentlyLine != line) {
            long sessionId = _CurrentlyLine.getSessionId();
            if (sessionId != PortSipErrorcode.INVALID_SESSION_ID) {
                Logger.info(TAG, "current line is busy");
                PortSipEventHandler handler = sdk.getPortSipEventHandler();
                sdk.setPortSipEventHandler(null);
                sdk.hangUp(sessionId);
                sdk.setPortSipEventHandler(handler);
                sdk.getPortSipEventHandler().clearRejectCallFlag();
            }
            _CurrentlyLine.reset();
        }
        if (line == null) {
            _CurrentlyLine = _CallSessions[Line.LINE_BASE];
        } else {
            _CurrentlyLine = line;
        }

    }

    public void handleChangeRegistration(Intent intent) {
        String accountId = null;
        boolean state = false;
        Bundle extras = intent.getExtras();
        if ( extras != null) {
            accountId = extras.getString(PARAM_ACCOUNT_ID,null);
            state = extras.getBoolean(PARAM_REGISTRATION_STATE, false);
        }
        if ( accountId == null || accountId.length() == 0) {
            return;
        }
        MyPortSipSDK sdk = getSdkByAccountId(accountId);
        if (sdk == null) {
            return;
        }
        if (state) {
            sdk.registerServer(90, 5);
        } else {
            print("Unregister in handleChangeRegistration");
            sdk.unRegisterServer();
        }
        //Store accounts to Shared references
        SipAccountData sipData  = sdk.getSipAccountData();
        for (int i=0; i < sipAccountData.size(); i++) {
            if (sipAccountData.get(i).getIdUri().equalsIgnoreCase(sipData.getIdUri())) {
                sipAccountData.get(i).setActive(state);
                storeSipAccountsData(sipAccountData);
            }
        }
    }

    private void print(String message) {
        Logger.error(TAG+"===>",message);
    }
    //
    //Get Current Line
    //
    public Session getCurrentSession() {
        return _CurrentlyLine;
    }
}
