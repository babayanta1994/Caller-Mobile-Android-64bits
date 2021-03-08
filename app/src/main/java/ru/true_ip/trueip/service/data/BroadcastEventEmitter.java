package ru.true_ip.trueip.service.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ru.true_ip.trueip.app.device_screen.DeviceActivity;
import ru.true_ip.trueip.service.service.Logger;

/**
 * Created by Eugen on 10.10.2017.
 */

public class BroadcastEventEmitter {
    private final static String TAG = "BroadcastEventEmitter";

    private Context context;

    public enum BroadcastAction {
        REGISTRATION,
        REFRESH_REGISTRATION,
        ON_INVITE_INCOMING,
        INCOMING_CALL,
        CALL_STATE,
        OUTGOING_CALL,
        STACK_STATUS,
        CODEC_PRIORITIES,
        CODEC_PRIORITIES_SET_STATUS,
        REJECT_CALL,
        ACCOUNT_REMOVED,
        ACCOUNT_CREATED,
        DO_FINISH
    }

    public class BroadcastParameters {
        public static final String ACCOUNT_ID = "account_id";
        public static final String CALL_ID = "call_id";
        public static final String CODE = "code";
        public static final String REMOTE_URI = "remote_uri";
        public static final String DISPLAY_NAME = "display_name";
        public static final String CALL_STATE = "call_state";
        public static final String NUMBER = "number";
        public static final String CONNECT_TIMESTAMP = "connectTimestamp";
        public static final String STACK_STARTED = "stack_started";
        public static final String CODEC_PRIORITIES_LIST = "codec_priorities_list";
        public static final String LOCAL_HOLD = "local_hold";
        public static final String LOCAL_MUTE = "local_mute";
        public static final String SUCCESS = "success";

        //portsip
        public static final String SESSION_ID = "session_id";
        public static final String CALLER_DISPLAY_NAME = "callerDisplayName";
        public static final String CALLER = "caller";
        public static final String CALLEE_DISPLAY_NAME = "calleeDisplayName";
        public static final String CALLEE  = "callee";
        public static final String AUDIO_CODECS = "audioCodecs";
        public static final String VIDEO_CODECS = "videoCodecs";
        public static final String EXISTING_AUDIO = "existsAudio";
        public static final String EXISTING_VIDEO = "existsVideo";
        public static final String SIP_MESSAGE = "sipMessage";
        public static final String OBJECT_ID = "OBJECT_ID";

        private final static String DO_FINISH = "DO_FINISH";
    }

    public BroadcastEventEmitter(Context context) {
        this.context = context;
    }
    /*
     *
     */
    public void refreshRegistration(String accountID) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.REFRESH_REGISTRATION));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);

        context.sendBroadcast(intent);
    }
    /*
     * Registration State
     */
    public void registrationState(String accountID, int registrationStateCode) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.REGISTRATION));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);
        intent.putExtra(BroadcastParameters.CODE, registrationStateCode);

        context.sendBroadcast(intent);
    }
    /*
     * accountRemoved
     */
    public void accountRemoved(String accountID, int objectId) {
        Intent intent = new Intent(getAction(BroadcastAction.ACCOUNT_REMOVED));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID != null ? accountID : "");
        intent.putExtra(BroadcastParameters.OBJECT_ID, objectId);

        context.sendBroadcast(intent);
    }
    /*
     * accountCreated
     */
    public void accountCreated(String accountID) {
        Intent intent = new Intent(getAction(BroadcastAction.ACCOUNT_CREATED));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID != null ? accountID : "");

        context.sendBroadcast(intent);
    }
    /*
     * OnInviteIncoming
     */
    public void onInviteIncoming(String accountID,
                                 long sessionId,
                                 String callerDisplayName,
                                 String caller,
                                 String calleeDisplayName,
                                 String callee,
                                 String audioCodecs,
                                 String videoCodecs,
                                 boolean existsAudio,
                                 boolean existsVideo,
                                 String sipMessage) {

        Logger.error(TAG, "OnInviteIncoming");
        final Intent intent = new Intent();

        intent.setAction("ru.true_ip.trueip.INCALL");
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);
        intent.putExtra(BroadcastParameters.SESSION_ID, sessionId);
        intent.putExtra(BroadcastParameters.CALLER_DISPLAY_NAME, callerDisplayName);
        intent.putExtra(BroadcastParameters.CALLER, caller);
        intent.putExtra(BroadcastParameters.CALLEE_DISPLAY_NAME, calleeDisplayName);
        intent.putExtra(BroadcastParameters.CALLEE, callee);
        intent.putExtra(BroadcastParameters.AUDIO_CODECS, audioCodecs);
        intent.putExtra(BroadcastParameters.VIDEO_CODECS, videoCodecs);
        intent.putExtra(BroadcastParameters.EXISTING_AUDIO, existsAudio);
        intent.putExtra(BroadcastParameters.EXISTING_VIDEO, existsVideo);
        intent.putExtra(BroadcastParameters.SIP_MESSAGE, sipMessage);
        //context.sendBroadcast(intent);

        Bundle extras = intent.getExtras();
        if ( extras!=null) {
            DeviceActivity.start(context, extras);
        }

    }


    public static String getAction(BroadcastAction action) {
        return action.name();
    }

    /**
     * Emit an incoming call broadcast intent.
     * @param accountID call's account IdUri
     * @param callID call ID number
     * @param displayName the display name of the remote party
     * @param remoteUri the IdUri of the remote party
     */
    public void incomingCall(String accountID, int callID, String displayName, String remoteUri) {
        //Logger.error(TAG, "---> incomingCall");
        final Intent intent = new Intent();

        intent.setAction("ru.true_ip.trueip.INCALL"); //getAction(BroadcastAction.INCOMING_CALL));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);
        intent.putExtra(BroadcastParameters.CALL_ID, callID);
        intent.putExtra(BroadcastParameters.DISPLAY_NAME, displayName);
        intent.putExtra(BroadcastParameters.REMOTE_URI, remoteUri);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.sendBroadcast(intent);
    }
    //
    // Call State
    //
    public void callState(String accountID, int callStateCode) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.CALL_STATE));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);
        intent.putExtra(BroadcastParameters.CALL_STATE, callStateCode);

        context.sendBroadcast(intent);
    }

    public void stackStatus(boolean started) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.STACK_STATUS));
        intent.putExtra(BroadcastParameters.STACK_STARTED, started);

        context.sendBroadcast(intent);
    }

    public void codecPrioritiesSetStatus(boolean success) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.CODEC_PRIORITIES_SET_STATUS));
        intent.putExtra(BroadcastParameters.SUCCESS, success);

        context.sendBroadcast(intent);
    }

    public void codecPriorities(ArrayList<CodecPriority> codecPriorities) {
        final Intent intent = new Intent();

        intent.setAction(getAction(BroadcastAction.CODEC_PRIORITIES));
        intent.putParcelableArrayListExtra(BroadcastParameters.CODEC_PRIORITIES_LIST, codecPriorities);

        context.sendBroadcast(intent);
    }

    public void rejectCall(String accountID, long sessionId) {
        Logger.error(TAG,"Sending Reject Call Intent");
        final Intent intent = new Intent();
        intent.setAction(getAction(BroadcastAction.REJECT_CALL));
        intent.putExtra(BroadcastParameters.ACCOUNT_ID, accountID);
        intent.putExtra(BroadcastParameters.SESSION_ID, sessionId);
        context.sendBroadcast(intent);
    }
}
