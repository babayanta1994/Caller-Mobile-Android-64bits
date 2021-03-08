package ru.true_ip.trueip.service.portsip;

/**
 * Created by Andrey Filimonov on 28.02.2018.
 */

public class Line extends Session {
    int index = 0;
    public static final int LINE_BASE = 0;
    public static final int MAX_LINES = 8;
    String mDescriptionString = "";
    final String lineName;

    public Line(int index) {
        this.index = index;
        lineName = "line-" + index;
    }

    String getStatusString() {
        String status = "";
        if (!getSessionState() && !getRecvCallState()) {
            status += getDescriptionString();
            return status;
        }
        if (!getSessionState()) {
            status += " idle";
            return status;
        } else {
            status += " busy";
        }
        if (getHoldState()) {
            status += " Hold";
        } else {
            status += " UnHold";
        }

        if (getConferenceState()) {
            status += " conference";
        }

        if (getReferState()) {
            status += " refered";
        }
        return status;
    }

    public String getLineName() {
        return "line-" + index;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return getLineName() + getStatusString();
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        super.reset();
        mDescriptionString= "";
    }

    public String getDescriptionString() {
        return mDescriptionString;
    }

    public void setDescriptionString(String descriptionString) {
        this.mDescriptionString = descriptionString;
    }
}
