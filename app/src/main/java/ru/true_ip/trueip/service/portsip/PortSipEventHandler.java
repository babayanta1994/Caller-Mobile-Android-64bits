package ru.true_ip.trueip.service.portsip;

import com.portsip.OnPortSIPEvent;

import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipService;

import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_CONNECTED;
import static ru.true_ip.trueip.service.portsip.CallStates.CALL_STATE_DISCONNECTED;


/**
 *
 * Created by Andrey Filimonov on 28.02.2018.
 */

public class PortSipEventHandler implements OnPortSIPEvent {
    private static String TAG = PortSipEventHandler.class.getSimpleName();

    private SipAccountData sipAccountData;
    private SipService service;
    private boolean isRegistered = false;
    private long longVal = -1;

    public PortSipEventHandler(SipService value) {
        print("Create new Handler.");
        service = value;
        sipAccountData = new SipAccountData("","","","");
    }

    public void setSipAccountData(SipAccountData data) {
        sipAccountData.setSipNumber(data.getSipNumber());
        sipAccountData.setPassword(data.getPassword());
        sipAccountData.setPort(data.getPort());
        sipAccountData.setRealm(data.getRealm());
        sipAccountData.setServerHost(data.getServerHost());
        TAG = PortSipEventHandler.class.getSimpleName() + " " + sipAccountData.getSipNumber();
    }

    boolean isRegistered() {
        return isRegistered;
    }

    void unRegistered() {
        isRegistered = false;
    }

    @Override
    public void onRegisterSuccess(String reason, int code, String sipMessage) {
        print( "onRegisterSuccess");
        isRegistered = true;
        service.getEventEmitter().registrationState(sipAccountData.getIdUri(), 1);
    }

    @Override
    public void onRegisterFailure(String s, int i, String s1) {
        print( "onRegisterFailure : " + s);
        isRegistered = false;
        service.getEventEmitter().registrationState(sipAccountData.getIdUri(), -1);
        service.performRefreshRegistration(sipAccountData.getIdUri());
    }
    //
    // onInviteIncoming
    //
    @Override
    public void onInviteIncoming(long sessionId,
                                 String callerDisplayName,
                                 String caller,
                                 String calleeDisplayName,
                                 String callee,
                                 String audioCodecs,
                                 String videoCodecs,
                                 boolean existsAudio,
                                 boolean existsVideo,
                                 String sipMessage) {
        print( "onInviteIncoming caller = " + caller + " shouldReject = " + shouldRejectCall());
        if (shouldRejectCall()) {
            print("Rejecting call");
            service.getEventEmitter().rejectCall(sipAccountData.getIdUri(),sessionId);
            return;
        }
        setRejectCall(true);
        print("Set reject to true onInviteIncoming");
        service.getEventEmitter().onInviteIncoming(sipAccountData.getIdUri(),
                sessionId,
                callerDisplayName, caller, calleeDisplayName, callee, audioCodecs, videoCodecs,
                existsAudio, existsVideo, sipMessage);
    }

    @Override
    public void onInviteTrying(long l) {
        print( "onInviteTrying");
        setRejectCall(true);
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CallStates.CALL_STATE_CONNECTING);
    }

    @Override
    public void onInviteSessionProgress(long l, String s, String s1, boolean b, boolean b1, boolean b2, String s2) {
        print( "onInviteSessionProgress");
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CallStates.CALL_STATE_CONNECTING);
    }

    @Override
    public void onInviteRinging(long l, String s, int i, String s1) {
        print( "onInviteRinging");
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CallStates.CALL_STATE_CONNECTING);
    }

    @Override
    public void onInviteAnswered(long l, String s, String s1, String s2, String s3, String s4, String s5, boolean b, boolean b1, String s6) {
        print( "onInviteAnswered Long = " + l);
        setRejectCall(true);
    }

    @Override
    public void onInviteFailure(long l, String s, int i, String s1) {
        print( "onInviteFailure");
        setRejectCall(false);
        longVal = -1;
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CallStates.CALL_STATE_FAILED);
    }

    @Override
    public void onInviteUpdated(long l, String s, String s1, boolean b, boolean b1, String s2) {
        print( "onInviteUpdated");
    }

    @Override
    public void onInviteConnected(long l) {
        print( "onInviteConnected long = " + l);
        setRejectCall(true);
        longVal = l;
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CALL_STATE_CONNECTED);
    }

    @Override
    public void onInviteBeginingForward(String s) {
        print( "onInviteBeginingForward");
    }

    @Override
    public void onInviteClosed(long l) {
        print( "onInviteClosed longVal = "+ longVal + " Long = " + l);
        setRejectCall(false);
        if ( longVal != l && longVal != -1)
            return;
        longVal = -1;
        print( "Set call state disconnected");
        service.getEventEmitter().callState(sipAccountData.getIdUri(), CALL_STATE_DISCONNECTED);
    }

    @Override
    public void onDialogStateUpdated(String s, String s1, String s2, String s3) {
        print( "onDialogStateUpdated");
    }

    @Override
    public void onRemoteHold(long l) {
        print( "onRemoteHold");
    }

    @Override
    public void onRemoteUnHold(long l, String s, String s1, boolean b, boolean b1) {
        print( "onRemoteUnHold");
    }

    @Override
    public void onReceivedRefer(long l, long l1, String s, String s1, String s2) {
        print( "onReceivedRefer");
    }

    @Override
    public void onReferAccepted(long l) {
        print( "onReferAccepted");
    }

    @Override
    public void onReferRejected(long l, String s, int i) {
        print( "onReferRejected");
    }

    @Override
    public void onTransferTrying(long l) {
        print( "onTransferTrying");
    }

    @Override
    public void onTransferRinging(long l) {
        print( "onTransferRinging");
    }

    @Override
    public void onACTVTransferSuccess(long l) {
        print( "onACTVTransferSuccess");
    }

    @Override
    public void onACTVTransferFailure(long l, String s, int i) {
        print( "onACTVTransferFailure");
    }

    @Override
    public void onReceivedSignaling(long l, String s) {
        //print( "onReceivedSignaling");
    }

    @Override
    public void onSendingSignaling(long l, String s) {
        print( "onSendingSignaling");
    }

    @Override
    public void onWaitingVoiceMessage(String s, int i, int i1, int i2, int i3) {
        print( "onWaitingVoiceMessage");
    }

    @Override
    public void onWaitingFaxMessage(String s, int i, int i1, int i2, int i3) {
        print( "onWaitingFaxMessage");
    }

    @Override
    public void onRecvDtmfTone(long l, int i) {
        print( "onRecvDtmfTone");
    }

    @Override
    public void onRecvOptions(String s) {
        //print( "onRecvOptions");
    }

    @Override
    public void onRecvInfo(String s) {
        print( "onRecvInfo");
    }

    @Override
    public void onRecvNotifyOfSubscription(long l, String s, byte[] bytes, int i) {
        print( "onRecvNotifyOfSubscription");
    }

    @Override
    public void onPresenceRecvSubscribe(long l, String s, String s1, String s2) {
        print( "onPresenceRecvSubscribe");
    }

    @Override
    public void onPresenceOnline(String s, String s1, String s2) {
        print( "onPresenceOnline");
    }

    @Override
    public void onPresenceOffline(String s, String s1) {
        print( "onPresenceOffline");
    }

    @Override
    public void onRecvMessage(long l, String s, String s1, byte[] bytes, int i) {
        print( "onRecvMessage");
    }

    @Override
    public void onRecvOutOfDialogMessage(String s, String s1, String s2, String s3, String s4, String s5, byte[] bytes, int i, String s6) {
        print( "onRecvOutOfDialogMessage");
    }

    @Override
    public void onSendMessageSuccess(long l, long l1) {
        print( "onSendMessageSuccess");
    }

    @Override
    public void onSendMessageFailure(long l, long l1, String s, int i) {
        print( "onSendMessageFailure");
    }

    @Override
    public void onSendOutOfDialogMessageSuccess(long l, String s, String s1, String s2, String s3) {
        print( "onSendOutOfDialogMessageSuccess");
    }

    @Override
    public void onSendOutOfDialogMessageFailure(long l, String s, String s1, String s2, String s3, String s4, int i) {
        print( "onSendOutOfDialogMessageFailure");
    }

    @Override
    public void onSubscriptionFailure(long l, int i) {
        print( "onSubscriptionFailure");
    }

    @Override
    public void onSubscriptionTerminated(long l) {
        print( "onSubscriptionTerminated");
    }

    @Override
    public void onPlayAudioFileFinished(long l, String s) {
        print( "onPlayAudioFileFinished");
    }

    @Override
    public void onPlayVideoFileFinished(long l) {
        print( "onPlayVideoFileFinished");
    }

    @Override
    public void onReceivedRTPPacket(long l, boolean b, byte[] bytes, int i) {
        print( "onReceivedRTPPacket");
    }

    @Override
    public void onSendingRTPPacket(long l, boolean b, byte[] bytes, int i) {
        print( "onSendingRTPPacket");
    }

    @Override
    public void onAudioRawCallback(long l, int i, byte[] bytes, int i1, int i2) {
        print( "onAudioRawCallback");
    }

    @Override
    public void onVideoRawCallback(long l, int i, int i1, int i2, byte[] bytes, int i3) {
        print( "onVideoRawCallback");
    }

    private static void print(String message) {
        Logger.error(TAG+"===>", message);
    }

    private boolean isShouldReject = false;
    private void setRejectCall(boolean value) {
        print("Setting Reject to " + value);
        isShouldReject = value;
    }
    private boolean shouldRejectCall() {
        return isShouldReject;
    }

    public void clearRejectCallFlag() {
        print("Setting Reject to false");
        isShouldReject = false;
        longVal = -1;
    }

//    @Override
//    public void onVideoDecodedInfoCallback(long l, int i, int i1, int i2, int i3) {
//        print( "onVideoDecodedInfoCallback");
//    }
}
