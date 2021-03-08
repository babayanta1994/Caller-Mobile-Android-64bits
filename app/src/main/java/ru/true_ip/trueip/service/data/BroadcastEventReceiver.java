package ru.true_ip.trueip.service.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;

import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.CALL_STATE;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.CODEC_PRIORITIES;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.CODEC_PRIORITIES_SET_STATUS;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.INCOMING_CALL;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.OUTGOING_CALL;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.REGISTRATION;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.REJECT_CALL;
import static ru.true_ip.trueip.service.data.BroadcastEventEmitter.BroadcastAction.STACK_STATUS;


/**
 *
 * Created by Andrey Filimonov on 10.10.2017.
 */

public class BroadcastEventReceiver extends BroadcastReceiver {
    private final static String TAG = "BroadcastEventReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        BroadcastEventEmitter.BroadcastAction action = BroadcastEventEmitter.BroadcastAction.valueOf(intent.getAction());
        switch (action) {
            case REFRESH_REGISTRATION:
                onRefreshRegistration(intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID));
                break;
            case REGISTRATION:
                int stateCode = intent.getIntExtra(BroadcastEventEmitter.BroadcastParameters.CODE, -1);
                onRegistration(intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID),stateCode);
                break;
            case INCOMING_CALL:
                onIncomingCall(intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID),
                        intent.getIntExtra(BroadcastEventEmitter.BroadcastParameters.CALL_ID, -1),
                        intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.DISPLAY_NAME),
                        intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.REMOTE_URI));
                break;
            case CALL_STATE:
                break;
            case OUTGOING_CALL:
                onOutgoingCall(intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID),
                        intent.getIntExtra(BroadcastEventEmitter.BroadcastParameters.CALL_ID, -1),
                        intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.NUMBER));
                break;
            case STACK_STATUS:
                onStackStatus(intent.getBooleanExtra(BroadcastEventEmitter.BroadcastParameters.STACK_STARTED, false));
                break;
            case CODEC_PRIORITIES:
                ArrayList<CodecPriority> codecList = intent.getParcelableArrayListExtra(BroadcastEventEmitter.BroadcastParameters.CODEC_PRIORITIES_LIST);
                onReceivedCodecPriorities(codecList);
                break;
            case CODEC_PRIORITIES_SET_STATUS:
                onCodecPrioritiesSetStatus(intent.getBooleanExtra(BroadcastEventEmitter.BroadcastParameters.SUCCESS, false));
                break;
            case REJECT_CALL:
                onRejectCall(context, intent.getStringExtra(BroadcastEventEmitter.BroadcastParameters.ACCOUNT_ID),
                        intent.getLongExtra(BroadcastEventEmitter.BroadcastParameters.SESSION_ID, -1L));
                break;
        }
    }

    public void onRefreshRegistration(String accountId) {

    }

    public void register(final Context context) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastEventEmitter.getAction(REGISTRATION));
        intentFilter.addAction(BroadcastEventEmitter.getAction(INCOMING_CALL));
        intentFilter.addAction(BroadcastEventEmitter.getAction(CALL_STATE));
        intentFilter.addAction(BroadcastEventEmitter.getAction(OUTGOING_CALL));
        intentFilter.addAction(BroadcastEventEmitter.getAction(STACK_STATUS));
        intentFilter.addAction(BroadcastEventEmitter.getAction(CODEC_PRIORITIES));
        intentFilter.addAction(BroadcastEventEmitter.getAction(CODEC_PRIORITIES_SET_STATUS));
        intentFilter.addAction(BroadcastEventEmitter.getAction(REJECT_CALL));
        context.registerReceiver(this, intentFilter);
    }

    public void unregister(final Context context) {
        context.unregisterReceiver(this);
    }

    public void onRegistration(String accountSipNumber, int registrationStateCode) {
        Logger.error(TAG, "onRegistration: ACCOUNT ID: " + accountSipNumber + " : REGISTRATION STATE CODE" + registrationStateCode);
    }

    public void onIncomingCall(String accountID, int callID, String displayName, String remoteUri) {
        Logger.error(TAG, "onIncomingCall - accountID: " + accountID +
                ", callID: " + callID +
                ", displayName: " + displayName +
                ", remoteUri: " + remoteUri);
    }

    public void onOutgoingCall(String accountID, int callID, String number) {
        Logger.error(TAG, "onOutgoingCall - accountID: " + accountID +
                ", callID: " + callID +
                ", number: " + number);
    }

    public void onStackStatus(boolean started) {
        Logger.error(TAG, "SIP service stack " + (started ? "started" : "stopped"));
    }

    public void onReceivedCodecPriorities(ArrayList<CodecPriority> codecPriorities) {
        Logger.error(TAG, "Received codec priorities");
        for (CodecPriority codec : codecPriorities) {
            Logger.error(TAG, codec.toString());
        }
    }

    public void onCodecPrioritiesSetStatus(boolean success) {
        Logger.error(TAG, "Codec priorities " + (success ? "successfully set" : "set error"));
    }

    public void onRejectCall(Context context, String accountID, long sessionID ) {
        Logger.error(TAG,"Calling Sip Service Command to decline Incoming Call");
        SipServiceCommands.declineIncomingCall(context, accountID, sessionID, 486);
    }
}
