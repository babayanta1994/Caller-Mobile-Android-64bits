package ru.true_ip.trueip.models.responses;

/**
 *
 * Created by Andrey Filimonov on 26.12.2017.
 */

public class PanelShortModel {
    public Integer id;
    public String name;
    public String sip_number;
    public String rtsp_link;
    public String login;
    public String password;
    public String ip_address;
    public String port;
    public String dtmf_1;
    public String dtmf_2;
    public String created_at;
    public String updated_at;
    public Integer is_callable;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSipNumber() { return sip_number; }
    public void setSipNumber(String sipNumber) { this.sip_number = sipNumber; }

    public String getRtsp_link() { return rtsp_link; }
    public void setRtsp_link(String rtsp_link) { this.rtsp_link = rtsp_link; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return ip_address;
    }

    public void setIpAddress(String ipAddress) {
        this.ip_address = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDtmf1() {
        return dtmf_1;
    }

    public void setDtmf1(String dtmf1) {
        this.dtmf_1 = dtmf1;
    }

    public String getDtmf2() {
        return dtmf_2;
    }

    public void setDtmf2(String dtmf2) {
        this.dtmf_2 = dtmf2;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    public Integer getIs_callable() {
        return is_callable;
    }

    public void setIs_callable(Integer is_callable) {
        this.is_callable = is_callable;
    }
}
