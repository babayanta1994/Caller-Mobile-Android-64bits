package ru.true_ip.trueip.service.portsip;

import com.portsip.PortSipEnumDefine;

/**
 * Created by Andrey Filimonov on 28.02.2018.
 */

public class UserInfo {
    String mUsername = "";
    String mUserpwd= "";
    String mSipServer= "";
    String mUserdomain= "";
    String mAuthName= "";
    String mUserDisName= "";
    String mStunSvr= "";

    int mStunPort= 5060;
    int msipPort= 5060;
    int mtransType= PortSipEnumDefine.ENUM_TRANSPORT_UDP;
    int msrtpType= PortSipEnumDefine.ENUM_SRTPPOLICY_NONE;

    public void setUserName(String userName){
        mUsername =userName;
    }

    public String getUserName() {
        return mUsername;
    }

    public void setUserPwd(String password){
        mUserpwd =password;
    }

    public String getUserPassword() {
        return mUserpwd;
    }

    public void setSipServer(String server){
        mSipServer =server;
    }

    public String getSipServer() {
        return mSipServer;
    }

    public void setUserDisplayName(String dispalyName){
        mUserDisName =dispalyName;
    }

    public String getUserDisplayName() {
        return mUserDisName;
    }

    public void setUserDomain(String domain){
        mUserdomain =domain;
    }
    public String getUserdomain() {
        return mUserdomain;
    }

    public void setAuthName(String authName){
        mAuthName = authName;
    }
    public String getAuthName() {
        return mAuthName;
    }

    public void setSipPort(int port){
        msipPort = port;
    }

    public int getSipPort(){
        return msipPort;
    }

    public void setStunServer(String stunServer){
        mStunSvr = stunServer;
    }

    public String getStunServer(){
        return mStunSvr;
    }

    public void setStunPort(int port){
        mStunPort = port;
    }

    public int getStunPort(){
        return mStunPort;
    }

    public void setTranType(int enum_transType){
        mtransType = enum_transType;
    }

    public int getTransType(){
        return mtransType;
    }

    public void setSrtpType(int enum_srtpType){
        msrtpType = enum_srtpType;
    }

    public int getSrtpType(){
        return msrtpType;
    }


    public boolean isAvailable(){

        if (mUsername != null && mUsername.length() > 0 &&
                mUserpwd!= null	&& mUserpwd.length() > 0 &&
                msipPort>0&&msipPort<65535&&
                mSipServer!= null&&mSipServer.length() > 0)// these fields are required
        {
            return true;
        }
        return false;
    }

}
