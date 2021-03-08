package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 06.09.2017.
 */

public class LangModel {

    /**
     * lang : ru
     */

    private String locale;
    private String push_token;
    private String sip_number;

    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) { this.locale = locale; }

    public String getPush_token() { return push_token; }
    public void setPush_token(String push_token) { this.push_token = push_token; }

    public String getSip_number() { return sip_number; }
    public void setSip_number(String sip_number) { this.sip_number = sip_number; }
}
