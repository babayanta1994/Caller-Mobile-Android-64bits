package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.net.URI;

import ru.true_ip.trueip.models.responses.CameraModel;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by user on 27-Sep-17.
 *
 */

@Entity(tableName = "Devices")
public class DevicesDb {

    @PrimaryKey(autoGenerate = true)
    public int device_id;

    @ColumnInfo(name = "device_server_id")
    public int device_server_id;

    // 1 - вызывная панель, 2 - камера
    @ColumnInfo(name = "device_type")
    public int device_type;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "ip_address")
    public String ip_address;

    @ColumnInfo(name = "port")
    public String port;

    @ColumnInfo(name = "login")
    public String login;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "sip_number")
    public String sip_number;

    @ColumnInfo(name = "rtsp_link")
    public String rtsp_link;

    @ColumnInfo(name = "locks_count")
    public int locks_count;

    // 0 - Устройство создано пользователем локально на мобильном устройстве
    // 1 - Устройство загружено пользователем с Облака
    @ColumnInfo(name = "is_cloud")
    public int is_cloud;

    // 0 - Устройство не добавлено в список “Избранное”
    // 1 - Устройство добавлено в список “Избранное”.
    @ColumnInfo(name = "is_favorite")
    public int is_favorite;

    @ColumnInfo(name = "object_id")
    public int object_id;

    @ColumnInfo(name = "is_full_rstp")
    public Boolean is_full_rstp;

    @ColumnInfo(name = "is_additional_relay")
    public Boolean is_additional_relay;

    @ColumnInfo(name = "dftm1")
    public String dftm1;

    @ColumnInfo(name = "dftm2")
    public String dftm2;

    @ColumnInfo(name = "is_callable")
    public Boolean is_callable;

    public DevicesDb() {}

    public DevicesDb(CameraModel cameraModel, int object_id, int is_cloud) {
        this.device_server_id = cameraModel.getId();
        this.name = cameraModel.getName();
        this.model = cameraModel.getModel();
        this.ip_address = cameraModel.getIp_address();
        this.port = Integer.toString(cameraModel.getPort());
        this.login = cameraModel.getLogin();
        this.password = cameraModel.getPassword();
        this.sip_number = cameraModel.getSip_number();
        this.rtsp_link = cameraModel.getRtsp_link();
        this.device_type = Constants.TYPE_CAMERA;
        this.object_id = object_id;
        this.is_cloud = is_cloud;
        this.image = "";
    }

    public DevicesDb(CameraShortModel cameraModel, int object_id, int is_cloud) {
        this.device_server_id = cameraModel.getId();
        this.name = cameraModel.getName();
        this.model = "";
        this.ip_address = "";
        this.port = "";
        this.login = "";
        this.password = "";
        this.sip_number = "";
        this.rtsp_link = cameraModel.getRtsp_link();
        this.device_type = Constants.TYPE_CAMERA;
        this.object_id = object_id;
        this.is_cloud = is_cloud;
        this.image = "";
        this.dftm1 = "";
        this.dftm2 = "";
        this.locks_count = 0;
        this.is_favorite = 0;
    }

    public DevicesDb(PanelShortModel panelShortModel, int object_id, int is_cloud) {
        this.device_server_id = panelShortModel.getId();
        this.name = panelShortModel.getName();
        this.model = "";

        String ipAddress = panelShortModel.getIpAddress();
        this.ip_address = ipAddress != null && !ipAddress.isEmpty() ? ipAddress : "";

        String port = panelShortModel.getPort();
        this.port = port != null && !port.isEmpty() ? port : "";

        String login = panelShortModel.getLogin();
        this.login = login != null && !login.isEmpty() ? login : "";

        String password = panelShortModel.getPassword();
        this.password = password != null && !password.isEmpty() ? password : "";

        String sipNumber = panelShortModel.getSipNumber();
        this.sip_number = sipNumber != null && !sipNumber.isEmpty() ? sipNumber : "";

        String rtspLink = panelShortModel.getRtsp_link();
        this.rtsp_link = rtspLink != null && !rtspLink.isEmpty() ? rtspLink : "";

        this.device_type = Constants.TYPE_PANEL;
        this.object_id = object_id;
        this.is_cloud = is_cloud;
        this.image = "";

        String dftm1 = panelShortModel.getDtmf1();
        this.dftm1 = dftm1 != null && !dftm1.isEmpty() ? dftm1 : "";

        String dftm2 = panelShortModel.getDtmf2();
        this.dftm2 = dftm2 != null && !dftm2.isEmpty() ? dftm2 : "";

        this.locks_count = 0;
        if (!this.dftm1.isEmpty())
            this.locks_count++;
        if (!this.dftm2.isEmpty())
            this.locks_count++;
        this.is_favorite = 0;
        this.is_callable = panelShortModel.getIs_callable() == 1;
    }

    public Boolean getIs_additional_relay() {
        return is_additional_relay;
    }

    public void setIs_additional_relay(Boolean is_additional_relay) {
        this.is_additional_relay = is_additional_relay;
    }

    public String getDftm1() {
        return dftm1;
    }

    public void setDftm1(String dftm1) {
        this.dftm1 = dftm1;
    }

    public String getDftm2() {
        return dftm2;
    }

    public void setDftm2(String dftm2) {
        this.dftm2 = dftm2;
    }

    public Boolean is_full_rstp() {
        return is_full_rstp;
    }

    public void setIs_full_rstp(boolean is_full_rstp) {
        this.is_full_rstp = is_full_rstp;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIp_address() { return ip_address; }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSip_number() {
        return sip_number;
    }

    public void setSip_number(String sip_number) {
        this.sip_number = sip_number;
    }

    public String getRtsp_link() {
        return rtsp_link;
    }

    public void setRtsp_link(String rtsp_link) {
        this.rtsp_link = rtsp_link;
    }

    public int getLocks_count() {
        return locks_count;
    }

    public void setLocks_count(int locks_count) {
        this.locks_count = locks_count;
    }

    public int getIs_cloud() {
        return is_cloud;
    }

    public void setIs_cloud(int is_cloud) {
        this.is_cloud = is_cloud;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public static String getIpAddress (DevicesDb object) {
        String ip_address = object.getIp_address();
        if ( ip_address == null)
            return "";
        String hst = "";
        if (!ip_address.isEmpty()) {
            hst = ip_address;
        }
        else {
            URI url;
            try {
                //Logger.error("GetIp address", "Getting URI from " + object.getRtsp_link());
                url = new URI(object.getRtsp_link());
                hst = url.getHost();
                if ( hst == null)
                    hst = "";
            } catch (Exception e) {
                e.printStackTrace();
                hst = "";
            }
        }
        //Logger.error("-->","Returning host = " + hst);
        return hst;
    }

    public Boolean getIs_callable() {
        return is_callable;
    }

    public void setIs_callable(Boolean is_callable) {
        this.is_callable = is_callable;
    }

    public int getDevice_server_id() {
        return device_server_id;
    }

    public void setDevice_server_id(int device_server_id) {
        this.device_server_id = device_server_id;
    }
}
