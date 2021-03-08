package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Eugen on 25.09.2017.
 */

@Entity(tableName = "Objects")
public class ObjectDb {

    @PrimaryKey(autoGenerate = true)
    public int object_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "is_cloud")
    public Integer is_cloud;

    @ColumnInfo(name = "user_id")
    public Integer user_id;

    @ColumnInfo(name = "ip_address")
    public String ip_address;

    @ColumnInfo(name = "port")
    public String port;

    @ColumnInfo(name = "sip_number")
    public String sip_number;

    @ColumnInfo(name = "concierge_number")
    public String concierge_number;

    @ColumnInfo(name = "has_concierge")
    public Boolean has_concierge;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "flat_number")
    public String flat_number;

    @ColumnInfo(name = "activation_code")
    public String activation_code;

    @ColumnInfo(name = "server_url")
    public String serverUrl;

    @ColumnInfo(name = "is_blocked")
    public Integer isBlocked;

    @ColumnInfo(name = "is_server_active")
    public Integer isServerActive;

    public boolean isStatusActive;

    @ColumnInfo(name = "license_type")
    public String licenseType;

    @ColumnInfo(name = "is_object_active")
    public Integer isObjectActive = 1;

    public boolean isStatusActive() {
        return isStatusActive;
    }

    public void setStatusActive(boolean statusActive) {
        isStatusActive = statusActive;
    }

    public String getConcierge_number() {
        return concierge_number;
    }

    public void setConcierge_number(String concierge_number) {
        this.concierge_number = concierge_number;
    }

    public Boolean getHas_concierge() {
        return has_concierge;
    }

    public void setHas_concierge(Boolean has_concierge) {
        this.has_concierge = has_concierge;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSip_number() {
        return sip_number;
    }

    public void setSip_number(String sip_number) {
        this.sip_number = sip_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIs_cloud() {
        return is_cloud;
    }

    public void setIs_cloud(Integer is_cloud) {
        this.is_cloud = is_cloud;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setFlat_number(String flat_number) { this.flat_number = flat_number; }

    public String getFlat_number() { return flat_number; }

    public void setActivation_code(String activation_code) { this.activation_code = activation_code; }

    public String getActivation_code() { return activation_code; }

    public String getIdUri() {
        return "sip:" + sip_number + "@" + ip_address;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public boolean isBlocked() {
        return isBlocked != null && isBlocked == 1;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked ? 1 : 0;
    }

    public Integer getIsServerActive() {
        return isServerActive;
    }

    public void setIsServerActive(Integer isServerActive) {
        this.isServerActive = isServerActive;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public boolean IsObjectActive() {
        if ( isObjectActive == null)
            return true;
        return isObjectActive == 1;
    }

    public void setObjectActive(boolean value) {
        this.isObjectActive = value ? 1 : 0;
    }
}
