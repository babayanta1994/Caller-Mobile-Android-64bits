package ru.true_ip.trueip.utils;

import java.util.ArrayList;

/**
 * Created by Eugen on 30.10.2017.
 */

public class WifiHelper {

    public static final String TAG = WifiHelper.class.getSimpleName();
    private static WifiHelper instance;
    private static boolean isConnectedToWifi;
    private static Boolean isSendOnce;
    private ArrayList<WifiConnectionChange> wifiListenersList;

    public interface WifiConnectionChange {
        void wifiConnected(boolean connected);
    }

    private WifiHelper() {
        wifiListenersList = new ArrayList<>();
    }

    public static WifiHelper getInstance() {
        if (instance == null) {
            instance = new WifiHelper();
        }
        return instance;
    }

    public void setWifiConnected(boolean connected) {
        if (wifiListenersList != null && (isSendOnce == null || isSendOnce != connected)) {
            for (int i = 0; i < wifiListenersList.size(); i++) {
                wifiListenersList.get(i).wifiConnected(connected);
            }
            isSendOnce = connected;
        }
    }

    public void addWifiListener(WifiConnectionChange listener) {
        if (listener == null) {
            return;
        }
        wifiListenersList.add(listener);
    }

}
