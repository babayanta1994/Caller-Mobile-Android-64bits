package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 06.09.2017.
 */

public class UserModel {

    public class ServerUrl {
        private String url;
        private String license;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }
    }

    private int id;
    private String name;
    private String email;
    private Integer is_active;
    private String address;
    private String api_token;
    private String sip_server_address;
    private String sip_server_port;
    private String concierge_sip_number;
    private String sip_client_number;
    private String sip_client_password;
    private ServerUrl server;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getApi_token() {
        return api_token;
    }
    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSip_server_address() { return sip_server_address; }
    public void setSip_server_address(String sip_server_address) {
        this.sip_server_address = sip_server_address;
    }

    public String getSip_server_port() { return sip_server_port;}
    public void setSip_server_port(String sip_server_port) {
        this.sip_server_port = sip_server_port;
    }

    public String getConcierge_sip_number() { return concierge_sip_number; }
    public void setConcierge_sip_number(String concierge_sip_number) {
        this.concierge_sip_number = concierge_sip_number;
    }

    public void setSip_client_number(String sip_client_number) {
        this.sip_client_number = sip_client_number;
    }
    public String getSip_client_number() { return sip_client_number; }

    public void setSip_client_password(String sip_client_password) {
        this.sip_client_password = sip_client_password;
    }
    public String getSip_client_password() { return sip_client_password; }

    public ServerUrl getServer() {
        return server;
    }

    public void setServer(ServerUrl server) {
        this.server = server;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

}
