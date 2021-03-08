package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 22.09.2017.
 */

public class CameraModel {

    /**
     * id : 90
     * name : Adipisci repudiandae hic pariatur accusamus.
     * model : Jerde LLC
     * ip_address : 1.77.44.62
     * port : 1884
     * login : lavinia.rowe
     * password : VOlub/R
     * sip_number : c50834f8e6f9994aa33ea27515dbb546
     * rtsp_link : https://block.org/similique-rerum-earum-consequatur-adipisci-quam-eum-velit.html
     */

    private int id;
    private String name;
    private String model;
    private String ip_address;
    private int port;
    private String login;
    private String password;
    private String sip_number;
    private String rtsp_link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
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
}
