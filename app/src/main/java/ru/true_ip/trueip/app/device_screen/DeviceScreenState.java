package ru.true_ip.trueip.app.device_screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Andrey Filimonov on 13.12.2017.
 */

public class DeviceScreenState implements Parcelable{

    private boolean isDeviceListShown;  //0
    private int IS_DEVICE_LIST_SHOWN = 0;

    private boolean isPanelShown; //1
    private int IS_PANEL_SHOWN = 1;

    private boolean isIncomingCallState; //2
    private int IS_INCOMING_CALL_STATE = 2;

    private boolean isMuted; //3
    private int IS_MUTED = 3;

    private boolean isLoudSpeakerOn; //4
    private int IS_LOUD_SPEAKER_ON = 4;

    private boolean isCallInProgress; //5
    private int IS_CALL_IN_PROGRESS = 5;

    private boolean isConversationInProgress; //6
    private int IS_CONVERSATION_IN_PROGRESS = 6;

    private boolean isShowButton; //7
    private int IS_SHOW_BUTTON = 7;

    private boolean isConciergeCall; //8
    private int IS_CONCIERGE_CALL = 8;

    private boolean isCamera;
    private int IS_CAMERA = 9;

    private boolean isFullScreen; //10
    private int IS_FULL_SCREEN = 10;

    private boolean isStateRestored;// 11
    private int IS_STATUS_RESTORED = 11; //11

    private boolean isCallRejected;
    private int IS_CALL_REJECTED = 12;

    private boolean isCallInvited;
    private int IS_CALL_INVITED = 13;

    private int NUMBER_OF_FIELDS = 14;

    private int currentCallId;
    private String concierge_sip;
    private int objectId;
    private int deviceId;
    private String accountId;
    private String displayName;
    private String remoteUri;
    private long sessionId;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBooleanArray(new boolean[] {
                this.isDeviceListShown, //0
                this.isPanelShown, //1
                this.isIncomingCallState, //2
                this.isMuted, //3
                this.isLoudSpeakerOn, //4
                this.isCallInProgress, //5
                this.isConversationInProgress, //6
                this.isShowButton, //7
                this.isConciergeCall, //8
                this.isCamera, //9
                this.isFullScreen, // 10
                this.isStateRestored, //11
                this.isCallRejected, //12
                this.isCallInvited}); //13
        parcel.writeInt(currentCallId);
        parcel.writeString(concierge_sip);
        parcel.writeInt(objectId);
        parcel.writeInt(deviceId);
        parcel.writeString(accountId);
        parcel.writeString(displayName);
        parcel.writeString(remoteUri);
        parcel.writeLong(sessionId);
    }

    public DeviceScreenState(boolean isDeviceListShown,
                             boolean isPanelShown,
                             boolean isFullScreen,
                             boolean isIncomingCallState,
                             boolean isMuted,
                             boolean isLoudSpeakerOn,
                             boolean isCallInProgress,
                             boolean isConversationInProgress,
                             boolean isShowButton,
                             boolean isConciergeCall,
                             boolean isCamera,
                             boolean isStateRestored,
                             boolean isCallRejected,
                             int currentCallId,
                             String concierge_sip,
                             int objectId,
                             int deviceId,
                             String accountId,
                             String displayName,
                             String remoteUri,
                             long sessionId,
                             boolean isCallInvited) {

        this.isDeviceListShown = isDeviceListShown;
        this.isPanelShown = isPanelShown;
        this.isFullScreen = isFullScreen;
        this.isIncomingCallState = isIncomingCallState;
        this.isMuted = isMuted;
        this.isLoudSpeakerOn = isLoudSpeakerOn;
        this.isCallInProgress = isCallInProgress;
        this.isConversationInProgress = isConversationInProgress;
        this.isShowButton = isShowButton;
        this.isConciergeCall = isConciergeCall;
        this.isCamera = isCamera;
        this.isStateRestored = isStateRestored;
        this.currentCallId = currentCallId;
        this.concierge_sip = concierge_sip;
        this.objectId = objectId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.displayName = displayName;
        this.remoteUri = remoteUri;
        this.isCallRejected = isCallRejected;
        this.isCallInvited = isCallInvited;
        this.sessionId = sessionId;
    }

    public DeviceScreenState(Parcel in) {
        boolean[] data = new boolean[NUMBER_OF_FIELDS];
        in.readBooleanArray(data);
        isDeviceListShown = data[IS_DEVICE_LIST_SHOWN];
        isPanelShown = data[IS_PANEL_SHOWN];
        isFullScreen = data[IS_FULL_SCREEN];
        isIncomingCallState = data[IS_INCOMING_CALL_STATE];
        isMuted = data[IS_MUTED];
        isLoudSpeakerOn = data[IS_LOUD_SPEAKER_ON];
        isCallInProgress = data[IS_CALL_IN_PROGRESS];
        isConversationInProgress = data[IS_CONVERSATION_IN_PROGRESS];
        isShowButton = data[IS_SHOW_BUTTON];
        isConciergeCall = data[IS_CONCIERGE_CALL];
        isCamera = data[IS_CAMERA];
        isStateRestored = data[IS_STATUS_RESTORED];
        isCallRejected = data[IS_CALL_REJECTED];
        isCallInvited = data[IS_CALL_INVITED];
        currentCallId = in.readInt();
        concierge_sip = in.readString();
        objectId  = in.readInt();
        deviceId  = in.readInt();
        accountId  = in.readString();
        displayName  = in.readString();
        remoteUri  = in.readString();
        sessionId = in.readLong();
    }

    public static final Parcelable.Creator<DeviceScreenState> CREATOR = new Parcelable.Creator<DeviceScreenState>() {

        @Override
        public DeviceScreenState createFromParcel(Parcel source) {
            return new DeviceScreenState(source);
        }

        @Override
        public DeviceScreenState[] newArray(int size) {
            return new DeviceScreenState[size];
        }
    };

    public boolean isDeviceListShown() { return isDeviceListShown; }
    public void setDeviceListShown(boolean deviceListShown) {
        isDeviceListShown = deviceListShown;
    }

    public boolean isPanelShown() { return isPanelShown; }
    public void setPanelShown(boolean panelShown) {
        isPanelShown = panelShown;
    }

    public boolean isFullScreen() { return isFullScreen; }
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public boolean isIncomingCallState() { return isIncomingCallState; }
    public void setIncomingCallState(boolean incomingCallState) {
        isIncomingCallState = incomingCallState;
    }

    public boolean isMuted() { return isMuted; }
    public void setMuted(boolean muted) { isMuted = muted; }

    public boolean isLoudSpeakerOn() { return isLoudSpeakerOn; }
    public void setLoudSpeakerOn(boolean loudSpeakerOn) {
        isLoudSpeakerOn = loudSpeakerOn;
    }

    public boolean isCallInProgress() { return isCallInProgress; }
    public void setCallInProgress(boolean callInProgress) {
        isCallInProgress = callInProgress;
    }

    public boolean isConversationInProgress() { return isConversationInProgress; }
    public void setConversationInProgress(boolean conversationInProgress) {
        isConversationInProgress = conversationInProgress;
    }

    public boolean isConciergeCall() { return isConciergeCall; }
    public void setConciergeCall(boolean conciergeCall) { isConciergeCall = conciergeCall; }

    public boolean isShowButton() { return isShowButton; }
    public void setShowButton(boolean showButton) {
        isShowButton = showButton;
    }

    public int getCurrentCallId() { return currentCallId; }
    public void setCurrentCallId(int currentCallId) {
        this.currentCallId = currentCallId;
    }

    public String getConcierge_sip() { return concierge_sip; }
    public void setConcierge_sip(String concierge_sip) { this.concierge_sip = concierge_sip; }

    public int getObjectId() { return objectId; }
    public void setObjectId(int objectId) { this.objectId = objectId; }

    public int getDeviceId() { return deviceId; }
    public void setDeviceId(int deviceId) { this.deviceId = deviceId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRemoteUri() { return remoteUri; }
    public void setRemoteUri(String remoteUri) { this.remoteUri = remoteUri; }

    public boolean isCamera() { return isCamera; }
    public void setCamera(boolean camera) { isCamera = camera; }

    public boolean isStateRestored() { return isStateRestored; }
    public void setStateRestored(boolean stateRestored) { isStateRestored = stateRestored; }

    public boolean isCallRejected() { return isCallRejected; }
    public void setCallRejected(boolean callRejected) { isCallRejected = callRejected; }

    public boolean isCallInvited() { return isCallInvited; }
    public void setIsCallInvided(boolean isCallInvided) { this.isCallInvited = isCallInvided; }

    public long getSessionId() { return sessionId; }
    public void setSessionId(long sessionId) { this.sessionId = sessionId; }
}
