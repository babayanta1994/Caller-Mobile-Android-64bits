package ru.true_ip.trueip.service.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.service.data.SipAccountData;

/**
 * 
 * Created by Eugen on 10.10.2017.
 */

public class SipServiceCommands {
    private final static String TAG = SipServiceCommands.class.getSimpleName();

    static final String ACTION_RELEASE_LINE = "ACTION_RELEASE_LINE";
    static final String ACTION_CREATE_ACCOUNT = "ACTION_CREATE_ACCOUNT";
    static final String ACTION_UPDATE_ACCOUNTS_LIST = "ACTION_UPDATE_ACCOUNTS_LIST";
    static final String ACTION_MAKE_CALL = "ACTION_MAKE_CALL";
    static final String ACTION_SHOW_ACCOUNT_SATE = "ACTION_SHOW_ACCOUNT_SATE";
    static final String ACTION_SHOW_ALL_ACCOUNTS_SATE = "ACTION_SHOW_ALL_ACCOUNTS_SATE";
    static final String ACTION_ACCEPT_INCOMING_CALL = "acceptIncomingCall";
    static final String ACTION_DECLINE_INCOMING_CALL = "declineIncomingCall";
    static final String ACTION_HANG_UP_CALL = "ACTION_HANG_UP_CALL";
    static final String ACTION_HANG_UP_CALLS = "ACTION_HANG_UP_CALLS";
    static final String ACTION_RESTART_SIP_STACK = "ACTION_RESTART_SIP_STACK";
    static final String ACTION_SET_ACCOUNT = "ACTION_SET_ACCOUNT";
    static final String ACTION_REMOVE_ACCOUNT = "ACTION_REMOVE_ACCOUNT";
    static final String ACTION_HOLD_CALLS = "ACTION_HOLD_CALLS";
    static final String ACTION_GET_CALL_STATUS = "ACTION_GET_CALL_STATUS";
    static final String ACTION_SEND_DTMF = "ACTION_SEND_DTMF";
    static final String ACTION_SET_HOLD = "ACTION_SET_HOLD";
    static final String ACTION_SET_MUTE = "ACTION_SET_MUTE";
    static final String ACTION_TOGGLE_HOLD = "ACTION_TOGGLE_HOLD";
    static final String ACTION_TOGGLE_MUTE = "ACTION_TOGGLE_MUTE";
    static final String ACTION_TRANSFER_CALL = "ACTION_TRANSFER_CALL";
    static final String ACTION_GET_REGISTRATION_STATUS = "ACTION_GET_REGISTRATION_STATUS";
    static final String ACTION_SET_LOUDSPEAKER = "ACTION_SET_LOUDSPEAKER";
    static final String ACTION_REJECT_INCOMING_CALL = "ACTION_REJECT_INCOMING_CALL";
    static final String ACTION_NO_ACTION = "NO_ACTION";
    //Params
    static final String ACTION_INCOMING_INVITE = "ACTION_INCOMING_INVITE";
    static final String ACTION_REFRESH_REGISTRATION = "ACTION_REFRESH_REGISTRATION";
    static final String ACTION_SET_RINGTONE = "ACTION_SET_RINGTONE";
    static final String PARAM_ACCOUNT_DATA = "PARAM_ACCOUNT_DATA";
    static final String PARAM_ACCOUNT_ID = "PARAM_ACCOUNT_ID";
    static final String PARAM_DIAL_NUMBER = "PARAM_DIAL_NUMBER";
    static final String PARAM_DTMF = "dtmf";
    static final String PARAM_MUTE = "mute";
    static final String PARAM_TO_SPEAKER = "toSpeaker";
    static final String PARAM_SESSION_ID = "session_id";
    static final String PARAM_CODE = "code";
    static final String PARAM_CALLER_DISPLAY_NAME = "callerDisplayName";
    static final String PARAM_CALLER = "caller";
    static final String PARAM_CALLEE_DISPLAY_NAME = "calleeDisplayName";
    static final String PARAM_CALLEE = "callee";
    static final String PARAM_OBJECT_ID = "PARAM_OBJECT_ID";
    static final String PARAM_AUDIO_CODECS = "audioCodecs";
    static final String PARAM_VIDEO_CODECS = "videoCodecs";
    static final String PARAM_EXISTING_AUDIO = "existsAudio";
    static final String PARAM_EXISTING_VIDEO = "existsVideo";
    private static final String PARAM_SIP_MESSAGE = "sipMessage";
    static final String PARAM_RINGTONE_MODE = "PARAM_RINGTONE_MODE";
    public static final String ACTION_CHANGE_REGISTRATION = "action_change_registration";
    public static final String PARAM_REGISTRATION_STATE = "action_registration_state";


    private static void startServiceInThread(final Context context, final Intent intent) {
        Thread t = new Thread(){
            public void run(){
                context.startService(intent);
            }
        };
        t.start();
    }
    //
    //set ringtone
    //
    public static void setRingtone(Context context, boolean mode) {
        Logger.error(TAG, "Set Ringtone");
        final Intent intent = new Intent(context, SipService.class);

        intent.setAction(ACTION_SET_RINGTONE);
        intent.putExtra(PARAM_RINGTONE_MODE, mode);
        startServiceInThread(context, intent);
    }
    //
    //start service
    //
    public static void startService(Context context) {
        Logger.error(TAG, "Start Service");
        startServiceInThread(context, new Intent(context, SipService.class));
    }
    //
    //stop service
    //
    public static void stopService(Context context) {
        Logger.error(TAG, "Stop Service");
        /*ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SipService.class.getName().equals(service.service.getClassName())) {
                Log.i(TAG,"Stopping service.");
                context.stopService(new Intent(context, SipService.class));
            }
        }*/
    }
    //
    // On Incoming Invite
    //
    public static void onIncomingInvite(Context context,
                                        String accountID,
                                        long sessionId,
                                        String callerDisplayName,
                                        String caller,
                                        String calleeDisplayName,
                                        String callee,
                                        String audioCodecs,
                                        String videoCodecs,
                                        boolean existsAudio,
                                        boolean existsVideo,
                                        String sipMessage)  {
        Logger.error(TAG, "On Incoming Event");
        final Intent intent = new Intent(context, SipService.class);

        intent.setAction(ACTION_INCOMING_INVITE);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_SESSION_ID, sessionId);
        intent.putExtra(PARAM_CALLER_DISPLAY_NAME, callerDisplayName);
        intent.putExtra(PARAM_CALLER, caller);
        intent.putExtra(PARAM_CALLEE_DISPLAY_NAME, calleeDisplayName);
        intent.putExtra(PARAM_CALLEE, callee);
        intent.putExtra(PARAM_AUDIO_CODECS, audioCodecs);
        intent.putExtra(PARAM_VIDEO_CODECS, videoCodecs);
        intent.putExtra(PARAM_EXISTING_AUDIO, existsAudio);
        intent.putExtra(PARAM_EXISTING_VIDEO, existsVideo);
        intent.putExtra(PARAM_SIP_MESSAGE, sipMessage);
        startServiceInThread(context, intent);
    }
    //
    // acceptIncomingCall
    //
    public static void acceptIncomingCall(Context context, String accountID) {
        Logger.error(TAG, "Accept Incoming Call");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_ACCEPT_INCOMING_CALL);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        startServiceInThread(context, intent);
    }
    //
    // declineIncomingCall
    //
    public static void declineIncomingCall(Context context, String accountID, long sessionId, int code) {
        Logger.error(TAG, "Decline Incoming Call");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_DECLINE_INCOMING_CALL);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_SESSION_ID, sessionId);
        intent.putExtra(PARAM_CODE, code);
        startServiceInThread(context, intent);
    }
    //
    // declineIncomingCall
    //
    public static void rejectIncomingCall(Context context, String accountID, long sessionId, int code) {
        Logger.error(TAG, "Reject Incoming Call");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_REJECT_INCOMING_CALL);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_SESSION_ID, sessionId);
        intent.putExtra(PARAM_CODE, code);
        startServiceInThread(context, intent);
    }
    //
    // hangupCall
    //
    public static void hangUpCall(Context context, String accountID) {
        Logger.error(TAG, "Hangup Call");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_HANG_UP_CALL);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        startServiceInThread(context, intent);
    }
    //
    // hangupAllCalls
    //
    public static void hangUpAllCalls(Context context, String accountID) {
        Logger.error(TAG, "Hangup Call");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_HANG_UP_CALLS);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        startServiceInThread(context, intent);
    }
    //
    //releaseCurrentLine
    //
    public static void releaseCurrentLine(Context context) {
        Logger.error(TAG, "Release line");

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_RELEASE_LINE);
        startServiceInThread(context, intent);
    }
    //
    // createAccount
    //
    public static void createAccount(Context context, SipAccountData sipAccountData) {
        Logger.error(TAG, "Create Account Is Active = " + sipAccountData.isActive());
        if (sipAccountData == null) {
            throw new IllegalArgumentException("Account must not be equal null");
        }

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_CREATE_ACCOUNT);
        intent.putExtra(PARAM_ACCOUNT_DATA, sipAccountData);
        startServiceInThread(context, intent);
    }
    //
    // updateAccount
    //
    public static void updateAccounts(Context context, ArrayList<SipAccountData> accountsList) {
        Logger.error(TAG, "Update Accounts");
        if (accountsList == null) {
            throw new IllegalArgumentException("ObjectDbs must not be equal null");
        }
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_UPDATE_ACCOUNTS_LIST);
        intent.putExtra(PARAM_ACCOUNT_DATA, accountsList);
        startServiceInThread(context, intent);

    }
    //
    // makeCall
    //
    public static void makeCall(Context context, String accountID, String numberToCall) {
        Logger.error(TAG, "Make call");
        //TODO check is number call is valid
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_MAKE_CALL);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_DIAL_NUMBER, numberToCall);
        startServiceInThread(context, intent);
    }
    //
    // getState
    //
    public static void getState(Context context, String accountID) {
        Logger.error(TAG, "Get State");
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_SHOW_ACCOUNT_SATE);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        startServiceInThread(context, intent);
    }
    //
    // getAllAccountState
    //
    public static void getAllAccountState(Context context) {
        Logger.error(SipService.TAG, "getAllAccountState: SIP SERVICE COMMANDS ");
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_SHOW_ALL_ACCOUNTS_SATE);
        startServiceInThread(context, intent);
    }
    //
    // checkAccount
    //
    private static boolean checkAccount(String accountID) {
        if (accountID == null || accountID.isEmpty() || !accountID.startsWith("sip:")) {
            Toast.makeText(App.getContext(), "Invalid accountID! Example: sip:user@domain", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Remove a SIP account.
     * @param context application context
     * @param accountID account ID uri
     */
    public static void removeAccount(Context context, String accountID) {
        Logger.error(TAG, "Remove Account");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_REMOVE_ACCOUNT);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        startServiceInThread(context, intent);
    }

    /**
     * Remove a SIP account.
     * @param context application context
     * @param accountID account ID uri
     */
    public static void removeAccount(Context context, String accountID, int objectId) {
        Logger.error(TAG, "Remove Account");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_REMOVE_ACCOUNT);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_OBJECT_ID, objectId);
        startServiceInThread(context, intent);
    }

    /**
     * Send DTMF. If the call does not exist or has been terminated, a disconnected
     * state will be sent to
     * @param context application context
     * @param accountID account ID
     * @param dtmfTone DTMF tone to send (e.g. number from 0 to 9 or # or *).
     *                 You can send only one DTMF at a time.
     */
    public static void sendDTMF(Context context, String accountID, long sesionID, String dtmfTone) {
        if (!checkAccount(accountID))
            return;
        Logger.error(TAG, "Transferring code = " + dtmfTone);
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_SEND_DTMF);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_SESSION_ID, sesionID);
        intent.putExtra(PARAM_DTMF, dtmfTone);
        startServiceInThread(context, intent);
    }


    /**
     * Sets mute status for a call. If the call does not exist or has been terminated, a disconnected
     * state will be sent to
     * @param context application context
     * @param accountID account ID
     * @param mute true to mute the call, false to un-mute it
     */
    public static void setCallMute(Context context, String accountID, boolean mute) {
        Logger.error(TAG, "Set Call Mute");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_SET_MUTE);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_MUTE, mute);
        startServiceInThread(context, intent);
    }

    /**
     * Sets route for audio output.
     * @param context application context
     * @param accountID account ID
     * @param toSpeaker true to set output route to loudspeaker else to default route
     */
    public static void setLoudspeaker(Context context, String accountID, boolean toSpeaker) {
        Logger.error(TAG, "Set Loud Speaker");
        if (!checkAccount(accountID))
            return;

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_SET_LOUDSPEAKER);
        intent.putExtra(PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(PARAM_TO_SPEAKER, toSpeaker);
        startServiceInThread(context, intent);
    }

    public static void changeRegistration(Context context, String accountId, boolean state) {

        Intent intent = new Intent(context, SipService.class);
        intent.setAction(ACTION_CHANGE_REGISTRATION);
        intent.putExtra(PARAM_ACCOUNT_ID, accountId);
        intent.putExtra(PARAM_REGISTRATION_STATE, state);
        startServiceInThread(context, intent);
    }

}
