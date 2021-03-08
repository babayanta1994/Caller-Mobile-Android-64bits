package ru.true_ip.trueip.service.portsip;

import com.portsip.PortSipErrorcode;

import java.util.Observable;

/**
 * Created by Andrey Filimonov on 28.02.2018.
 */

public class Session extends Observable {
    public long mSessionId = PortSipErrorcode.INVALID_SESSION_ID;
    public boolean mHoldState = false;
    public boolean mSessionState = false;
    public boolean mConferenceState = false;
    public boolean mRecvCallState = false;
    public boolean mHasEarlyMedia = false;
    public boolean mVideoState = false;
    public int mVideoWidth = 352;
    public int mVideoHeight= 288;
    public boolean mIsReferCall = false;
    public long mOriginSessionId = PortSipErrorcode.INVALID_SESSION_ID;

    public void reset() {
        mSessionId = PortSipErrorcode.INVALID_SESSION_ID;
        mHoldState = false;
        mSessionState = false;
        mConferenceState = false;
        mRecvCallState = false;
        mHasEarlyMedia = false;
        mVideoState = false;
        mIsReferCall = false;
        mVideoWidth = 352;
        mVideoHeight= 288;
        mOriginSessionId = PortSipErrorcode.INVALID_SESSION_ID;
    }

    public boolean hasEarlyMeida() {
        return mHasEarlyMedia;
    }

    public void setEarlyMeida(boolean earlyMedia) {
        mHasEarlyMedia = earlyMedia;
    }

    public boolean isReferCall() {
        return mIsReferCall;
    }

    public long getOriginCallSessionId() {
        return mOriginSessionId;
    }

    public void setReferCall(boolean referCall, long l) {
        mIsReferCall = referCall;
        mOriginSessionId = l;
    }

    public void setVideoHeight(int videoHeight) {
        this.mVideoHeight = videoHeight;
        setChanged();
    }

    public void setVideoWidth(int videoWidth) {
        this.mVideoWidth = videoWidth;
        setChanged();
    }


    public void setVideosizeChanged(){
        setChanged();
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoWidth(){
        return mVideoWidth;
    }
    public void setSessionId(long sessionId) {
        mSessionId = sessionId;
    }

    public long getSessionId() {
        return mSessionId;
    }

    public void setHoldState(boolean state) {
        mHoldState = state;
    }

    public boolean getHoldState() {
        return mHoldState;
    }

    public void setSessionState(boolean state) {
        mSessionState = state;
    }

    public boolean getSessionState() {
        return mSessionState;
    }

    public void setRecvCallState(boolean state) {
        mRecvCallState = state;
    }

    public boolean getRecvCallState() {
        return mRecvCallState;
    }

    public void setVideoState(boolean state) {
        mVideoState = state;
    }

    public boolean getVideoState() {
        return mVideoState;
    }


    public boolean getReferState() {
        return mIsReferCall;
    }

    boolean getConferenceState() {
        return mConferenceState;
    }
}
