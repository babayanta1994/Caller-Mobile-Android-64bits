package ru.true_ip.trueip.service.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eugen on 10.10.2017.
 */

public class SipAccountData implements Parcelable {

    private String sipNumber;
    private String password;
    private String serverHost;
    private String realm;
    private String port;
    private boolean isActive;

    public SipAccountData(String serverHost, String username, String password, String port) {
        this.serverHost = serverHost;
        this.sipNumber = username;
        this.password = password;
        this.realm = "*";
        this.port = port;
        this.isActive = true;
    }

    protected SipAccountData(Parcel in) {
        sipNumber = in.readString();
        password = in.readString();
        serverHost = in.readString();
        realm = in.readString();
        port = in.readString();
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sipNumber);
        dest.writeString(password);
        dest.writeString(serverHost);
        dest.writeString(realm);
        dest.writeString(port);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SipAccountData> CREATOR = new Creator<SipAccountData>() {
        @Override
        public SipAccountData createFromParcel(Parcel in) {
            return new SipAccountData(in);
        }

        @Override
        public SipAccountData[] newArray(int size) {
            return new SipAccountData[size];
        }
    };

    public SipAccountData setSipNumber(String sipNumber) {
        this.sipNumber = sipNumber;
        return this;
    }

    public SipAccountData setPassword(String password) {
        this.password = password;
        return this;
    }

    public SipAccountData setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    public SipAccountData setPort(String port) {
        this.port = port;
        return this;
    }

    public String getSipNumber() {
        return sipNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getPort() {
        return port;
    }

    public boolean isEqual(SipAccountData anotherSipAccountData) {
        return sipNumber.equals(anotherSipAccountData.getSipNumber())
                && password.equals(anotherSipAccountData.getPassword())
                && serverHost.equals(anotherSipAccountData.getServerHost());
    }

    public String getIdUri() {
        return "sip:" + sipNumber+"@"+serverHost;

    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }
}
