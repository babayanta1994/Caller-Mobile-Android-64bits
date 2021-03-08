package ru.true_ip.trueip.service.portsip;


import com.portsip.PortSipSdk;

import ru.true_ip.trueip.service.data.SipAccountData;

/**
 *
 * Created by Andrey Filimonov on 01.03.2018.
 */

public class MyPortSipSDK extends PortSipSdk {
    private SipAccountData sipAccountData = new SipAccountData("","","","");
    private PortSipEventHandler eventHandler;

    public void setSipAccountData(SipAccountData data) {
        sipAccountData = data;
    }

    public SipAccountData getSipAccountData() {
        return sipAccountData;
    }

    public void setPortSipEventHandler(PortSipEventHandler handler) {
        eventHandler = handler;
    }
    public PortSipEventHandler getPortSipEventHandler() {
        return eventHandler;
    }

    public boolean isRegistered() {
        return eventHandler.isRegistered();
    }

    @Override
    public int unRegisterServer() {
        eventHandler.unRegistered();
        return super.unRegisterServer();
    }
}
